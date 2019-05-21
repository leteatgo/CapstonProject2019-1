/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ckddn
 */
public class LoginHelper {
    private static String LetEatGoIP = "http://34.74.255.9:5000/reservation/login";
    private AES256Util aES256Util;

    private String id, pw;
    
    public LoginHelper(String id, String pw) throws UnsupportedEncodingException {
        this.id = id;
        this.pw = pw;
        this.aES256Util = new AES256Util();
    }
    
    public OwnerInfo signInTask(OwnerInfo owner) throws UnsupportedEncodingException {
        try {
            BufferedReader reader = null;
            JSONObject requestObject = new JSONObject();
            requestObject.accumulate("id", id);
            
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
//                System.out.println(buffer.toString());
                JSONObject jsonObect = new JSONObject(buffer.toString());
                int no = jsonObect.getInt("no");
                String hash = jsonObect.getString("hash");
                String name = jsonObect.getString("name");
                String mobile = jsonObect.getString("mobile");
//                System.out.println("no : " + no + " id : " + id + " name : " + name + " mobile : " + mobile);
                try {
                    String decrypted = aES256Util.decrypt(hash);
                    if (decrypted.equals(pw)) {
                        System.out.println("비밀번호 일치");
                        owner = new OwnerInfo(no, id, name, mobile, true);
                        return owner;
                    }
                    else
                        return owner;
                } catch (GeneralSecurityException ex) {
                    Logger.getLogger(LoginHelper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  catch (IOException ex) {
                Logger.getLogger(LoginHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoginHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(LoginHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return owner;
    }
}
