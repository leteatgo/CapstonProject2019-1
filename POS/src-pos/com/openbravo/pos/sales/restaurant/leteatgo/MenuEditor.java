/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales.restaurant.leteatgo;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ckddn
 */
public class MenuEditor {
    private final String LetEatGoIP = "http://34.74.255.9:5000/pos/restaurant/menu/";
    private int res_no;
    
    // Constructor: set restaurant no
    public MenuEditor() {
        FileReader fr = null;
        try {
            File file = new File("restInfo");
            fr = new FileReader(file);
            res_no = fr.read();
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
    public int insertMenu(Object[] values) {
        try {
            BufferedReader reader = null;
            JSONObject requestObject = new JSONObject();
            requestObject.accumulate("res_no", this.res_no);//  ResNo
            requestObject.accumulate("id", values[0]);      //  ID
            requestObject.accumulate("code", values[2]);    //  Code
            requestObject.accumulate("name", values[3]);    //  Name
            requestObject.accumulate("price", values[7]);   //  Price Sell
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage) values[11], "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            requestObject.accumulate("image", imageInByte); //  Image
            baos.close();
            System.out.println(requestObject);
                    
            URL url = new URL(this.LetEatGoIP + "insert");
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
                return 1;
            }  catch (IOException ex) {
                Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    public int updateMenu(Object[] values) {
        try {
            BufferedReader reader = null;
            JSONObject requestObject = new JSONObject();
            requestObject.accumulate("res_no", this.res_no);//  ResNo
            requestObject.accumulate("id", values[0]);      //  ID
            requestObject.accumulate("code", values[2]);    //  Code
            requestObject.accumulate("name", values[3]);    //  Name
            requestObject.accumulate("price", values[7]);   //  Price Sell
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage) values[11], "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            requestObject.accumulate("image", imageInByte); //  Image
            baos.close();
            System.out.println(requestObject);
                    
            URL url = new URL(this.LetEatGoIP + "update");
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
                return 1;
            }  catch (IOException ex) {
                Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    public int deleteMenu(Object[] values) {
        try {
            BufferedReader reader = null;
            JSONObject requestObject = new JSONObject();
            requestObject.accumulate("res_no", this.res_no);//  ResNo
            requestObject.accumulate("id", values[0]);      //  ID
//            requestObject.accumulate("code", values[2]);    //  Code
//            requestObject.accumulate("name", values[3]);    //  Name
//            requestObject.accumulate("price", values[7]);   //  Price Sell
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write((RenderedImage) values[11], "jpg", baos);
//            baos.flush();
//            byte[] imageInByte = baos.toByteArray();
//            requestObject.accumulate("image", imageInByte); //  Image
//            baos.close();
            System.out.println(requestObject);
                    
            URL url = new URL(this.LetEatGoIP + "delete");
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
                return 1;
            }  catch (IOException ex) {
                Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException | JSONException ex) {
            Logger.getLogger(MenuEditor.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return 0;
    }
}
