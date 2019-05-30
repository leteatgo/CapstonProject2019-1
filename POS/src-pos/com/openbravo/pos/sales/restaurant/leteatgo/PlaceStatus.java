package com.openbravo.pos.sales.restaurant.leteatgo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ckddn
 */
public class PlaceStatus {
   
    private int r_no;
    private int totalPlaceNum;              //  number of tables
    private int availablePlaceNum;          //  number of available tables
    private SeatsInfoSender sender;
    
    
    public PlaceStatus(int totalPlaceNum, int availablePlaceNum) {
        FileReader fr = null;
        try {
            File file = new File("restInfo");
            fr = new FileReader(file);
            this.r_no = fr.read();
            fr.close();
            this.totalPlaceNum = totalPlaceNum;
            this.availablePlaceNum = availablePlaceNum;
            this.sender = new SeatsInfoSender();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlaceStatus.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlaceStatus.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(PlaceStatus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getRestaurntNo() {
        return this.r_no;
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
        sender.sendSeatInfo(r_no, totalPlaceNum, availablePlaceNum);
    }

    public void decreaseAvailablePlace() {
        this.availablePlaceNum--;
        sender.sendSeatInfo(r_no, totalPlaceNum, availablePlaceNum);
    }
    
    public void increaseAvailablePlace() {
        this.availablePlaceNum++;
        sender.sendSeatInfo(r_no, totalPlaceNum, availablePlaceNum);
    }
    
    @Override
    public String toString() {
        return this.r_no + " Place Status : " + this.availablePlaceNum + " / " + this.totalPlaceNum + ".";
    }
}
