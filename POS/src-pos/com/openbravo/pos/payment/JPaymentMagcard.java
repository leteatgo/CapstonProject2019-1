//    dPOS  - Dongwun Point Of Sale
//    Copyright (c) 2009-2017 Dongwun & uniCenta & previous Openbravo POS works
//    http://www.dongwun.com
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
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 *
 * @author  adrianromero
 */
public class JPaymentMagcard extends javax.swing.JPanel implements JPaymentInterface {
    private static final long serialVersionUID = -5047612582593091639L;
    
    private PaymentPanel m_cardpanel;
    private final PaymentGateway m_paymentgateway;
    private final JPaymentNotifier m_notifier;
    private String transaction;
    
    /** Creates new form JPaymentMagcard
     * @param app
     * @param notifier */
    public JPaymentMagcard(AppView app, JPaymentNotifier notifier) {
        
        initComponents();   
        
        m_notifier = notifier;
        
        m_paymentgateway = PaymentGatewayFac.getPaymentGateway(app.getProperties());
        
        if (m_paymentgateway == null) {
            jlblMessage.setText(AppLocal.getIntString("message.nopaymentgateway"));            
        } else {           
            // Se van a poder efectuar pagos con tarjeta
            m_cardpanel = PaymentPanelFac.getPaymentPanel(app.getProperties().getProperty("payment.magcardreader"), notifier);
            add(m_cardpanel.getComponent(), BorderLayout.CENTER);
            jlblMessage.setText(null);
            // jlblMessage.setText(AppLocal.getIntString("message.nocardreader"));
        }
    }
    
    /**
     *
     * @param customerext
     * @param dTotal
     * @param transID
     */
    @Override
    public void activate(CustomerInfoExt customerext, double dTotal, String transID) {   
        this.transaction = transID;

        if (m_cardpanel == null) {
            jlblMessage.setText(AppLocal.getIntString("message.nopaymentgateway"));  
            m_notifier.setStatus(false, false);
        } else {
            jlblMessage.setText(null);
            m_cardpanel.activate(transaction, dTotal); 
            // The cardpanel sets the status
        }
    }

    /**
     *
     * @return
     */
    @Override
    public PaymentInfo executePayment() {
        
        jlblMessage.setText(null);

        PaymentInfoMagcard payinfo = m_cardpanel.getPaymentInfoMagcard();

        m_paymentgateway.execute(payinfo);
        if (payinfo.isPaymentOK()) {
            return payinfo;
        } else {
            jlblMessage.setText(payinfo.getMessage());
            return null;
        }
    }  

    /**
     *
     * @return
     */
    @Override
    public Component getComponent() {
        return this;
    }
    
    /**
     *
     * @param transid
     */
    public void setTransaction(String transid){
        transaction = transid;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jlblMessage = new javax.swing.JTextArea();

        setMinimumSize(new java.awt.Dimension(300, 40));
        setPreferredSize(new java.awt.Dimension(300, 40));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jPanel1.setMinimumSize(new java.awt.Dimension(290, 35));
        jPanel1.setPreferredSize(new java.awt.Dimension(290, 35));

        jlblMessage.setBackground(new java.awt.Color(224, 223, 227));
        jlblMessage.setFont(StartPOS.getgblFont().deriveFont( 1, 12)); // NOI18N
        jlblMessage.setLineWrap(true);
        jlblMessage.setWrapStyleWord(true);
        jlblMessage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jlblMessage.setFocusable(false);
        jlblMessage.setPreferredSize(new java.awt.Dimension(290, 37));
        jlblMessage.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jlblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextArea jlblMessage;
    // End of variables declaration//GEN-END:variables
    
}