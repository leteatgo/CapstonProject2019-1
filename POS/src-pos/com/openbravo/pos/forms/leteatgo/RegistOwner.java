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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ckddn
 */
public class RegistOwner {
    private final String LetEatGoIP = "http://34.74.255.9:5000/pos/join";
    private AES256Util aES256Util;
    private String name;
    private String id;
    private String pw;
    private String mobile;

    public RegistOwner(String name, String id, String pw, String mobile) {
        try {
            this.aES256Util = new AES256Util();
            this.name = name;
            this.id = id;
            this.pw = aES256Util.encrypt(pw);
            this.mobile = mobile;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RegistOwner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
                Logger.getLogger(RegistOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean regist() {
        try {
            BufferedReader reader = null;
            JSONObject requestObject = new JSONObject();
            requestObject.accumulate("name", this.name);
            requestObject.accumulate("id", this.id);
            requestObject.accumulate("pw", this.pw);
            requestObject.accumulate("mobile", this.mobile);
            
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
                if (buffer.toString().equals("OK"))
                    return true;
            }  catch (IOException ex) {
                Logger.getLogger(RegistOwner.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(RegistOwner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(RegistOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
