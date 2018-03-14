package com.jkellaway.cryptocurrencyvaluepredictorgui;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.IObserver;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.StringHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.valuepredictor.CryptocurrencyValuePredictor;
import com.jkellaway.guihelpers.TableConfigurer;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jkell
 */
public class JFCryptocurrencyValuePredictor extends javax.swing.JFrame implements IObserver {
    private CryptocurrencyValuePredictor cryptocurrencyValuePredictor;
            
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
            String col[] = {"Currency", "Value ($)", "Change (%)", "GOFAI Prediction (%)", "Neural Network Prediction (%)"};
            DefaultTableModel tableModel = new DefaultTableModel(col, 0);
            JTable table = new JTable(tableModel);

            for (Currency currency : cryptocurrencyValuePredictor.getCurrencies()){
                String name = currency.getName();
                ExchangeRate rate = currency.getRate();
                Double value = rate.getValue();
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
            LocalDateTime startTime = cryptocurrencyValuePredictor.getPriceCollector().getFirstRelevantRate();
            int maxTrades = Globals.READINGSREQUIRED - Globals.NUMBEROFPREDICTIONS;
            double best = cryptocurrencyValuePredictor.getBest().getWallet().getUSDValue(cryptocurrencyValuePredictor.getCurrencies());
            double worst = cryptocurrencyValuePredictor.getWorst().getWallet().getUSDValue(cryptocurrencyValuePredictor.getCurrencies());
            this.jlblFirstTradeTime.setText(LocalDateTimeHelper.toString(startTime));
            this.jlblMaxTrades.setText(String.valueOf(maxTrades));
            this.jlblBestPerformance.setText(StringHelper.doubleToCurrencyString(best));
            this.jlblWorstPerformance.setText(StringHelper.doubleToCurrencyString(worst));
            
            String col[] = {"Strategy", "Final Value ($)"};
            DefaultTableModel tableModel = new DefaultTableModel(col, 0);
            JTable table = new JTable(tableModel);

            for (Currency currency : cryptocurrencyValuePredictor.getCurrencies()){
                String name = currency.getName();
                ExchangeRate rate = currency.getRate();
                Double value = rate.getValue();
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHome = new javax.swing.JPanel();
        jspValues = new javax.swing.JScrollPane();
        jtblValues = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpnlTrading = new javax.swing.JPanel();
        jpnlInvestmentProtection = new javax.swing.JPanel();
        jpnlGOFAI = new javax.swing.JPanel();
        jspGOFAI = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
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
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jspValues.setViewportView(jtblValues);

        javax.swing.GroupLayout jpnlTradingLayout = new javax.swing.GroupLayout(jpnlTrading);
        jpnlTrading.setLayout(jpnlTradingLayout);
        jpnlTradingLayout.setHorizontalGroup(
            jpnlTradingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 795, Short.MAX_VALUE)
        );
        jpnlTradingLayout.setVerticalGroup(
            jpnlTradingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 466, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Trading", jpnlTrading);

        javax.swing.GroupLayout jpnlInvestmentProtectionLayout = new javax.swing.GroupLayout(jpnlInvestmentProtection);
        jpnlInvestmentProtection.setLayout(jpnlInvestmentProtectionLayout);
        jpnlInvestmentProtectionLayout.setHorizontalGroup(
            jpnlInvestmentProtectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 795, Short.MAX_VALUE)
        );
        jpnlInvestmentProtectionLayout.setVerticalGroup(
            jpnlInvestmentProtectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 466, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Investment Protection", jpnlInvestmentProtection);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Strategy", "Final Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jspGOFAI.setViewportView(jTable1);

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
                .addComponent(jspGOFAI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnlGOFAILayout.createSequentialGroup()
                        .addComponent(jlblFirst)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlblFirstTradeTime))
                    .addGroup(jpnlGOFAILayout.createSequentialGroup()
                        .addComponent(jlblMax)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlblMaxTrades))
                    .addGroup(jpnlGOFAILayout.createSequentialGroup()
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlblBest)
                            .addComponent(jlblWorst))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlblBestPerformance, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jlblWorstPerformance, javax.swing.GroupLayout.Alignment.TRAILING))))
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
                            .addComponent(jlblMaxTrades))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpnlGOFAILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnlGOFAILayout.createSequentialGroup()
                                .addComponent(jlblBest)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlblWorst))
                            .addGroup(jpnlGOFAILayout.createSequentialGroup()
                                .addComponent(jlblBestPerformance)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlblWorstPerformance))))
                    .addComponent(jspGOFAI, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78))
        );

        jTabbedPane1.addTab("Predictions", jpnlGOFAI);

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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
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
    private javax.swing.JPanel jpnlInvestmentProtection;
    private javax.swing.JPanel jpnlTrading;
    private javax.swing.JScrollPane jspGOFAI;
    private javax.swing.JScrollPane jspValues;
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
    private javax.swing.JPanel pnlHome;
    // End of variables declaration//GEN-END:variables
}
