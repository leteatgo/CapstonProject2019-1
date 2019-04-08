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
package com.openbravo.pos.reports;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.StartPOS;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.awt.Component;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author adrianromero
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JParamsCustomer extends javax.swing.JPanel implements ReportEditorCreator {
    private static final long serialVersionUID = 1827414826679242883L;

    private DataLogicCustomers dlCustomers;
    private CustomerInfo currentcustomer;

    /**
     * Creates new form JParamsCustomer
     */
    public JParamsCustomer() {

        initComponents();

        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                currentcustomer = null;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                currentcustomer = null;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                currentcustomer = null;
            }
        });
    }

    /**
     *
     * @param app
     */
    @Override
    public void init(AppView app) {
        dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomers");
    }

    /**
     *
     * @throws BasicException
     */
    @Override
    public void activate() throws BasicException {

        currentcustomer = null;
        jTextField1.setText(null);
    }

    /**
     *
     * @return
     */
    @Override
    public SerializerWrite getSerializerWrite() {
        return new SerializerWriteBasic(new Datas[]{Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING});
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
     * @return @throws BasicException
     */
    @Override
    public Object createValue() throws BasicException {

        if (currentcustomer == null) {
            if (jTextField1.getText() == null || jTextField1.getText().equals("")) {
                return new Object[]{QBFCompareEnum.COMP_NONE, null, QBFCompareEnum.COMP_NONE, null};
            } else {
                return new Object[]{QBFCompareEnum.COMP_NONE, null, QBFCompareEnum.COMP_RE, jTextField1.getText()};
            }
        } else {
            return new Object[]{QBFCompareEnum.COMP_EQUALS, currentcustomer.getId(), QBFCompareEnum.COMP_NONE, null};
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        btnCustomer = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.bycustomer"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(400, 60));
        setLayout(null);

        jLabel1.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jLabel1.setText(AppLocal.getIntString("label.customer")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(20, 20, 120, 25);

        jTextField1.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        add(jTextField1);
        jTextField1.setBounds(140, 20, 200, 25);

        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/customer_sml.png"))); // NOI18N
        btnCustomer.setToolTipText("고객 정보 가져오기");
        btnCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerActionPerformed(evt);
            }
        });
        add(btnCustomer);
        btnCustomer.setBounds(350, 10, 52, 40);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed

        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlCustomers);
        finder.search(currentcustomer);
        finder.setVisible(true);
        currentcustomer = finder.getSelectedCustomer();
        if (currentcustomer == null) {
            jTextField1.setText(null);
        } else {
            jTextField1.setText(currentcustomer.getName());
        }

    }//GEN-LAST:event_btnCustomerActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCustomer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}