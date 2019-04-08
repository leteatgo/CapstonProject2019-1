package com.openbravo.pos.sales.restaurant.leteatgo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ckddn
 */
public class PlaceStatus {

    private static String URLpath = "/seatsChanged";
   
    private int totalPlaceNum;              //  number of tables
    private int availablePlaceNum;          //  number of available tables
    private SeatsInfoSender sender;
            
    public PlaceStatus(int totalPlaceNum, int availablePlaceNum) {
        this.totalPlaceNum = totalPlaceNum;
        this.availablePlaceNum = availablePlaceNum;
        
        this.sender = new SeatsInfoSender(URLpath);
    }

    public int getTotalPlaceNum() {
        return totalPlaceNum;
    }

    public void setTotalPlaceNum(int totalPlaceNum) {
        this.totalPlaceNum = totalPlaceNum;
    }

    public int getAvailablePlaceNum() {
        return availablePlaceNum;
    }

    public void setAvailablePlaceNum(int availablePlaceNum) {
        this.availablePlaceNum = availablePlaceNum;
    }

    public void decreaseAvailablePlace() {
        this.availablePlaceNum--;
        sender.sendSeatInfo(totalPlaceNum, availablePlaceNum);
    }
    
    public void increaseAvailablePlace() {
        this.availablePlaceNum++;
        sender.sendSeatInfo(totalPlaceNum, availablePlaceNum);
    }
    
    @Override
    public String toString() {
        return "Place Status : " + this.availablePlaceNum + " / " + this.totalPlaceNum + ".";
    }
}
