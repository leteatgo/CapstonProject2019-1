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

package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppProperties;
import com.openbravo.pos.forms.JRootFrame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import com.openbravo.pos.sales.JResetPickupID;


/**
 *
 * @author adrianromero
 */
public class JResetPickupID extends javax.swing.JFrame {
    private static final long serialVersionUID = -705033357745330748L;
    
    private JPanelResetPickupId config;

    /**
     *
     * @param props
     */
    public JResetPickupID(AppProperties props) {
        
        initComponents();
        
        try {
            this.setIconImage(ImageIO.read(JRootFrame.class.getResourceAsStream("/com/openbravo/images/favicon.png")));
        } catch (IOException e) {
        }   
        setTitle(AppLocal.APP_NAME + " - " + AppLocal.APP_VERSION + " - " + AppLocal.getIntString("Menu.Resetpickup"));
        
        addWindowListener(new MyFrameListener()); 
        
       config = new JPanelResetPickupId(props);
        
        getContentPane().add(config, BorderLayout.CENTER);
       
        try {
            config.activate();
        } catch (BasicException e) { // never thrown ;-)
        }
    }
    
    private class MyFrameListener extends WindowAdapter{
        
        @Override
        public void windowClosing(WindowEvent evt) {
            if (config.deactivate()) {
                dispose();
            }
        }
        @Override
        public void windowClosed(WindowEvent evt) {
            System.exit(0);
        }
    }    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-507)/2, (screenSize.height-304)/2, 507, 304);
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                AppConfig config = new AppConfig(args);
                config.load();    
                
// Set the look and feel.
// JG 6 May 2013 to Multicatch                
                try {                    
                    Object laf = Class.forName(config.getProperty("swing.defaultlaf")).newInstance();                    
                    if (laf instanceof LookAndFeel){
                        UIManager.setLookAndFeel((LookAndFeel) laf);
                    } else if (laf instanceof SubstanceSkin) {                      
                        SubstanceLookAndFeel.setSkin((SubstanceSkin) laf);                   
                    }
// JG 6 May 2013 to multicatch
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                }
                
                JResetPickupID resetFrame = new JResetPickupID(config);//
                resetFrame.setSize(360, 120);
                resetFrame.setVisible(true);
                
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
