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
public class LoginOwner {
    private final String LetEatGoIP = "http://34.74.255.9:5000/pos/login";
    private final String UNKNOWN_ID_STR = "존재하지 않는 ID입니다. 올바른 ID를 입력해주세요.";

    private Owner owner;
    
    private AES256Util aES256Util;
    private String id;
    private String pw;

    public LoginOwner(String id, String pw) {
        try {
            this.id = id;
            this.pw = pw;
            aES256Util = new AES256Util();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LoginOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Owner login() {
        try {
            BufferedReader reader = null;
            JSONObject requestObject = new JSONObject();
            requestObject.accumulate("id", this.id);
            
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
                System.out.println(buffer.toString());
                        
                if (buffer.toString().equals(UNKNOWN_ID_STR))
                    return null;
                try {
                    JSONObject object = new JSONObject(buffer.toString());
                    System.out.println(object.toString());
                            
                   if (aES256Util.decrypt(object.getString("hash")).equals(pw))
                        if (object.has("r_no")) {
                            owner = new Owner(object.getInt("no"), id, object.getString("name"), object.getString("mobile"), true, object.getInt("r_no"));
                            return owner;
                        }
                        else {
                            owner = new Owner(object.getInt("no"), id, object.getString("name"), object.getString("mobile"), false);
                            return owner;
                        }
                    else
                        return null;
                } catch (GeneralSecurityException ex) {
                    Logger.getLogger(LoginOwner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(LoginOwner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  catch (IOException ex) {
                Logger.getLogger(LoginOwner.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoginOwner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(LoginOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
