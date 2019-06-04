package com.example.insu0.miribom.Data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.insu0.miribom.Servers.MiribomInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//implemented by Insu Yang
public class CatListViewItem {
    private CatListViewAdapter catListViewAdapter;
    private int resNo;
    private String catImgUrl;
    private Bitmap catResImg;
    private String catResName;
    private String catResAddress;
    private String catResDest;
    private int catLeftSeatNum;
    private double distance;

    //  음식 종류로 클릭
    public CatListViewItem(int resNo, String catImgUrl, String catResName, String catResAddress, CatListViewAdapter catListViewAdapter) {
        this.resNo = resNo;
        this.catImgUrl = catImgUrl;
        this.catResName = catResName;
        this.catResAddress = catResAddress;
        setResIcon(catImgUrl);
        this.catListViewAdapter = catListViewAdapter;
        this.catResDest = catResAddress;
    }

    //  가까운 음식점 순
    public CatListViewItem(int resNo, String catImgUrl, String catResName, String catResAddress, double distance, CatListViewAdapter catListViewAdapter) {
        this.resNo = resNo;
        this.catImgUrl = catImgUrl;
        this.catResName = catResName;
        this.catResAddress = catResAddress;
        setResIcon(catImgUrl);
        this.distance = distance;
        this.catListViewAdapter = catListViewAdapter;
        this.catResDest = Double.toString(distance).substring(0, 4)+ "km";
    }

    //  기다리지 않고 바로가기
    public CatListViewItem(int resNo, String catImgUrl, String catResName, String catResAddress, int catLeftSeatNum, CatListViewAdapter catListViewAdapter) {
        this.resNo = resNo;
        this.catImgUrl = catImgUrl;
        this.catResName = catResName;
        this.catResAddress = catResAddress;
        setResIcon(catImgUrl);
        this.catLeftSeatNum = catLeftSeatNum;
        this.catListViewAdapter = catListViewAdapter;
        this.catResDest = catLeftSeatNum + "좌석 이용가능";
    }



    private void setResIcon(String imageUrl) {
        new ImageLoader().execute("http://" + MiribomInfo.ipAddress + "/image/load", imageUrl);
    }

    public String getCatResDest() {
        return catResDest;
    }

    public void setCatResDest(String catResDest) {
        this.catResDest = catResDest;
    }

    public void setCatResImage(Bitmap image){
        catResImg = image;
    }

    public void setCatResName(String title){
        catResName = title;
    }

    public void setCatResAddress(String address){
        catResAddress = address;
    }

    public void setResNo(int resNo) {
        this.resNo = resNo;
    }

    public Bitmap getCatResImg(){
        return this.catResImg;
    }

    public String getCatResName(){
        return this.catResName;
    }

    public String getCatResAddress(){
        return this.catResAddress;
    }

    public int getResNo() {
        return resNo;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getCatLeftSeatNum() {
        return catLeftSeatNum;
    }

    public void setCatLeftSeatNum(int catLeftSeatNum) {
        this.catLeftSeatNum = catLeftSeatNum;
    }

    public String getCatImgUrl() {
        return catImgUrl;
    }

    public void setCatImgUrl(String catImgUrl) {
        this.catImgUrl = catImgUrl;
    }

    private class ImageLoader extends AsyncTask<String, String, String> {
        String TAG = "ImageLoader>>>";
        @Override
        protected String doInBackground(String... strings) {
            JSONObject reqInfo = new JSONObject();
            try {
                reqInfo.accumulate("imageUrl", strings[1]);
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                URL url = new URL(strings[0]);
                // settings
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
                writer.write(reqInfo.toString());
                writer.flush();
                writer.close();

                InputStream stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    Log.d(TAG, "doInBackground: readLine, " + line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: "+ result);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                String imageStr = jsonObject.getString("image");
                JSONObject imageObject = new JSONObject(imageStr);
                String data = imageObject.getString("data");
                String[] imageStrs = data.substring(1, data.length() - 1).split(",");
                byte[] imageDatas = new byte[imageStrs.length];
                for (int j = 0; j < imageStrs.length; j++) {
                    imageDatas[j] = DataUtils.intToByteArray(Integer.parseInt(imageStrs[j]));
                }
                catResImg = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
                catListViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
