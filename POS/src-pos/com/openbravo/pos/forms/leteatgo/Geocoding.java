/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms.leteatgo;

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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ckddn
 */
public class Geocoding {
    private static String baseUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
//    private static String address = "서울특별시 동작구 상도로 272";
    private static String API_KEY = "&key=API_KEY";
   
    public static double[] geoCoding(String address) {
        double[] longlat = new double[2];
        try {
            BufferedReader reader = null;
            JSONObject requestObject = new JSONObject();
            URL url = new URL(baseUrl+address.replaceAll(" ", "+")+API_KEY);
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
                JSONObject jsonObject = new JSONObject(buffer.toString());
                String status = jsonObject.getString("status");
                System.out.println("status : " + status);
                if (status.equals("OK")) {
                    JSONArray resultArray = jsonObject.getJSONArray("results");
                    JSONObject resultObject = resultArray.getJSONObject(0);
                    JSONObject locationObject = resultObject.getJSONObject("geometry").getJSONObject("location");
                    System.out.println(locationObject.getDouble("lng"));
                    System.out.println(locationObject.getDouble("lat"));
                    longlat[0] = locationObject.getDouble("lng");
                    longlat[1] = locationObject.getDouble("lat");
                    return longlat;
                } else
                    return null;
            }  catch (IOException ex) {
                Logger.getLogger(Geocoding.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Geocoding.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(Geocoding.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
