/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms.leteatgo;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import javax.imageio.ImageIO;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ckddn
 */
public class RegistRestaurant {
    private final String LetEatGoIP = "http://34.74.255.9:5000/pos/restaurant";
    Geocoding geocoding;

    public RegistRestaurant() {
        geocoding = new Geocoding();
    }
    
    
    public int regist(Restaurant restaurant, Owner owner) {
        byte[] imageInByte;
        BufferedImage originalImage;

        try {
            BufferedReader reader = null;
            JSONObject requestObject = new JSONObject();
            requestObject.accumulate("name", restaurant.getName());
            requestObject.accumulate("address", restaurant.getAddress());
            requestObject.accumulate("mobile", restaurant.getPhone());
            double[] lonlat = geocoding.geoCoding(restaurant.getAddress());
            requestObject.accumulate("longitude", lonlat[0]);
            requestObject.accumulate("latitude", lonlat[1]);
            requestObject.accumulate("r_type", restaurant.getResnum());     //  예약 할지말지
            requestObject.accumulate("s_type", restaurant.getLeftnum());    //  잔여좌석 보여줄지 말지
            requestObject.accumulate("f_type", restaurant.getFoodtype());
            requestObject.accumulate("r_num", 0);

            try {
                originalImage = ImageIO.read(new File(restaurant.getImage()));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(originalImage, "png", baos);
                baos.flush();
                imageInByte = baos.toByteArray();
                baos.close();
                requestObject.accumulate("image", imageInByte);
            } catch (IOException ex) {
                Logger.getLogger(RegistRestaurant.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            JSONObject hours = new JSONObject("{"+restaurant.getHours()+"}");
            requestObject.accumulate("hours", hours);
            requestObject.accumulate("owner_request", restaurant.getOwner_request());
            requestObject.accumulate("o_no", owner.getNo());
            requestObject.accumulate("initial_reservable", 0);
            System.out.println(requestObject);
                    
            URL url = new URL(this.LetEatGoIP);
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
                System.out.println(buffer.toString().substring(1, 3));
                owner.setRegistered(true);
                owner.setR_no(Integer.parseInt(buffer.toString().substring(1, 3)));
            }  catch (IOException ex) {
                Logger.getLogger(RegistRestaurant.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(RegistRestaurant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(RegistRestaurant.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
