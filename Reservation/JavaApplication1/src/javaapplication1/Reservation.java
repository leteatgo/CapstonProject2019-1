/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author lee, ckddn
 */
public class Reservation {
    private String name;
    private String time;
    private String date;
    private int people_num;
    private String mobile;
    private String user_request;

    public Reservation(String name, String time, String date, int people_num, String mobile, String user_request) {
        this.name = name;
        this.time = time;
        this.date = date;
        this.people_num = people_num;
        this.mobile = mobile;
        this.user_request = user_request;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPeople_num() {
        return people_num;
    }

    public void setPeople_num(int people_num) {
        this.people_num = people_num;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_request() {
        return user_request;
    }

    public void setUser_request(String user_request) {
        this.user_request = user_request;
    }

    @Override
    public String toString() {
        return "name : " + this.name + " time : " + this.date + " " + this.time + " , " + people_num + "peoples";
    }
    
}
