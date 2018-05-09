/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;


import com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers.CurrencyAPIController;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers.ExchangeRateAPIController;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers.GDAXAPIController;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.IObserver;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.ISubject;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.MathsHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.GDAXTrade;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Gap;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author jkell
 */
public class PriceCollector implements ISubject {
    private boolean connectedToDatabase;
    private Currency[] currencies;
    private CurrencyAPIController currencyAPIController;
    private ExchangeRateAPIController exchangeRateAPIController;
    private GDAXAPIController gdaxAPIController;
    private int lap;
    private transient List<IObserver> observers;
    private LocalDateTime firstRelevantRate;
    private ScheduledExecutorService collector;
    
    /**
     * Default PriceCollector constructor.
     */
    public PriceCollector() {
    }
    
    /**
     * Initialises required APIControllers, the collector thread and calculates
     * the oldest required ExchangeRate before initialising the Currency array
     * and the current and historic data collection.
     * @param readingsRequired The number of readings required to make predictions.
     */
    public void initialise(Integer readingsRequired) {
        gdaxAPIController = new GDAXAPIController();
        currencyAPIController = new CurrencyAPIController();
        exchangeRateAPIController = new ExchangeRateAPIController();
        collector = Executors.newSingleThreadScheduledExecutor();
        firstRelevantRate = LocalDateTimeHelper.startOfMinute(LocalDateTime.now(Clock.systemUTC()).minusMinutes(readingsRequired));
        connectedToDatabase = false;
        lap = 1;
        initCurrencies();
        initCollector();
    }
    
    /**
     * Attempts to connect to the database to obtain Currency data, initialises 
     * database use if successful, falls back on hard-coded Currency data if
     * unsuccessful to prevent error.
     */
    private void initCurrencies() {
        try {
            currencies = currencyAPIController.getCurrencies(Globals.API_ENDPOINT + Globals.CURRENCY_EXTENSION);
            if (currencies.length == 0){
                storageFreeMode();
            } else {
                System.out.println("[INFO] Successfully connected to Oracle database. Initialising catch up.");
                sortRelevantRates();
                connectedToDatabase = true;
            }
        } catch (Exception e){
            storageFreeMode();
        }
    }
    
    /**
     * Connects to the database to obtain ExchangeRate data for each Currency.
     * Separates obsolete data from useful data and then calculates any Gaps in
     * the useful data that need to be filled by the historic collector. Calculates
     * any growths not stored in the database and updates it as necessary.
     */
    private void sortRelevantRates() {
        ExchangeRate[] rates, updatedRates;
        for (Currency currency : currencies){
            rates = exchangeRateAPIController.getExchangeRates(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + currency.getID());
            for (ExchangeRate rate : rates){
                rate.setTimestamp(rate.getTimestamp());
                if (!rate.getLDTTimestamp().isBefore(firstRelevantRate)){
                    currency.addHistoricRate(rate);
                }
            }
            currency.findGaps(firstRelevantRate);
            currency.mergeRates();
            updatedRates = currency.calculateGrowth(false);
            exchangeRateAPIController.put(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, updatedRates);
        }
    }
    
    /**
     * Sets up Currency data manually to avoid errors when database connection is
     * unsuccessful. It then calculates the single Gap that needs to be filled -
     * between now and the oldest required ExchangeRate.
     */
    private void storageFreeMode() {
        /*
            Not intended for project - only as failsafe.
        */
        System.out.println("[INFO] Failed to connect to Oracle database. Initialising local mode.");
        currencies = new Currency[4];
        Currency newCurrency = new Currency("BCH", "Bitcoin Cash", Globals.BCH_TRADES);
        currencies[0] = newCurrency;
        newCurrency = new Currency("BTC", "Bitcoin", Globals.BTC_TRADES);
        currencies[1] = newCurrency;
        newCurrency = new Currency("ETH", "Ethereum", Globals.ETH_TRADES);
        currencies[2] = newCurrency;
        newCurrency = new Currency("LTC", "Litecoin", Globals.LTC_TRADES);
        currencies[3] = newCurrency;
        
        for (Currency currency : currencies){
            currency.findGaps(firstRelevantRate);
        }
    }
    
    /**
     * Initialises automated collection with a 1 second delay between the end of
     * a processing cycle and beginning of the next cycle. This is to ensure that
     * GDAX's API call threshold is not violated.
     */
    private void initCollector() {
        Runnable automatedCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            /**
             * Collects data from GDAX API endpoints. If there is no data for the 
             * current minute then that data is prioritised, otherwise collects
             * required historic data. Calculates growths when all Gaps in historic
             * data have been filled.
             */
            private void priceCollection() {
                LocalDateTime currentTime = LocalDateTimeHelper.startOfMinute(LocalDateTime.now(Clock.systemUTC()));
                LocalDateTime currentRateTime = null;
                try {
                    currentRateTime = currencies[0].getRate().getLDTTimestamp();
                } catch (Exception e){
                    //Protection from empty rate arraylist
                }
                
                if (!currentTime.equals(currentRateTime)) {
                    getCurrentPrices(currentTime);
                } else {
                    if (gapsFilled()){
                        calculateHistoricGrowth();
                    } else {
                        getHistoricPrices();
                    }
                }
            }
            
            /**
             * Collects current data from GDAX API endpoint. If no trades were 
             * made in the last minute then the price is carried forward from 
             * the previous minute. It also notifies Observers so that the GUI
             * can remain up to date with the latest prices.
             * @param postTime The time to collect data for - current UTC time.
             */
            private void getCurrentPrices(LocalDateTime postTime) {
                System.out.println("[INFO] Fetching resource from: " + Globals.GDAX_ENDPOINT + "/products/.../trades");
                try {
                    GDAXTrade[] trades;
                    Double previousPrice = null;
                    Double meanPrice;
                    int tradeID;
                    ExchangeRate rate;
                    ExchangeRate previousRate;
                    for (Currency currency : currencies){
                        rate = new ExchangeRate(currency.getID(), postTime, null, null, null, null, null);
                        trades = gdaxAPIController.getGDAXTrades(currency.getGDAXEndpoint());
                        trades = getRelevantTrades(trades, postTime);
                        previousRate = currency.getRate();
                        if (previousRate != null) {
                            if (rate.isMinuteAfter(previousRate)) {
                                previousPrice = previousRate.getValue();
                            }
                        }
                        if (0 < trades.length){
                            meanPrice = calculateMeanPrice(trades);
                            tradeID = trades[trades.length - 1].getTrade_id();
                        } else {
                            meanPrice = previousPrice;
                            tradeID = currency.getRate().getLastTrade();
                        }
                        rate.setValue(meanPrice);
                        rate.setLastTrade(tradeID);
                        currency.setValue(rate);
                        if (connectedToDatabase){
                            exchangeRateAPIController.post(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, rate);
                            if (0 < currency.getGaps().size() && 0 < trades.length) {
                                if (currency.getLastGap().getPaginationStart() == 0) {
                                    currency.getLastGap().setPaginationStart(trades[trades.length - 1].getTrade_id());
                                    currency.getLastGap().setStartTime(currency.getLastGap().getStartTime().minusMinutes(1));
                                }
                            }
                            if (lap == 3 || lap == -1) {
                                currency.getRate().calculateGrowth(previousPrice);
                                exchangeRateAPIController.put(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, rate);
                            }
                        }
                    }
                    notifyObservers();
                } catch (Exception e) {
                    System.out.println("[INFO] Error: " + e);
                }
            }
            
            /**
             * Extracts GDAXTrades from parameter array that occur in the minute
             * leading up to the parameter time.
             * @param trades Potentially relevant GDAXTrades.
             * @param postTime Minute to associate with ExchangeRate.
             * @return GDAXTrades at the required time.
             */
            private GDAXTrade[] getRelevantTrades(GDAXTrade[] trades, LocalDateTime postTime) {
                ArrayList<GDAXTrade> relevantTrades = new ArrayList<>();
                boolean foundStart = false;
                for (GDAXTrade trade : trades){
                    if (trade.getTime().equals(postTime.minusMinutes(1))) {
                    relevantTrades.add(trade);
                    foundStart = true;
                    } else if (foundStart) break;
                }
                return relevantTrades.toArray(new GDAXTrade[relevantTrades.size()]);
            }
            
            /**
             * Calculates the mean of the prices of the GDAXTrade array parameter.
             * @param trades The array of GDAXTrades to get the mean price of.
             * @return The mean price of the parameter.
             */
            private Double calculateMeanPrice(GDAXTrade[] trades) {
                List<Double> prices = new ArrayList<>();
                Double meanPrice;

                for (GDAXTrade trade : trades){
                    prices.add(trade.getPrice());
                }

                meanPrice = MathsHelper.mean(prices.toArray(new Double[prices.size()]));
                return meanPrice;
            }
            
            /**
             * Calculates whether all fillable Gaps have been filled for each
             * Currency. Removes any Gaps which have 0 rates required remaining.
             * @return true - filled, false - some data to collect
             */
            private boolean gapsFilled() {
                boolean filled = true;
                int ratesRequired = 0;
                List<Gap> gaps, toRemove;
                for (Currency currency : currencies){
                    gaps = currency.getGaps();
                    toRemove = new ArrayList<>();
                    for (Gap gap : gaps){
                        ratesRequired = gap.getRatesRequired();
                        if (ratesRequired == 0){
                            toRemove.add(gap);
                        }
                    }
                    if (!toRemove.isEmpty()){
                        gaps.removeAll(toRemove);
                    }
                    if (!gaps.isEmpty()) {
                        filled = false;
                    }
                }
                return filled;
            }
            
            /**
             * Merges historic ExchangeRates into the current ExchangeRate List
             * and then calculates growths between consecutive ExchangeRates
             * before storing new information in the database. If the collector
             * has already completed its second pass through the Gaps it assumes
             * that any missing data genuinely is missing and calculates any
             * missing ExchangeRates. Finally it sets up the collector for its
             * next phase: either a re-run through any Gaps to attempt to find
             * missing ExchangeRates or shutting down historic collection and
             * notifying Observers to begin predicting future prices.
             */
            private void calculateHistoricGrowth() {
                ExchangeRate[] updatedRates;
                for (Currency currency : currencies) {
                    currency.mergeRates();
                    updatedRates = currency.calculateGrowth((lap == 2));
                    if (connectedToDatabase) {
                        exchangeRateAPIController.put(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, updatedRates);
                    }
                    if (!currency.getHistoricRates().isEmpty()) {
                        currency.mergeRates();
                        updatedRates = currency.calculateGrowth((lap == 2));
                        if (connectedToDatabase){
                            exchangeRateAPIController.post(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, updatedRates);
                        }
                    }
                }
                if (lap == 1) {
                    for (Currency currency : currencies) {
                        currency.findGaps(firstRelevantRate);
                    }
                    System.out.println("[INFO] First lap completed. Attempting to collect failed prices.");
                    lap = 2;
                } else if (lap == 2) {
                    System.out.println("[INFO] Finished historic collection. Switching current price mode");
                    lap = 3;
                    /*
                    collector.shutdownNow();
                    */
                    notifyObservers();
                }
            }
            
            /**
             * Gathers the maximum number of GDAXTrades from the GDAX API endpoint
             * and groups each minute of data into a single ExchangeRate with a
             * mean price. It then crops any data that has already been collected
             * and if there is still useful data it is posted to the database. If
             * there is no useful data or the Gap's pagination can't be collected
             * properly (i.e. it is negative) then it discards the Gap as filled 
             * or unable to be filled. Since GDAX's API allows only 4 readings 
             * per second the historic collector will break after one get per
             * Currency under normal conditions, however, if one Currency has
             * been collected then it will wrap back to the first that hasn't
             * been finished to utilise the full 4 gets per second.
             */
            private void getHistoricPrices() {
                int readingsTaken = 0;
                GDAXTrade[] trades;
                while (readingsTaken < 4){
                    boolean needsMoreReadings = false;
                    for (Currency currency : currencies){
                        if (!currency.getGaps().isEmpty()){
                            Gap gap = currency.getLastGap();
                            if (!collectionCompleted(gap.getRatesRequired(), currency.noOfHistoricRates())){
                                trades = getHistoricTrades(currency, gap);
                                calculateHistoricAverages(currency, trades, gap);
                                boolean noNewData = currency.dumpDuplicates(trades[trades.length - 1].getTime().isBefore(gap.getStartTime().minusMinutes(gap.getRatesRequired())));
                                if (noNewData || gap.getPaginationStart() < 0) {
                                    currency.getGaps().remove(gap);
                                } else {
                                    if (connectedToDatabase){
                                        exchangeRateAPIController.post(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION,
                                                currency.getHistoricRates().toArray(new ExchangeRate[currency.getHistoricRates().size()]));
                                    }
                                    currency.gradualMerge();
                                }
                                readingsTaken++;
                            }
                            if (readingsTaken == 4) {
                                System.out.println("[INFO] 4 readings taken. 1 second break for GDAX API.");
                                break;
                            } else if (!collectionCompleted(gap.getRatesRequired(), currency.noOfHistoricRates())){
                                needsMoreReadings = true;
                            }
                        }
                    }
                    if (!needsMoreReadings){
                        break;
                    }
                }
            }
            
            /**
             * Calculates whether the number of collected readings exceeds the
             * required number.
             * @param required Target number.
             * @param collected Actual number.
             * @return true - collected >= required, else false
             */
            private boolean collectionCompleted(int required, int collected) {
                return (required <= collected);
            }
            
            /**
             * Uses GDAX pagination information to collect missing historical
             * GDAXTrade data.
             * @param currency The Currency to collect GDAXTrades for.
             * @param gap The Gap to fill.
             * @return GDAXTrade array of collected data.
             */
            private GDAXTrade[] getHistoricTrades(Currency currency, Gap gap) {
                String pagination = "";
                if (currency.getLastHistoricTrade() != null) {
                    pagination = "?after=" + currency.getLastHistoricTrade().getTrade_id();
                } else if (gap != null) {
                    Integer lastTrade = gap.getPaginationStart();
                    if (0 < lastTrade){
                        pagination = "?after=" + lastTrade;
                    }
                }
                return gdaxAPIController.getGDAXTrades(currency.getGDAXEndpoint() + pagination);
            }
            
            /**
             * Sorts an array of GDAXTrades minutely ExchangeRates and averages
             * the price. Always wraps the final minute into the next collection
             * so that all trades are used creating the mean.
             * @param currency The Currency to collect prices for.
             * @param trades The GDAXTrades to create ExchangeRates from.
             * @param gap The Gap being filled.
             */
            private void calculateHistoricAverages(Currency currency, GDAXTrade[] trades, Gap gap) {
                if (trades.length == 0) {
                    System.out.println("[INFO] calculateHistoricAverages received no trades. GDAX endpoint may be down...");
                    return;
                }
                ExchangeRate rate;
                Double meanPrice;
                LocalDateTime tradeTime;
                int missingReadings = 0;

                if (gap == null){
                    tradeTime = LocalDateTimeHelper.startOfMinute(LocalDateTime.now(Clock.systemUTC()));
                    System.out.println("[INFO] " + currency.getID() + " starting prices at " + tradeTime.getHour() + ":" + tradeTime.getMinute());
                } else {
                    tradeTime = gap.getStartTime();
                }
                
                while (tradeTime.isAfter(trades[0].getTime().plusMinutes(1))) {
                    tradeTime = tradeTime.minusMinutes(1);
                    missingReadings++;
                }
                gap.setStartTime(tradeTime);
                gap.setRatesRequired(gap.getRatesRequired() - missingReadings);

                for (GDAXTrade trade : trades){
                    if (currency.getHistoricRates().size() == gap.getRatesRequired()) {
                        break;
                    }
                    if (trade.getTime().equals(tradeTime.minusMinutes(1))) {
                        currency.addHistoricTrade(trade);
                    } else {
                        if (currency.hasFoundPosition()) {
                            meanPrice = calculateMeanPrice(currency.getHistoricTrades().toArray(new GDAXTrade[currency.getHistoricTrades().size()]));
                            rate = new ExchangeRate(currency.getID(), tradeTime.toString(), meanPrice, null, null, null, trade.getTrade_id());
                            currency.addHistoricRate(rate);
                            tradeTime = tradeTime.minusMinutes(1);
                            currency.addHistoricTrade(trade);
                        }
                    }
                }
            }
        };
        
        if (lap == 1) {
            collector.scheduleWithFixedDelay(automatedCollection, 1, 1, TimeUnit.SECONDS);
        }/* else {
            int initDelay = 60 - LocalDateTime.now().getSecond();
            if (initDelay == 60) {
                initDelay = 1;
            }
            collector.scheduleAtFixedRate(automatedCollection, initDelay, 60, TimeUnit.SECONDS);
        }*/
    }

    /**
     * Gets the LocalDateTime timestamp of the first ExchangeRate required to
     * perform price predictions.
     * @return The LocalDateTime timestamp of the first ExchangeRate required.
     */
    public LocalDateTime getFirstRelevantRate() {
        return firstRelevantRate;
    }
    
    /**
     * Updates the Currency data to include predictions and turns off historic 
     * collections.
     * @param currencies The new Currency data.
     */
    public void benchmarkComplete(Currency[] currencies) {
        this.currencies = currencies;
        lap = -1;
        /*
        initCollector();
        */
    }
    
    /**
     * Gets the current data collection lap.
     * @return The current data collection lap.
     */
    public int getLap(){
        return lap;
    }

    /**
     * Gets the currently collected Currency data.
     * @return The currently collected Currency data.
     */
    public Currency[] getCurrencies() {
        return currencies;
    }

    /**
     * Sets the Currency data to a new array of data (used to add the prediction
     * calculations).
     * @param currencies The new Currency data.
     */
    public void setCurrencies(Currency[] currencies) {
        this.currencies = currencies;
    }

    /**
     * Adds the parameter Observer to the Observer List.
     * @param o Observer to register.
     * @return true = registered correctly, false = failed to register.
     */
    @Override
    public Boolean registerObserver(IObserver o) {
        Boolean blnAdded = false;
        if (o != null) {
            if (this.observers == null) {
                this.observers = new ArrayList<>();
            }
            if (!this.observers.contains(o)) {
                blnAdded = this.observers.add(o);
            }
        }
        return blnAdded;
    }

    /**
     * Remove parameter Observer from the Observer List.
     * @param o Observer to be removed.
     * @return true = Observer successfully removed, false = failed to remove.
     */
    @Override
    public Boolean removeObserver(IObserver o) {
        Boolean blnRemoved = false;
        if (o != null) {
            if (this.observers != null && 0 < this.observers.size()) {
                blnRemoved = this.observers.remove(o);
            }
        }
        return blnRemoved;
    }

    /**
     * Notify each Observer to update itself due to a change of situation.
     */
    @Override
    public void notifyObservers() {
        if (this.observers != null && 0 < this.observers.size()) {
            for (IObserver currentObserver : this.observers) {
                currentObserver.update();
            }
        }    
    }
}
