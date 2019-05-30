/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms.leteatgo;

/**
 *
 * @author ckddn
 */
public class Owner {
    private int no;
    private String id;
    private String name;
    private String mobile;
    private boolean registered;
    private int r_no;
    
    public Owner(int no, String id, String name, String mobile, boolean registered) {
        this.no = no;
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.registered = registered;
    }
    
    public Owner(int no, String id, String name, String mobile, boolean registered, int r_no) {
        this.no = no;
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.registered = registered;
        this.r_no = r_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean IsRegistered() {
        return this.registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public int getR_no() {
        return r_no;
    }

    public void setR_no(int r_no) {
        this.r_no = r_no;
    }
    
}
