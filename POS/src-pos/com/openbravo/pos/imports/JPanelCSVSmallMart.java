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
//    CSV Import Panel added by JDL - February 2013
//    Additonal library required - javacsv


package com.openbravo.pos.imports;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.BatchSentence;
import com.openbravo.data.loader.BatchSentenceResource;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.*;
import com.openbravo.pos.util.AltEncrypter;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.*;

/**
 *
 * @author JG uniCenta
 */
public class JPanelCSVSmallMart extends JPanel implements JPanelView {    
    private static final long serialVersionUID = 7485603397765151138L;
    
    private Connection con;  
    private AppConfig config;
    private Session session;

    /**
     *
     * @param oApp
     */
    public JPanelCSVSmallMart(AppView oApp) {
        this(oApp.getProperties()); 
 
    }

    /**
     *
     * @param props
     */
    public JPanelCSVSmallMart(AppProperties props) {  

        initComponents();
        
        config = new AppConfig(props.getConfigFile());
        config.load();

        jMessageBox.setText("데이터베이스 생성 이후 다른 상품이 없을 때 사용할 수 있습니다.\n"
                + "(중복 자료가 있거나 기타 사유로 비정상 동작하는 것을 책임지지 않습니다)\n"
                + "데이터베이스 생성 시점의 기본 분류로 상품을 등록하므로 기본분류가 존재해야 합니다. \n\n"
                + "각 상품은 예제용이므로 가격을 확인하셔야 하며 재고 관리를 하시려면 재고 등록을 먼저 수행하세요. \n"
                + "6천여건을 등록하므로 완료메시지가 나올때까지 기다려주세요.");
  
        }
    
    /**
     *
     * @return
     */
    @Override
    public String getTitle() {
        return AppLocal.getIntString("Menu.SmallMart");
    }

    /**
     *
     * @return
     */
    @Override
    public JComponent getComponent() {
        return this;
    }
     
    /**
     *
     * @throws BasicException
     */
    @Override
    public void activate() throws BasicException {
 // connect to the database
         String db_user =(config.getProperty("db.user"));
         String db_url = (config.getProperty("db.URL"));
         String db_password = (config.getProperty("db.password"));     
         
         if (db_user != null && db_password != null && db_password.startsWith("crypt:")) {
                // the password is encrypted
                AltEncrypter cypher = new AltEncrypter("cypherkey" + db_user);
                db_password = cypher.decrypt(db_password.substring(6));
         }        
         try{
            session = AppViewConnection.createSession(config);
            con = DriverManager.getConnection(db_url,db_user,db_password);
            } catch (Exception e) {                
       }    

    }
    
    /**
     *
     * @return
     */
    @Override
    public boolean deactivate() {        
        return (true);
    }    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jMessageBox = new javax.swing.JTextPane();
        jEnableButton = new javax.swing.JCheckBox();
        jButtonCleardb = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(420, 240));

        jLabel1.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pos_messages"); // NOI18N
        jLabel1.setText(bundle.getString("label.extoptimport")); // NOI18N

        jMessageBox.setEditable(false);
        jMessageBox.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jScrollPane1.setViewportView(jMessageBox);

        jEnableButton.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jEnableButton.setText(bundle.getString("label.extoptbuttonon")); // NOI18N
        jEnableButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jEnableButtonStateChanged(evt);
            }
        });
        jEnableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEnableButtonActionPerformed(evt);
            }
        });

        jButtonCleardb.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        jButtonCleardb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/run_script.png"))); // NOI18N
        jButtonCleardb.setText(bundle.getString("label.extoptrun")); // NOI18N
        jButtonCleardb.setEnabled(false);
        jButtonCleardb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCleardbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jEnableButton)
                        .addGap(74, 74, 74)
                        .addComponent(jButtonCleardb))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCleardb)
                    .addComponent(jEnableButton))
                .addContainerGap(16, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jEnableButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jEnableButtonStateChanged
        if (jEnableButton.isSelected()){
            jButtonCleardb.setEnabled(true);
        }else {
            jButtonCleardb.setEnabled(false);
        }
    }//GEN-LAST:event_jEnableButtonStateChanged

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void jButtonCleardbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCleardbActionPerformed
                // update database using updater scripts
        try {
            BatchSentence bsentence = new BatchSentenceResource(session, "/com/openbravo/pos/scripts/SmallMart.sql");

            java.util.List l = bsentence.list();
            if (l.size() > 0) {
                JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("database.UpdaterWarning"), l.toArray(new Throwable[l.size()])));
            } else {
                JOptionPane.showMessageDialog(this, "Import complete.");
            }
        } catch (BasicException e) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_DANGER, AppLocal.getIntString("database.ScriptNotFound"), e));
            session.close();
        } finally {

        }
    }//GEN-LAST:event_jButtonCleardbActionPerformed

    private void jEnableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEnableButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jEnableButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCleardb;
    private javax.swing.JCheckBox jEnableButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextPane jMessageBox;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    

}
