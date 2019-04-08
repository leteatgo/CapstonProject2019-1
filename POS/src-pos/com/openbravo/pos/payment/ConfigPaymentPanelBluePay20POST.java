//    dPOS  - Dongwun Point Of Sale
//    Copyright (C) 2008-2014 Openbravo, uniCenta
//    Addition of Blue Pay - Walter Wojick Feb 2014
//    http://www.unicenta.net/unicentaopos
//
//    This file is part of dPOS
//
//    dPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   dPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with dPOS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.payment;

import com.openbravo.pos.forms.StartPOS;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import javax.swing.JPanel;


/**
 *
 * @author  Mikel Irurita
 */
public class ConfigPaymentPanelBluePay20POST extends javax.swing.JPanel implements PaymentConfiguration {
    private static final long serialVersionUID = -2941609727268873594L;

    /** Creates new form ConfigPaymentPanelBluePay20POST */
    public ConfigPaymentPanelBluePay20POST() {
        initComponents();
    }
    
    /**
     *
     * @return
     */
    @Override
    public JPanel getComponent() {
        return this;
    }
    
    /**
     *
     * @param config
     */
    @Override
        public void loadProperties(AppConfig config) {
        String sAccountID = config.getProperty("payment.BluePay.accountID");
        String sSecretKey = config.getProperty("payment.BluePay.secretKey");
        String sURL = config.getProperty("payment.BluePay.URL");
        
//        System.out.println(sURL);
         
        if (sAccountID!=null && sSecretKey!=null && sURL!=null && sURL.startsWith("https://")) {
            jtxtURL.setText(config.getProperty("payment.BluePay.URL"));
            jtxtAccountID.setText(config.getProperty("payment.BluePay.accountID"));
            jtxtSecretKey.setText(config.getProperty("payment.BluePay.secretKey"));
        }
    }

    /**
     *
     * @param config
     */
    public void saveProperties(AppConfig config) {
        config.setProperty("payment.BluePay.accountID", comboValue(jtxtAccountID.getPassword()));
        config.setProperty("payment.BluePay.secretKey", comboValue(jtxtSecretKey.getText()));
        config.setProperty("payment.BluePay.URL", comboValue(jtxtURL.getText()));
    }
    
    private String comboValue(Object value) {
        return value == null ? "" : value.toString();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtxtURL = new javax.swing.JTextField();
        jtxtAccountID = new javax.swing.JPasswordField();
        jtxtSecretKey = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(400, 116));

        jLabel1.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jLabel1.setText(AppLocal.getIntString("label.storename")); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 30));

        jLabel3.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jLabel3.setText(AppLocal.getIntString("label.certificatepwd")); // NOI18N
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 30));

        jLabel4.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jLabel4.setText(AppLocal.getIntString("label.certificatepath")); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(100, 30));

        jtxtURL.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jtxtURL.setPreferredSize(new java.awt.Dimension(200, 30));

        jtxtAccountID.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jtxtAccountID.setPreferredSize(new java.awt.Dimension(200, 30));

        jtxtSecretKey.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jtxtSecretKey.setPreferredSize(new java.awt.Dimension(300, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtxtSecretKey, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtAccountID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(180, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtAccountID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtSecretKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPasswordField jtxtAccountID;
    private javax.swing.JTextField jtxtSecretKey;
    private javax.swing.JTextField jtxtURL;
    // End of variables declaration//GEN-END:variables

}
