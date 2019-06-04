package com.example.insu0.miribom.Lists;

public class SearchItem {
    private int resNo;
    private String name;

    public SearchItem(int resNo, String name) {
        this.resNo = resNo;
        this.name = name;
    }

    public int getResNo() {
        return resNo;
    }

    public void setResNo(int resNo) {
        this.resNo = resNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
