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
package com.openbravo.pos.sales.restaurant;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.NullIcon;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.forms.*;
import com.openbravo.pos.sales.*;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
// ckddn
import com.openbravo.pos.sales.restaurant.leteatgo.*;

/**
 *
 * @author JG uniCenta
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JTicketsBagRestaurantMap extends JTicketsBag {
    private static final long serialVersionUID = -3688599503324333082L;

    /**
     *
     */
    public static void newticket() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static class ServerCurrent {

        public ServerCurrent() {
        }
    }

//    private static final Icon ICO_OCU = new ImageIcon(JTicketsBag.class.getResource("/com/openbravo/images/edit_group.png"));
//    private static final Icon ICO_FRE = new NullIcon(22, 22);
    private java.util.List<Place> m_aplaces;
    private java.util.List<Floor> m_afloors;

    private JTicketsBagRestaurant m_restaurantmap;
    private JTicketsBagRestaurantRes m_jreservations;

    private Place m_PlaceCurrent;

// TODO - Add Server JG 03.07.2011
    private ServerCurrent m_ServerCurrent;

    // State vars
    private Place m_PlaceClipboard;
    private CustomerInfo customer;

    private DataLogicReceipts dlReceipts = null;
    private DataLogicSales dlSales = null;
    private final RestaurantDBUtils restDB;
    private static final Icon ICO_OCU_SM = new ImageIcon(Place.class.getResource("/com/openbravo/images/edit_group_sm.png"));
    private static final Icon ICO_WAITER = new NullIcon(1, 1);
    private static final Icon ICO_FRE = new NullIcon(22, 22);
    private String waiterDetails;
    private String customerDetails;
    private String tableName;

    /* Written by ckddn */
    private PlaceStatus placeStatus;
//    private P
    /**
     * Creates new form JTicketsBagRestaurant
     *
     * @param app
     * @param panelticket
     */
    public JTicketsBagRestaurantMap(AppView app, TicketsEditor panelticket) {
        super(app, panelticket);

        restDB = new RestaurantDBUtils(app);    //  DB연결

        dlReceipts = (DataLogicReceipts) app.getBean("com.openbravo.pos.sales.DataLogicReceipts");
        dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");

        m_restaurantmap = new JTicketsBagRestaurant(app, this);
        m_PlaceCurrent = null;
        m_PlaceClipboard = null;
        customer = null;

        try {
            SentenceList sent = new StaticSentence(
                    app.getSession(),
                    "SELECT ID, NAME, IMAGE FROM FLOORS ORDER BY NAME",
                    null,
                    new SerializerReadClass(Floor.class));
            m_afloors = sent.list();
            //  List<Floor> 에 Floors의 정보를 담는다
        } catch (BasicException eD) {
            m_afloors = new ArrayList<>();
        }
        try {
            SentenceList sent = new StaticSentence(
                    app.getSession(),
                    // "SELECT ID, NAME, X, Y, FLOOR, CUSTOMER FROM PLACES ORDER BY FLOOR", 
                    "SELECT ID, NAME, X, Y, FLOOR, CUSTOMER, WAITER, TICKETID, TABLEMOVED FROM PLACES ORDER BY FLOOR",
                    null,
                    new SerializerReadClass(Place.class));
            m_aplaces = sent.list();
            //  List<Place> 에 Places의 정보를 담는다
        } catch (BasicException eD) {
            m_aplaces = new ArrayList<>();
        }
        
        
        initComponents();
        //  화면구성

        // add the Floors containers
        if (m_afloors.size() > 1) {
            // A tab container for 2 or more floors
            JTabbedPane jTabFloors = new JTabbedPane();
            jTabFloors.applyComponentOrientation(getComponentOrientation());
            jTabFloors.setBorder(new javax.swing.border.EmptyBorder(new Insets(5, 5, 5, 5)));
            jTabFloors.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            jTabFloors.setFocusable(false);
            jTabFloors.setRequestFocusEnabled(false);
            m_jPanelMap.add(jTabFloors, BorderLayout.CENTER);

            for (Floor f : m_afloors) { //  floor가 2개 이상이므로 floor 갯수만큼 진행
                f.getContainer().applyComponentOrientation(getComponentOrientation());
                JScrollPane jScrCont = new JScrollPane();
                jScrCont.applyComponentOrientation(getComponentOrientation());
                JPanel jPanCont = new JPanel();
                jPanCont.applyComponentOrientation(getComponentOrientation());

                jTabFloors.addTab(f.getName(), f.getIcon(), jScrCont);
                // Adds a component represented by a title and/or icon, either of which can be null. Cover method for insertTab.
                jScrCont.setViewportView(jPanCont);
                jPanCont.add(f.getContainer());
            }
        } else if (m_afloors.size() == 1) {
            // Just a frame for 1 floor
            Floor f = m_afloors.get(0);
            f.getContainer().applyComponentOrientation(getComponentOrientation());

            JPanel jPlaces = new JPanel();
            jPlaces.applyComponentOrientation(getComponentOrientation());
            jPlaces.setLayout(new BorderLayout());
            jPlaces.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.EmptyBorder(new Insets(5, 5, 5, 5)),
                    new javax.swing.border.TitledBorder(f.getName())));

            JScrollPane jScrCont = new JScrollPane();
            jScrCont.applyComponentOrientation(getComponentOrientation());
            JPanel jPanCont = new JPanel();
            jPanCont.applyComponentOrientation(getComponentOrientation());

            // jPlaces.setLayout(new FlowLayout());           
            m_jPanelMap.add(jPlaces, BorderLayout.CENTER);
            jPlaces.add(jScrCont, BorderLayout.CENTER);
            jScrCont.setViewportView(jPanCont);
            jPanCont.add(f.getContainer());
        }

        // Add all the Table buttons.
        Floor currfloor = null;
        
        //  Written by ckddn
        placeStatus = new PlaceStatus(m_aplaces.size(), 0);
        int count = 0;
        for (Place pl : m_aplaces) {    //  테이블의 갯수만큼 진행
            int iFloor = 0;
           
            if (currfloor == null || !currfloor.getID().equals(pl.getFloor())) {    //  curfloor가 null이거나 null이 아니고 floor가 추가하려는 table의 floor와 같지 않다면
                // Look for a new floor
                do {
                    currfloor = m_afloors.get(iFloor++);                            //  curfloor++
                } while (!currfloor.getID().equals(pl.getFloor()));                 //  만약 하나의 floor에 하나의 place도 나오지 않을 때
            }
            /* Written by ckddn - table counter */
            if (getTicketInfo(pl) != null) {
                count++;
            }
            
            currfloor.getContainer().add(pl.getButton());                           
            pl.setButtonBounds();
            pl.getButton().addActionListener(new MyActionListener(pl));             //  MyActionListener를 place마다 추가
        }
     
        // Writtne by ckddn
        placeStatus.setAvailablePlaceNum(m_aplaces.size() - count);
        System.out.println(placeStatus);
        
        // Add the reservations panel
        m_jreservations = new JTicketsBagRestaurantRes(app, this);                  //  예약 현황 패널 추가
        add(m_jreservations, "res");
    }

    /**
     *
     */
    @Override
    public void activate() {

        // precondicion es que no tenemos ticket activado ni ticket en el panel
        m_PlaceClipboard = null;
        customer = null;
        loadTickets();
        printState();

        m_panelticket.setActiveTicket(null, null);
        m_restaurantmap.activate();

        showView("map"); // arrancamos en la vista de las mesas.

        // postcondicion es que tenemos ticket activado aqui y ticket en el panel
    }

    /**
     *
     * @return
     */
    @Override
    public boolean deactivate() {

        // precondicion es que tenemos ticket activado aqui y ticket en el panel
        if (viewTables()) {

            // borramos el clipboard
            m_PlaceClipboard = null;
            customer = null;

            // guardamos el ticket
            if (m_PlaceCurrent != null) {

                try {
                    dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket(), m_panelticket.getActiveTicket().getPickupId());
                } catch (BasicException e) {
                    new MessageInf(e).show(this);
                }

                m_PlaceCurrent = null;
            }

            // desactivamos cositas.
            printState();
            m_panelticket.setActiveTicket(null, null);

            return true;
        } else {
            return false;
        }

        // postcondicion es que no tenemos ticket activado
    }

    /**
     *
     * @return
     */
    @Override
    protected JComponent getBagComponent() {
        return m_restaurantmap;
    }

    /**
     *
     * @return
     */
    @Override
    protected JComponent getNullComponent() {
        return this;
    }

    /**
     *
     * @return
     */
    public TicketInfo getActiveTicket() {
        return m_panelticket.getActiveTicket();
    }

    /**
     *
     */
    public void moveTicket() {

        // guardamos el ticket
        if (m_PlaceCurrent != null) {

            try {
                dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket(), m_panelticket.getActiveTicket().getPickupId());
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }

            // me guardo el ticket que quiero copiar.
            m_PlaceClipboard = m_PlaceCurrent;

            customer = null;
            m_PlaceCurrent = null;
        }

        printState();
        m_panelticket.setActiveTicket(null, null);
    }

    /**
     *
     * @param c
     * @return
     */
    public boolean viewTables(CustomerInfo c) {
        // deberiamos comprobar si estamos en reservations o en tables...
        if (m_jreservations.deactivate()) {
            showView("map"); // arrancamos en la vista de las mesas.

            m_PlaceClipboard = null;
            customer = c;
            printState();

            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return
     */
    public boolean viewTables() {
        return viewTables(null);
    }

    /**
     *
     */
    public void newTicket() {

        // guardamos el ticket
        if (m_PlaceCurrent != null) {

            try {
                dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket(), m_panelticket.getActiveTicket().getPickupId());
            } catch (BasicException e) {
                new MessageInf(e).show(this); // maybe other guy deleted it
            }

            m_PlaceCurrent = null;
        }

        printState();
        m_panelticket.setActiveTicket(null, null);
    }

    /**
     *
     * @return
     */
    public String getTable() {
        String id = null;
        if (m_PlaceCurrent != null) {
            id = m_PlaceCurrent.getId();
        }
        return (id);
    }

    /**
     *
     * @return
     */
    public String getTableName() {
        String tableName = null;
        if (m_PlaceCurrent != null) {
            tableName = m_PlaceCurrent.getName();
        }
        return (tableName);
    }

    /**
     *
     */
    @Override
    public void deleteTicket() {
        //  계산 할때, 자리 삭제, 자리 변경

        if (m_PlaceCurrent != null) {

            String id = m_PlaceCurrent.getId();
            try {
                dlReceipts.deleteSharedTicket(id);
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }

            m_PlaceCurrent.setPeople(false);
            
            m_PlaceCurrent = null;
            
            /* Written by ckddn  */
            placeStatus.increaseAvailablePlace();
            System.out.println(placeStatus);
        }

        printState();
        m_panelticket.setActiveTicket(null, null);
    }

// Added JG 03.07.2011 - TODO - Change Server Dialog here
    /**
     *
     */
    public void changeServer() {

        if (m_ServerCurrent != null) {

//          Show list of Users
//          Allow Users - CurrentUsers select
//          Compare Users
//          If newServer equal.currentUser
//              Msg NoChange
//          else
//              m_ServerCurrent.setPeople(newServer);
//              Msg Changed to NewServer
        }
    }

    /**
     *
     */
    public void loadTickets() {

        Set<String> atickets = new HashSet<>();

        try {
            java.util.List<SharedTicketInfo> l = dlReceipts.getSharedTicketList();
            for (SharedTicketInfo ticket : l) {
                atickets.add(ticket.getId());
            }
        } catch (BasicException e) {
            new MessageInf(e).show(this);
        }

        for (Place table : m_aplaces) {
            table.setPeople(atickets.contains(table.getId()));
        }
    }

    private void printState() {

        if (m_PlaceClipboard == null) {
            if (customer == null) {
                // Select a table
                m_jText.setText(null);
                // Enable all tables
                for (Place place : m_aplaces) {
                    place.getButton().setEnabled(true);
// get the customer details form the database
// We have set the option show details on table.   
                    if (m_App.getProperties().getProperty("table.tablecolour") == null) {
                        tableName = "<style=font-size:9px;font-weight:bold;><font color = black>" + place.getName() + "</font></style>";
                    } else {
                        tableName = "<style=font-size:9px;font-weight:bold;><font color =" + m_App.getProperties().getProperty("table.tablecolour") + ">" + place.getName() + "</font></style>";
                    }

                    if (Boolean.valueOf(m_App.getProperties().getProperty("table.showwaiterdetails")).booleanValue()) {
                        if (m_App.getProperties().getProperty("table.waitercolour") == null) {
                            waiterDetails = (restDB.getWaiterNameInTable(place.getName()) == null) ? "" : "<style=font-size:9px;font-weight:bold;><font color = red>"
                                    + restDB.getWaiterNameInTableById(place.getId()) + "</font></style><br>";
                        } else {
                            waiterDetails = (restDB.getWaiterNameInTable(place.getName()) == null) ? "" : "<style=font-size:9px;font-weight:bold;><font color ="
                                    + m_App.getProperties().getProperty("table.waitercolour") + ">" + restDB.getWaiterNameInTableById(place.getId()) + "</font></style><br>";
                        }
                        place.getButton().setIcon(ICO_OCU_SM);
                    } else {
                        waiterDetails = "";
                    }

                    if (Boolean.valueOf(m_App.getProperties().getProperty("table.showcustomerdetails")).booleanValue()) {
                        place.getButton().setIcon((Boolean.valueOf(m_App.getProperties().getProperty("table.showwaiterdetails")).booleanValue() && (restDB.getCustomerNameInTable(place.getName()) != null)) ? ICO_WAITER : ICO_OCU_SM);
                        if (m_App.getProperties().getProperty("table.customercolour") == null) {
                            customerDetails = (restDB.getCustomerNameInTable(place.getName()) == null) ? "" : "<style=font-size:9px;font-weight:bold;><font color = blue>"
                                    + restDB.getCustomerNameInTableById(place.getId()) + "</font></style><br>";
                        } else {
                            customerDetails = (restDB.getCustomerNameInTable(place.getName()) == null) ? "" : "<style=font-size:9px;font-weight:bold;><font color ="
                                    + m_App.getProperties().getProperty("table.customercolour") + ">" + restDB.getCustomerNameInTableById(place.getId()) + "</font></style><br>";
                        }
                    } else {
                        customerDetails = "";
                    }

                    if ((Boolean.valueOf(m_App.getProperties().getProperty("table.showwaiterdetails")).booleanValue())
                            || (Boolean.valueOf(m_App.getProperties().getProperty("table.showcustomerdetails")).booleanValue())) {
                        place.getButton().setText("<html><center>" + customerDetails + waiterDetails + tableName + "</html>");
//  JG 29 Aug 13 Bug fix }else{;
                    } else {
                        if (m_App.getProperties().getProperty("table.tablecolour") == null) {
                            tableName = "<style=font-size:10px;font-weight:bold;><font color = black>" + place.getName() + "</font></style>";
                        } else {
                            tableName = "<style=font-size:10px;font-weight:bold;><font color =" + m_App.getProperties().getProperty("table.tablecolour") + ">" + place.getName() + "</font></style>";
                        }

                        place.getButton().setText("<html><center>" + tableName + "</html>");

                    }
                    if (!place.hasPeople()) {
                        place.getButton().setIcon(ICO_FRE);
                    }
                }

                m_jbtnReservations.setEnabled(true);
            } else {
                // receive a customer
                m_jText.setText(AppLocal.getIntString("label.restaurantcustomer", new Object[]{customer.getName()}));
                // Enable all tables
                for (Place place : m_aplaces) {
                    place.getButton().setEnabled(!place.hasPeople());
                }
                m_jbtnReservations.setEnabled(false);
            }
        } else {
            // Moving or merging the receipt to another table
            m_jText.setText(AppLocal.getIntString("label.restaurantmove", new Object[]{m_PlaceClipboard.getName()}));
            // Enable all empty tables and origin table.
            for (Place place : m_aplaces) {
                place.getButton().setEnabled(true);
            }
            m_jbtnReservations.setEnabled(false);
        }

    }

    private TicketInfo getTicketInfo(Place place) {

        try {
            return dlReceipts.getSharedTicket(place.getId());
        } catch (BasicException e) {
            new MessageInf(e).show(JTicketsBagRestaurantMap.this);
            return null;
        }
    }

    /* setActivePlace(m_place, ticket) in actionperformed
    ** 선택한 place를 m_PlaceCurrent에 저장, 선택한 place에 대한 ticket을 TicketsEditor에 지정(티켓과 테이블 이름을 담는다) */
    private void setActivePlace(Place place, TicketInfo ticket) {   
        m_PlaceCurrent = place;
        m_panelticket.setActiveTicket(ticket, m_PlaceCurrent.getName());
    }

    private void showView(String view) {
        CardLayout cl = (CardLayout) (getLayout());
        cl.show(this, view);
    }

    private class MyActionListener implements ActionListener {

        private final Place m_place;

        public MyActionListener(Place place) {  //  클릭한 버튼의 place의 정보 받아온다.
            m_place = place;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {  /*  테이블을 클릭 했을 때   */
            if (m_PlaceClipboard == null) {
                System.out.println("m_PlaceClipboard == null");
                if (customer == null) {
                    System.out.println("customer == null");
                    //  모든 케이스가 이 경우이다.
                    
                    // tables

                    // check if the sharedticket is the same
                    TicketInfo ticket = getTicketInfo(m_place);

                    // check
                    
                    /* ticket과 선택한 테이블에 사람이 있는지 없는지 확인 */
                    if (ticket == null && !m_place.hasPeople()) { //  빈 테이블 클릭 시
                        System.out.println("티켓이 null 이면서 테이블에 사람이 없는경우");
                        // Empty table and checked

                        // table occupied
                        ticket = new TicketInfo();
                        try {
                            dlReceipts.insertSharedTicket(m_place.getId(), ticket, ticket.getPickupId());   //  INSERT INTO SHAREDTICKETS
                        } catch (BasicException e) {
                            new MessageInf(e).show(JTicketsBagRestaurantMap.this); // Glup. But It was empty.
                        }
                        m_place.setPeople(true);                                                            //  set Person Icon visible
                        /* setActivePlace(m_place, ticket) in actionperformed
                         선택한 place를 m_PlaceCurrent에 저장, 선택한 place에 대한 ticket을 TicketsEditor에 지정(티켓과 테이블 이름을 담는다) */
                        setActivePlace(m_place, ticket);    //  간단히 말해 현재 사용중인 테이블임을 지정해준다.
                        
                        /*  Written by ckddn */
                        placeStatus.decreaseAvailablePlace();
                        System.out.println(placeStatus);  //  check table status
                        
                    } else if (ticket == null && m_place.hasPeople()) {
                        System.out.println("티켓이 null 이면서 테이블에 사람이 있는경우");  //  ERROR
                        // The table is now empty
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty")).show(JTicketsBagRestaurantMap.this);
                        m_place.setPeople(false); // fixed  
                    } else if (ticket != null && !m_place.hasPeople()) {
                        System.out.println("티켓이 존재하면서 테이블에 사람이 없는경우");   //  ERROR
                        // The table is now full
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(JTicketsBagRestaurantMap.this);
                        m_place.setPeople(true);

                    } else { // both != null    
                        //  주로 이미 클릭했던 테이블이 이렇다
                        System.out.println("티켓이 존재하면서 테이블에 사람도 있는경우");
                        // Full table                
                        // m_place.setPeople(true); // already true                           
                        setActivePlace(m_place, ticket);

                    }
                } else {
                    System.out.println("customer != null");
                    // receiving customer.

                    // check if the sharedticket is the same
                    TicketInfo ticket = getTicketInfo(m_place);
                    if (ticket == null) {
                        // receive the customer
                        // table occupied
                        ticket = new TicketInfo();

                        try {
                            ticket.setCustomer(customer.getId() == null
                                    ? null
                                    : dlSales.loadCustomerExt(customer.getId()));
                        } catch (BasicException e) {
                            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
                            msg.show(JTicketsBagRestaurantMap.this);
                        }

                        try {
                            dlReceipts.insertSharedTicket(m_place.getId(), ticket, ticket.getPickupId());
                        } catch (BasicException e) {
                            new MessageInf(e).show(JTicketsBagRestaurantMap.this); // Glup. But It was empty.
                        }
                        m_place.setPeople(true);
                        m_PlaceClipboard = null;
                        customer = null;

                        setActivePlace(m_place, ticket);
                    } else {
                        // TODO: msg: The table is now full
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(JTicketsBagRestaurantMap.this);
                        m_place.setPeople(true);
                        m_place.getButton().setEnabled(false);
                    }
                }
            } else {    /*  테이블 이동 버튼이 눌러져 m_PlaceClipboard가 존재할 시 */
                System.out.println("m_PlaceClipboard != null 테이블 이동시 나온다");
                // check if the sharedticket is the same
                TicketInfo ticketclip = getTicketInfo(m_PlaceClipboard);

                if (ticketclip == null) {   /*  ERROR!! 복사한 테이블에 ticket이 없을 수 없다.  */
                    System.err.println("복사한 테이블에 대한 ticket이 없을 경우");
                    new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty")).show(JTicketsBagRestaurantMap.this);
                    m_PlaceClipboard.setPeople(false);
                    m_PlaceClipboard = null;
                    customer = null;
                    printState();
                } else {    /* 복사한 티켓인 ticketclip 이 존재할 시 */    
                    System.out.println("복사한 테이블에 대한 ticket이 있을 경우");
                    if (m_PlaceClipboard == m_place) {  /*  복사한 테이블과 같은 테이블 클릭 => 이동하지않고 테이블 유지 */
                        System.out.println("복사한 테이블이 클릭한 테이블이랑 같을 때");
                        // the same button. Canceling.
                        Place placeclip = m_PlaceClipboard;
                        m_PlaceClipboard = null;
                        customer = null;
                        printState();
                        setActivePlace(placeclip, ticketclip);
                    } else if (!m_place.hasPeople()) {  /*  사람이 없는 빈 테이블 클릭 => 자리이동  */
                        System.out.println("테이블 이동");
                        // Moving the receipt to an empty table
                        TicketInfo ticket = getTicketInfo(m_place); /*  클릭한 테이블에 대한 ticket가져오기 */
////
                        if (ticket == null) {   /*  빈테이블을 클릭했으므로 클릭한 테이블에 대한 티켓이 없다.   */
                            System.out.println("클릭한 테이블에 티켓이 없다면");
                            try {
                                dlReceipts.insertSharedTicket(m_place.getId(), ticketclip, ticketclip.getPickupId());//dlSales.getNextPickupIndex());
                                m_place.setPeople(true);
                                dlReceipts.deleteSharedTicket(m_PlaceClipboard.getId());
                                m_PlaceClipboard.setPeople(false);
                            } catch (BasicException e) {
                                new MessageInf(e).show(JTicketsBagRestaurantMap.this); // Glup. But It was empty.
                            }

                            m_PlaceClipboard = null;
                            customer = null;
                            printState();

                            setActivePlace(m_place, ticketclip);    //  클릭한 테이블에 복사한 ticket부여

                        } else {    /*  ERROR!! 사람이 없는 테이블에 티켓이 발급되어 있을 수 없다.  */
                            System.err.println("클릭한 테이블에 티켓이 있다면 ERROR");    
                            // Full table
                            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(JTicketsBagRestaurantMap.this);
                            m_PlaceClipboard.setPeople(true);
                            printState();
                        }
                    } else {    /* 사람이 있는 다른 테이블을 선택 => 병합 */
                        System.out.println("테이블 병합");
                        // Merge the lines with the receipt of the table
                        TicketInfo ticket = getTicketInfo(m_place); /*  클릭한 테이블의 티켓정보*/

                        if (ticket == null) {   /*  ERROR!! 사람이 있는 다른 테이블에 대한 티켓정보가 없을 수 없다. */
                            System.err.println("클릭한 테이블에 티켓이 없다면");
                            // The table is now empty
                            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty")).show(JTicketsBagRestaurantMap.this);
                            m_place.setPeople(false); // fixed                        
                        } else {    /*  클릭한 테이블에 대한 티켓정보가 존재 한다면 */
                            System.out.println("클릭한 테이블에 티켓이 있다면");
                            //asks if you want to merge tables
                            if (JOptionPane.showConfirmDialog(JTicketsBagRestaurantMap.this, AppLocal.getIntString("message.mergetablequestion"), AppLocal.getIntString("message.mergetable"),JOptionPane.YES_NO_OPTION)
                                    == JOptionPane.YES_OPTION) {
                                // merge lines ticket

                                try {   /*  병합작업을 수락할 때    */
                                    dlReceipts.deleteSharedTicket(m_PlaceClipboard.getId());
                                    m_PlaceClipboard.setPeople(false);  /*  복사한 테이블 비우기*/
                                    if (ticket.getCustomer() == null) {
                                        ticket.setCustomer(ticketclip.getCustomer());
                                    }
                                    for (TicketLineInfo line : ticketclip.getLines()) { /*  복사했던 모든 주문 라인을 클릭한 테이블의 주문에 추가하기   */
                                        ticket.addLine(line);
                                    }
                                    dlReceipts.updateSharedTicket(m_place.getId(), ticket, ticket.getPickupId());   /*  ticket을 수정하기*/
                                } catch (BasicException e) {
                                    new MessageInf(e).show(JTicketsBagRestaurantMap.this); // Glup. But It was empty.
                                }

                                m_PlaceClipboard = null;
                                customer = null;
//clear the original table data 
                                /*  복사하였던 테이블에 대한 정보를 Place 데이터베이스에 수정을 가해줌  
                                **  테이블 이름으로 검색하여 고객이름, 웨이터이름, 테이블이동기록, 테이블의 고유티켓 아이디 = null로 초기화 */
                                restDB.clearCustomerNameInTable(restDB.getTableDetails(ticketclip.getId()));
                                restDB.clearWaiterNameInTable(restDB.getTableDetails(ticketclip.getId()));
                                restDB.clearTableMovedFlag(restDB.getTableDetails(ticketclip.getId()));
                                restDB.clearTicketIdInTable(restDB.getTableDetails(ticketclip.getId()));
                                //           restDB.clearTableMovedFlag("");
                                printState();

                                setActivePlace(m_place, ticket);
                                
                                /*  Written by ckddn */
                                placeStatus.increaseAvailablePlace();
                                System.out.println(placeStatus);
                            } else {    /*  병합작업을 취소할때 */
                                // Cancel merge operations
                                Place placeclip = m_PlaceClipboard;
                                m_PlaceClipboard = null;
                                customer = null;
                                printState();
                                setActivePlace(placeclip, ticketclip);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param btnText
     */
    public void setButtonTextBags(String btnText) {
        m_PlaceClipboard.setButtonText(btnText);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jPanelMap = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        m_jbtnReservations = new javax.swing.JButton();
        m_jbtnRefresh = new javax.swing.JButton();
        m_jText = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        m_jPanelMap.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        m_jPanelMap.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        m_jbtnReservations.setFont(StartPOS.getgblFont().deriveFont( 0, 12)); // NOI18N
        m_jbtnReservations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnReservations.setText(AppLocal.getIntString("button.reservations")); // NOI18N
        m_jbtnReservations.setToolTipText("예약화면 열기");
        m_jbtnReservations.setFocusPainted(false);
        m_jbtnReservations.setFocusable(false);
        m_jbtnReservations.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnReservations.setMaximumSize(new java.awt.Dimension(133, 40));
        m_jbtnReservations.setMinimumSize(new java.awt.Dimension(133, 40));
        m_jbtnReservations.setPreferredSize(new java.awt.Dimension(133, 40));
        m_jbtnReservations.setRequestFocusEnabled(false);
        m_jbtnReservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnReservationsActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnReservations);

        m_jbtnRefresh.setFont(StartPOS.getgblFont().deriveFont( 0, 10)); // NOI18N
        m_jbtnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/reload.png"))); // NOI18N
        m_jbtnRefresh.setText(AppLocal.getIntString("button.reloadticket")); // NOI18N
        m_jbtnRefresh.setToolTipText("테이블 정보 새로고침");
        m_jbtnRefresh.setFocusPainted(false);
        m_jbtnRefresh.setFocusable(false);
        m_jbtnRefresh.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnRefresh.setMaximumSize(new java.awt.Dimension(100, 40));
        m_jbtnRefresh.setMinimumSize(new java.awt.Dimension(100, 40));
        m_jbtnRefresh.setPreferredSize(new java.awt.Dimension(100, 40));
        m_jbtnRefresh.setRequestFocusEnabled(false);
        m_jbtnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnRefreshActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnRefresh);
        jPanel2.add(m_jText);

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);

        m_jPanelMap.add(jPanel1, java.awt.BorderLayout.NORTH);

        add(m_jPanelMap, "map");
    }// </editor-fold>//GEN-END:initComponents

    /* 새로고침 버튼 */
    private void m_jbtnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnRefreshActionPerformed

        m_PlaceClipboard = null;
        customer = null;
        loadTickets();
        printState();

    }//GEN-LAST:event_m_jbtnRefreshActionPerformed

    /* 예약 버튼 */
    private void m_jbtnReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnReservationsActionPerformed

        showView("res");
        m_jreservations.activate();

    }//GEN-LAST:event_m_jbtnReservationsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel m_jPanelMap;
    private javax.swing.JLabel m_jText;
    private javax.swing.JButton m_jbtnRefresh;
    private javax.swing.JButton m_jbtnReservations;
    // End of variables declaration//GEN-END:variables

}
