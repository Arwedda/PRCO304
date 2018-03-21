package com.jkellaway.cryptocurrencyvaluepredictorgui;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.ArrayHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.IObserver;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.StringHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Wallet;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.Trader;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.valuepredictor.CryptocurrencyValuePredictor;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.TableConfigurer;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.TextBoxHelper;
import java.awt.Desktop;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jkell
 */
public class JFCryptocurrencyValuePredictor extends javax.swing.JFrame implements IObserver {
    private static CryptocurrencyValuePredictor cryptocurrencyValuePredictor;
            
    /**
     * Creates new form CryptocurrencyValuePredictor
     */
    public JFCryptocurrencyValuePredictor() {
        initComponents();
        initialise();
    }
    
    private void initialise() {
        cryptocurrencyValuePredictor = CryptocurrencyValuePredictor.getInstance();
        cryptocurrencyValuePredictor.registerObserver(this);
    }
    
    @Override
    public void update() {
        loadCurrencies();
        loadPredictions();
    }
    
    private void loadCurrencies() {
        try {
            String col[] = {"Currency", "Value (USD)", "Change (%)", "GOFAI Prediction (%)", "Neural Network Prediction (%)"};
            DefaultTableModel tableModel = new DefaultTableModel(col, 0);
            JTable table = new JTable(tableModel);

            for (Currency currency : cryptocurrencyValuePredictor.getCurrencies()){
                String name = currency.getName();
                ExchangeRate rate = currency.getRate();
                String value = StringHelper.doubleToCurrencyString(rate.getValue());
                Double change = rate.getGrowth();
                Double GOFAI = rate.getGofaiNextGrowth();
                Double neuralNetwork = rate.getNeuralNetworkNextGrowth();
                Object[] row = {name, value, change, GOFAI, neuralNetwork};
                tableModel.addRow(row);
            }
            this.jtblValues.setModel(table.getModel());
            TableConfigurer.configureTable(this.jtblValues);
        } catch (Exception e) {
            System.out.println("[INFO] Error " + e);
        }
    }
    
    private void loadPredictions() {
        try {
            Currency[] currencies = cryptocurrencyValuePredictor.getCurrencies();
            Trader[] gofaiTraders = ArrayHelper.merge(cryptocurrencyValuePredictor.getGOFAITradersHold(), cryptocurrencyValuePredictor.getGOFAITradersUSD());
            LocalDateTime startTime = cryptocurrencyValuePredictor.getPriceCollector().getFirstRelevantRate();
            int maxTrades = (int) ChronoUnit.MINUTES.between(startTime, LocalDateTime.now()) - Globals.NUMBEROFPREDICTIONS;
            double best = cryptocurrencyValuePredictor.getBest().getWallet().getUSDValue(currencies);
            double worst = cryptocurrencyValuePredictor.getWorst().getWallet().getUSDValue(currencies);
            this.jlblFirstTradeTime.setText(LocalDateTimeHelper.toString(startTime));
            this.jlblMaxTrades.setText(String.valueOf(maxTrades));
            this.jlblBestPerformance.setText(StringHelper.doubleToCurrencyString(best));
            this.jlblWorstPerformance.setText(StringHelper.doubleToCurrencyString(worst));
            
            String col[] = {"Strategy", "Starting", "Final Holding", "Final Value (USD)"};
            DefaultTableModel tableModel = new DefaultTableModel(col, 0);
            JTable table = new JTable(tableModel);

            for (Trader trader : cryptocurrencyValuePredictor.getHolders()){
                Wallet wallet = trader.getWallet();
                String strategy = "Hold " + trader.getHoldMode();
                String finalValue = StringHelper.doubleToCurrencyString(wallet.getUSDValue(currencies));
                String starting;
                String finalHolding = wallet.getValue() + " " + wallet.getHoldingID();
                switch (wallet.getHoldingID()) {
                    case "BCH":
                        starting = wallet.getStartingValues()[0] + " BCH";
                        break;
                    case "BTC":
                        starting = wallet.getStartingValues()[1] + " BTC";
                        break;
                    case "ETH":
                        starting = wallet.getStartingValues()[2] + " ETH";
                        break;
                    case "LTC":
                        starting = wallet.getStartingValues()[3] + " LTC";
                        break;
                    default:
                        starting = "100.00 USD";
                        break;
                }
                
                Object[] row = {strategy, starting, finalHolding, finalValue};
                tableModel.addRow(row);
            }
            
            for (int i = 0; i < gofaiTraders.length; i++) {
                Wallet wallet = gofaiTraders[i].getWallet();
                String strategy = "GOFAI " + ((i % 20) + 1) + " Trades & Hold " + gofaiTraders[i].getHoldMode();
                String finalValue = StringHelper.doubleToCurrencyString(wallet.getUSDValue(currencies));
                String starting;
                String finalHolding = wallet.getValue() + " " + wallet.getHoldingID();
                switch (wallet.getHoldingID()) {
                    case "BCH":
                        starting = wallet.getStartingValues()[0] + " BCH";
                        break;
                    case "BTC":
                        starting = wallet.getStartingValues()[1] + " BTC";
                        break;
                    case "ETH":
                        starting = wallet.getStartingValues()[2] + " ETH";
                        break;
                    case "LTC":
                        starting = wallet.getStartingValues()[3] + " LTC";
                        break;
                    default:
                        starting = "100.00 USD";
                        break;
                }
                
                Object[] row = {strategy, starting, finalHolding, finalValue};
                tableModel.addRow(row);
            }
            
            this.jtblPredictions.setModel(table.getModel());
            TableConfigurer.configureTable(this.jtblPredictions);
        } catch (Exception e) {
            System.out.println("[INFO] Error " + e);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btngrpTradeMode = new javax.swing.ButtonGroup();
        btngrpHoldMode = new javax.swing.ButtonGroup();
        pnlHome = new javax.swing.JPanel();
        jspValues = new javax.swing.JScrollPane();
        jtblValues = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpnlTrading = new javax.swing.JPanel();
        jpnlTradeMode = new javax.swing.JPanel();
        rdbtnOff = new javax.swing.JRadioButton();
        rdbtnGOFAI = new javax.swing.JRadioButton();
        rdbtnNeuralNetwork = new javax.swing.JRadioButton();
        lblTradeAmount = new javax.swing.JLabel();
        txtStartAmount = new javax.swing.JTextField();
        jpnlHoldMode = new javax.swing.JPanel();
        rdbtnUSD = new javax.swing.JRadioButton();
        rdbtnCrypto = new javax.swing.JRadioButton();
        tglbtnTrade = new javax.swing.JToggleButton();
        jpnlInvestmentProtection = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jpnlGOFAI = new javax.swing.JPanel();
        jspPredictions = new javax.swing.JScrollPane();
        jtblPredictions = new javax.swing.JTable();
        jlblFirst = new javax.swing.JLabel();
        jlblFirstTradeTime = new javax.swing.JLabel();
        jlblMax = new javax.swing.JLabel();
        jlblMaxTrades = new javax.swing.JLabel();
        jlblBest = new javax.swing.JLabel();
        jlblWorst = new javax.swing.JLabel();
        jlblWorstPerformance = new javax.swing.JLabel();
        jlblBestPerformance = new javax.swing.JLabel();
        jpnlAbout = new javax.swing.JPanel();
        lblDeveloper = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblLiability = new javax.swing.JLabel();
        lblLiability2 = new javax.swing.JLabel();
        lblCopyright = new javax.swing.JLabel();
        lblDistribution = new javax.swing.JLabel();
        lblContactDetails = new javax.swing.JLabel();
        lblContactDetails2 = new javax.swing.JLabel();
        lblContactDetails3 = new javax.swing.JLabel();
        lblContactDetails4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cryptocurrency Value Predictor");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setSize(new java.awt.Dimension(800, 600));

        pnlHome.setMaximumSize(new java.awt.Dimension(800, 600));
        pnlHome.setMinimumSize(new java.awt.Dimension(800, 600));
        pnlHome.setPreferredSize(new java.awt.Dimension(800, 600));

        jtblValues.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Currency", "Value ($)", "Change (%)", "GOFAI Prediction (%)", "Neural Network Prediction (%)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jspValues.setViewportView(jtblValues);

        jpnlTradeMode.setBorder(javax.swing.BorderFactory.createTitledBorder("Trade Mode"));

        btngrpTradeMode.add(rdbtnOff);
        rdbtnOff.setSelected(true);
        rdbtnOff.setText("Off");

        btngrpTradeMode.add(rdbtnGOFAI);
        rdbtnGOFAI.setText("GOFAI");

        rdbtnNeuralNetwork.setText("Neural Network");
        rdbtnNeuralNetwork.setEnabled(false);

        javax.swing.GroupLayout jpnlTradeModeLayout = new javax.swing.GroupLayout(jpnlTradeMode);
        jpnlTradeMode.setLayout(jpnlTradeModeLayout);
        jpnlTradeModeLayout.setHorizontalGroup(
            jpnlTradeModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlTradeModeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdbtnOff)
                .addGap(18, 18, 18)
                .addComponent(rdbtnGOFAI)
                .addGap(18, 18, 18)
                .addComponent(rdbtnNeuralNetwork)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpnlTradeModeLayout.setVerticalGroup(
            jpnlTradeModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlTradeModeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlTradeModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbtnOff)
                    .addComponent(rdbtnGOFAI)
                    .addComponent(rdbtnNeuralNetwork))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblTradeAmount.setText("Start Amount:");

        txtStartAmount.setText("0.00");
        txtStartAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStartAmountKeyTyped(evt);
            }
        });

        jpnlHoldMode.setBorder(javax.swing.BorderFactory.createTitledBorder("Hold Mode"));

        btngrpHoldMode.add(rdbtnUSD);
        rdbtnUSD.setSelected(true);
        rdbtnUSD.setText("USD");

        btngrpHoldMode.add(rdbtnCrypto);
        rdbtnCrypto.setText("Cryptocurrency");

        javax.swing.GroupLayout jpnlHoldModeLayout = new javax.swing.GroupLayout(jpnlHoldMode);
        jpnlHoldMode.setLayout(jpnlHoldModeLayout);
        jpnlHoldModeLayout.setHorizontalGroup(
            jpnlHoldModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlHoldModeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdbtnUSD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rdbtnCrypto)
                .addContainerGap())
        );
        jpnlHoldModeLayout.setVerticalGroup(
            jpnlHoldModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlHoldModeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlHoldModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbtnUSD)
                    .addComponent(rdbtnCrypto))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tglbtnTrade.setText("Start Trading");
        tglbtnTrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnTradeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnlTradingLayout = new javax.swing.GroupLayout(jpnlTrading);
        jpnlTrading.setLayout(jpnlTradingLayout);
        jpnlTradingLayout.setHorizontalGroup(
            jpnlTradingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlTradingLayout.createSequentialGroup()
                .addGroup(jpnlTradingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnlTradingLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpnlTradingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jpnlHoldMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpnlTradeMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpnlTradingLayout.createSequentialGroup()
                                .addComponent(lblTradeAmount)
                                .addGap(18, 18, 18)
                                .addComponent(txtStartAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jpnlTradingLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(tglbtnTrade)))
                .addContainerGap(528, Short.MAX_VALUE))
        );
        jpnlTradingLayout.setVerticalGroup(
            jpnlTradingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlTradingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnlTradeMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpnlTradingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTradeAmount)
                    .addComponent(txtStartAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jpnlHoldMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tglbtnTrade)
                .addContainerGap(238, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Trading", jpnlTrading);

        jLabel2.setText("Coming Soon...");

        javax.swing.GroupLayout jpnlInvestmentProtectionLayout = new javax.swing.GroupLayout(jpnlInvestmentProtection);
        jpnlInvestmentProtection.setLayout(jpnlInvestmentProtectionLayout);
        jpnlInvestmentProtectionLayout.setHorizontalGroup(
            jpnlInvestmentProtectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlInvestmentProtectionLayout.createSequentialGroup()
                .addGap(326, 326, 326)
                .addComponent(jLabel2)
                .addContainerGap(395, Short.MAX_VALUE))
        );
        jpnlInvestmentProtectionLayout.setVerticalGroup(
            jpnlInvestmentProtectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlInvestmentProtectionLayout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addComponent(jLabel2)
                .addContainerGap(252, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Investment Protection", jpnlInvestmentProtection);

        jtblPredictions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Strategy", "Starting", "Final Holding", "Final Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jspPredictions.setViewportView(jtblPredictions);

        jlblFirst.setText("First Trade Time:");

        jlblFirstTradeTime.setText("jlblFirstTradeTime");

        jlblMax.setText("Maximum Trades:");

        jlblMaxTrades.setText("jlblMaxTrades");

        jlblBest.setText("Best Performance:");

        jlblWorst.setText("Worst Performance:");

        jlblWorstPerformance.setText("jlblWorstPerformance");

        jlblBestPerformance.setText("jlblBestPerformance");

        javax.swing.GroupLayout jpnlGOFAILayout = new javax.swing.GroupLayout(jpnlGOFAI);
        jpnlGOFAI.setLayout(jpnlGOFAILayout);
        jpnlGOFAILayout.setHorizontalGroup(
            jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlGOFAILayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnlGOFAILayout.createSequentialGroup()
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlblFirst)
                            .addComponent(jlblMax))
                        .addGap(18, 18, 18)
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlblFirstTradeTime)
                            .addComponent(jlblMaxTrades))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 305, Short.MAX_VALUE)
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlblBest)
                            .addComponent(jlblWorst))
                        .addGap(84, 84, 84)
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlblBestPerformance, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jlblWorstPerformance, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jspPredictions))
                .addContainerGap())
        );
        jpnlGOFAILayout.setVerticalGroup(
            jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlGOFAILayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnlGOFAILayout.createSequentialGroup()
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblFirst)
                            .addComponent(jlblFirstTradeTime))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblMax)
                            .addComponent(jlblMaxTrades)))
                    .addGroup(jpnlGOFAILayout.createSequentialGroup()
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblBestPerformance)
                            .addComponent(jlblBest))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblWorstPerformance)
                            .addComponent(jlblWorst))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspPredictions, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );

        jTabbedPane1.addTab("Benchmarking", jpnlGOFAI);

        lblDeveloper.setFont(new java.awt.Font("SimSun", 0, 11)); // NOI18N
        lblDeveloper.setText("Developer: Joseph Kellaway");

        lblTitle.setFont(new java.awt.Font("SimSun", 1, 15)); // NOI18N
        lblTitle.setText("Cryptocurrency Value Predictor");
        lblTitle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblTitle.setFocusable(false);
        lblTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblLiability.setFont(new java.awt.Font("SimSun", 0, 11)); // NOI18N
        lblLiability.setText("Users of this program accept that they are doing so at their own risk. The developer and any distributor of this software will");

        lblLiability2.setFont(new java.awt.Font("SimSun", 0, 11)); // NOI18N
        lblLiability2.setText("accept neither responsibility, nor accountability for losses or damages experienced during use.");

        lblCopyright.setFont(new java.awt.Font("SimSun", 0, 11)); // NOI18N
        lblCopyright.setText("Copyright 2018 Joseph Kellaway. All rights reserved.");

        lblDistribution.setFont(new java.awt.Font("SimSun", 0, 11)); // NOI18N
        lblDistribution.setText("Before using, modifying or distributing this software written authorisation from the developer is required.");

        lblContactDetails.setFont(new java.awt.Font("SimSun", 0, 11)); // NOI18N
        lblContactDetails.setText("Contact Details:");

        lblContactDetails2.setFont(new java.awt.Font("SimSun", 0, 11)); // NOI18N
        lblContactDetails2.setForeground(new java.awt.Color(0, 0, 255));
        lblContactDetails2.setText("j.kellaway88@gmail.com");
        lblContactDetails2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblContactDetails2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lblContactDetails2MouseDragged(evt);
            }
        });

        lblContactDetails3.setFont(new java.awt.Font("SimSun", 0, 11)); // NOI18N
        lblContactDetails3.setForeground(new java.awt.Color(0, 0, 255));
        lblContactDetails3.setText("https://github.com/Arwedda");
        lblContactDetails3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblContactDetails3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblContactDetails3MouseClicked(evt);
            }
        });

        lblContactDetails4.setFont(new java.awt.Font("SimSun", 0, 11)); // NOI18N
        lblContactDetails4.setForeground(new java.awt.Color(0, 0, 255));
        lblContactDetails4.setText("https://www.linkedin.com/in/joseph-kellaway-604a22128/");
        lblContactDetails4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblContactDetails4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblContactDetails4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpnlAboutLayout = new javax.swing.GroupLayout(jpnlAbout);
        jpnlAbout.setLayout(jpnlAboutLayout);
        jpnlAboutLayout.setHorizontalGroup(
            jpnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlAboutLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnlAboutLayout.createSequentialGroup()
                        .addGroup(jpnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDeveloper)
                            .addComponent(lblLiability)
                            .addComponent(lblDistribution)
                            .addComponent(lblLiability2))
                        .addContainerGap(29, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlAboutLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblTitle)
                        .addGap(262, 262, 262))))
            .addGroup(jpnlAboutLayout.createSequentialGroup()
                .addGroup(jpnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnlAboutLayout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(lblCopyright))
                    .addGroup(jpnlAboutLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblContactDetails)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblContactDetails2))
                    .addGroup(jpnlAboutLayout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addGroup(jpnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblContactDetails4)
                            .addComponent(lblContactDetails3))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jpnlAboutLayout.setVerticalGroup(
            jpnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlAboutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addGap(9, 9, 9)
                .addComponent(lblDeveloper)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblLiability)
                .addGap(1, 1, 1)
                .addComponent(lblLiability2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDistribution)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContactDetails)
                    .addComponent(lblContactDetails2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblContactDetails3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblContactDetails4)
                .addGap(275, 275, 275)
                .addComponent(lblCopyright))
        );

        jTabbedPane1.addTab("About", jpnlAbout);

        javax.swing.GroupLayout pnlHomeLayout = new javax.swing.GroupLayout(pnlHome);
        pnlHome.setLayout(pnlHomeLayout);
        pnlHomeLayout.setHorizontalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jspValues)
            .addComponent(jTabbedPane1)
        );
        pnlHomeLayout.setVerticalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addComponent(jspValues, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblContactDetails3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblContactDetails3MouseClicked
        openWebpage(lblContactDetails3.getText());
    }//GEN-LAST:event_lblContactDetails3MouseClicked

    private void lblContactDetails4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblContactDetails4MouseClicked
        openWebpage(lblContactDetails4.getText());
    }//GEN-LAST:event_lblContactDetails4MouseClicked

    private void lblContactDetails2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblContactDetails2MouseDragged
        writeEmail(lblContactDetails2.getText());
    }//GEN-LAST:event_lblContactDetails2MouseDragged

    private void tglbtnTradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnTradeActionPerformed
        if (rdbtnOff.isSelected()){
            JOptionPane.showMessageDialog(this, "Please select a trade mode.", "Cryptocurrency Value Predictor", JOptionPane.WARNING_MESSAGE);
            tglbtnTrade.setSelected(false);
        } else {
            try {
                Double tradeAmount = Double.parseDouble(txtStartAmount.getText());
                if (tradeAmount < Globals.MINIMUM_INVESTMENT) {
                    JOptionPane.showMessageDialog(this, "Starting trade value too low.", "Invalid Request!", JOptionPane.WARNING_MESSAGE);
                    tglbtnTrade.setSelected(false);
                } else {
                    if (tglbtnTrade.isSelected()){
                        boolean gofai = rdbtnGOFAI.isSelected();
                        boolean holdUSD = rdbtnUSD.isSelected();
                        cryptocurrencyValuePredictor.startTrading(gofai, tradeAmount, holdUSD);
                        toggleEnabled();
                        tglbtnTrade.setText("Stop Trading");
                        JOptionPane.showMessageDialog(this, "Trading.", "Cryptocurrency Value Predictor", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        cryptocurrencyValuePredictor.stopTrading();
                        toggleEnabled();
                        tglbtnTrade.setText("Start Trading");
                        JOptionPane.showMessageDialog(this, "Not trading.", "Cryptocurrency Value Predictor", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid starting trade value", "Invalid Request!", JOptionPane.WARNING_MESSAGE);
                tglbtnTrade.setSelected(false);
            }
        }
    }//GEN-LAST:event_tglbtnTradeActionPerformed

    private void txtStartAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStartAmountKeyTyped
        TextBoxHelper.forceNumeric(evt);
    }//GEN-LAST:event_txtStartAmountKeyTyped

    private void toggleEnabled() {
        rdbtnOff.setEnabled(!rdbtnOff.isEnabled());
        rdbtnGOFAI.setEnabled(!rdbtnGOFAI.isEnabled());
        txtStartAmount.setEnabled(!txtStartAmount.isEnabled());
        rdbtnUSD.setEnabled(!rdbtnUSD.isEnabled());
        rdbtnCrypto.setEnabled(!rdbtnCrypto.isEnabled());
    }
    
    private void openWebpage(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                System.out.println("[INFO] Error: " + e);
            }
        }
    }
    
    private void writeEmail(String address) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().mail( new URI( "mailto:" + address));
            } catch (Exception e) {
                System.out.println("[INFO] Error: " + e);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CryptocurrencyValuePredictor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CryptocurrencyValuePredictor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CryptocurrencyValuePredictor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CryptocurrencyValuePredictor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFCryptocurrencyValuePredictor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btngrpHoldMode;
    private javax.swing.ButtonGroup btngrpTradeMode;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jlblBest;
    private javax.swing.JLabel jlblBestPerformance;
    private javax.swing.JLabel jlblFirst;
    private javax.swing.JLabel jlblFirstTradeTime;
    private javax.swing.JLabel jlblMax;
    private javax.swing.JLabel jlblMaxTrades;
    private javax.swing.JLabel jlblWorst;
    private javax.swing.JLabel jlblWorstPerformance;
    private javax.swing.JPanel jpnlAbout;
    private javax.swing.JPanel jpnlGOFAI;
    private javax.swing.JPanel jpnlHoldMode;
    private javax.swing.JPanel jpnlInvestmentProtection;
    private javax.swing.JPanel jpnlTradeMode;
    private javax.swing.JPanel jpnlTrading;
    private javax.swing.JScrollPane jspPredictions;
    private javax.swing.JScrollPane jspValues;
    private javax.swing.JTable jtblPredictions;
    private javax.swing.JTable jtblValues;
    private javax.swing.JLabel lblContactDetails;
    private javax.swing.JLabel lblContactDetails2;
    private javax.swing.JLabel lblContactDetails3;
    private javax.swing.JLabel lblContactDetails4;
    private javax.swing.JLabel lblCopyright;
    private javax.swing.JLabel lblDeveloper;
    private javax.swing.JLabel lblDistribution;
    private javax.swing.JLabel lblLiability;
    private javax.swing.JLabel lblLiability2;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTradeAmount;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JRadioButton rdbtnCrypto;
    private javax.swing.JRadioButton rdbtnGOFAI;
    private javax.swing.JRadioButton rdbtnNeuralNetwork;
    private javax.swing.JRadioButton rdbtnOff;
    private javax.swing.JRadioButton rdbtnUSD;
    private javax.swing.JToggleButton tglbtnTrade;
    private javax.swing.JTextField txtStartAmount;
    // End of variables declaration//GEN-END:variables
}
