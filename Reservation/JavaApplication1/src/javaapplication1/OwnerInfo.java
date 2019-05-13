/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author ckddn
 */
public class OwnerInfo {
    private int no;
    private String id;
    private String name;
    private String mobile;
    private boolean verified;

    public OwnerInfo(int no, String id, String name, String mobile, boolean verified) {
        this.no = no;
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.verified = verified;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "Owner no : " + no + " id : " + id + " name : " + name + " mobile : " + mobile;
    }
    
}
