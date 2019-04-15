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
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ckddn
 */
public class SeatsInfoSender {
    
    private static String LetEatGoIP = "http://35.243.118.35:5000/pos/seatsChanged";
//    private static String LetEatGoIP = "http://localhost:5000/pos/pos";
   
    public void sendSeatInfo(int totalPlaceNum, int availablePlaceNum) {
        try {
            BufferedReader reader = null;
            JSONObject requestObject = new JSONObject();
            requestObject.accumulate("no", 1);
            requestObject.accumulate("totalPlaceNum", totalPlaceNum);
            requestObject.accumulate("availablePlaceNum", availablePlaceNum);
            URL url = new URL(LetEatGoIP);
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/text");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();
                
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(requestObject.toString());
                writer.flush();
                writer.close();
                
                InputStream stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                System.out.println(buffer.toString());
            }  catch (IOException ex) {
                Logger.getLogger(PlaceStatus.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(SeatsInfoSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SeatsInfoSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
