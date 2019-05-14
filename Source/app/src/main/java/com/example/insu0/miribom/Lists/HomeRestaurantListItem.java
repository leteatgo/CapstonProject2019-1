package com.example.insu0.miribom.Lists;

import android.graphics.Bitmap;

public class HomeRestaurantListItem {
    private int resNo;
    private String resName;
    private Bitmap resIcon;

    public HomeRestaurantListItem(int resNo, String resName, Bitmap resIcon) {
        this.resNo = resNo;
        this.resName = resName;
        this.resIcon = resIcon;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public Bitmap getResIcon() {
        return resIcon;
    }

    public void setResIcon(Bitmap resIcon) {
        this.resIcon = resIcon;
    }

    public int getResNo() {
        return resNo;
    }

    @Override
    public String toString() {
        return "restaurant no: "+ resNo +"\nrestaurant name: " + resName;
    }
}
