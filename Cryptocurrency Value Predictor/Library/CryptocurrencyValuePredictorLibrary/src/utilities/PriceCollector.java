/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import controllers.APIController;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.Currency;

/**
 *
 * @author jkell
 */
public class PriceCollector {
    private APIController apiController = new APIController();
    private Currency[] currencies = new Currency[4];
    private JsonParser parser = new JsonParser();
    private ScheduledExecutorService current = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService historic = Executors.newSingleThreadScheduledExecutor();
    private final int startGOFAI = 21;
    private final int startNN = 1001;
    private int currentSecond;
    
    public PriceCollector() {
        //System.out.println("[INFO] Fetching resource from: " + apiController.API_ENDPOINT + "/currencies/");
        try {
            setUpCurrencies();
            /*
                Websocket listener will act as collector (rather than RESTful) if the bug is ever ironed out:
                It can't find an implementation of ContainerProvider.getWebSocketContainer();
                [Info] Error: java.lang.RuntimeException: Could not find an implementation class.
                [INFO] Error: java.lang.NullPointerException
            initWebsocketListener();
            */
            initCollector();
            getRecentPrices();
        } catch (Exception e){
            System.out.println("[INFO] Error: " + e);
        }
    }
    
    private void initWebsocketListener(){
        /*try {
            wsListener = new WebsocketListener(new URI("wss://ws-feed.gdax.com"));
        
            wsListener.addMessageHandler(new WebsocketListener.MessageHandler() {
                @Override
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            wsListener.sendMessage("{\"type\": \"subscribe\", \"product_ids\": [\"BCH-USD\", \"BTC-USD\", \"ETH-USD\", \"LTC-USD\"], \"channels\": [\"level2\", \"heartbeat\"] }");

            // wait 5 seconds for messages from websocket
            Thread.sleep(5000);

        } catch (Exception e) {
            System.out.println("[INFO] Error: " + e);
        }*/
    }
    
    private void setUpCurrencies(){
        /*
        GET FROM API
        */
        Currency newCurrency = new Currency("BCH", "Bitcoin Cash", apiController.getBCH_TRADES());
        currencies[0] = newCurrency;
        newCurrency = new Currency("BTC", "Bitcoin", apiController.getBTC_TRADES());
        currencies[1] = newCurrency;
        newCurrency = new Currency("ETH", "Ethereum", apiController.getETH_TRADES());
        currencies[2] = newCurrency;
        newCurrency = new Currency("LTC", "Litecoin", apiController.getLTC_TRADES());
        currencies[3] = newCurrency;
    }
    
    private void getRecentPrices(){  
        Runnable recentPriceCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                if (0 < currentSecond && currentSecond < 59 ) {
                    int bchSize = currencies[0].getNumberOfRatesCollected();
                    int btcSize = currencies[1].getNumberOfRatesCollected();
                    int ethSize = currencies[2].getNumberOfRatesCollected();
                    int ltcSize = currencies[3].getNumberOfRatesCollected();
                    int readingsTaken = 0;
                    int readings = startGOFAI;

                    if (readings <= bchSize && readings <= btcSize && readings <= ethSize && readings <=  ltcSize) {
                        if (!currencies[0].isCalculatingGOFAI()){
                            for (Currency currency : currencies){
                                currency.mergePrices();
                            }
                            System.out.println("[INFO] First price list merge complete. Can now calculate GOFAI predictions.");
                        }
                        readings = startNN;
                        if (readings <= bchSize && readings <= btcSize && readings <= ethSize && readings <=  ltcSize) {
                            for (Currency currency : currencies){
                                currency.mergePrices();
                            }
                            System.out.println("[INFO] Second price list merge complete. Can now calculate Neural Network predictions.");
                            System.out.println("[INFO] Shutting down historic price collection thread.");
                            historic.shutdownNow();
                        }
                    }
                            
                    while (readingsTaken < 4){
                        for (Currency currency : currencies){
                            if (currency.getNumberOfRatesCollected() < readings){
                                currency.calculateRecentAverages(apiController, parser);
                                readingsTaken++;
                            }
                            if (readingsTaken == 4) break;
                        }
                        if (readingsTaken == 0) {
                            System.out.println("[INFO] Failed to take a reading - collected enough rates for all.");
                            break;
                        }
                    }
                    
                    /*if (readings <= bchSize) {
                        if (!currencies[0].isCalculatingGOFAI()){
                            currencies[0].mergePrices();
                            System.out.println("[INFO] First price list merge complete. Can now calculate GOFAI predictions.");
                        }
                        readings = startNN;
                        if (readings <= bchSize) {
                            currencies[0].mergePrices();
                            System.out.println("[INFO] Second price list merge complete. Can now calculate Neural Network predictions.");
                            System.out.println("[INFO] Shutting down historic price collection thread.");
                            historic.shutdownNow();
                        }
                    }

                    while (readingsTaken < 4){
                        if (currencies[0].getNumberOfRatesCollected() < readings){
                            currencies[0].calculateRecentAverages(apiController, parser);
                            readingsTaken++;
                        } else {
                            readingsTaken++;
                        }
                        if (readingsTaken == 0) {
                            System.out.println("[INFO] Failed to take a reading - collected enough rates for all.");
                            break;
                        }
                    }
                    */
                }
            }
        };
        historic.scheduleWithFixedDelay(recentPriceCollection, 0, 1, TimeUnit.SECONDS);
    }

    private void initCollector(){
        Runnable automatedCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                currentSecond++;
                if (currentSecond == 60) {
                    currentSecond = 0;
                    get(Helpers.startOfMinute(LocalDateTime.now()));
                    for (Currency currency : currencies){
                        System.out.println(currency.getId() + " " + currency.getName() + " " + currency.getRate());
                    }
                }
            }
        };
        currentSecond = LocalDateTime.now().getSecond();
        current.scheduleAtFixedRate(automatedCollection, 1, 1, TimeUnit.SECONDS);
    }

    public void get(LocalDateTime postTime){
        System.out.println("[INFO] Fetching resource from: " + apiController.getGDAX_ENDPOINT() + "/products/.../trades");
        try {
            for (Currency currency : currencies){
                currency.getPrice(apiController, parser);
            }
        } catch (Exception e) {
            System.out.println("[INFO] Error: " + e);
        }
    }
}
