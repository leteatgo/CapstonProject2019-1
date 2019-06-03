package com.example.insu0.miribom.Lists;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import com.example.insu0.miribom.Data.DataUtils;
import com.example.insu0.miribom.Servers.MiribomInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeRestaurantListItem implements
        Comparable<HomeRestaurantListItem>, Parcelable {
    // name, mobile, address, LatLng, hours, image
    private int resNo;
    private String resName;
    private String address;
    private String mobile;
    private double latitude;
    private double longitude;
    private Bitmap resIcon;
    private String imageUrl;
    private String hours;
    private double distance;
    private String ownerRequest;

    public HomeRestaurantListItem(int resNo, String resName, String address, String mobile, String imageUrl,
                                  double latitude, double longitude, String hours, double distance, String ownerRequest) {
        this.resNo = resNo;
        this.resName = resName;
        this.address = address;
        this.mobile = mobile;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hours = hours;
        this.distance = distance;
        this.ownerRequest = ownerRequest;
        setResIcon(imageUrl);
    }



//    public HomeRestaurantListItem(int resNo, String resName, String address, String mobile, Bitmap resIcon, double latitude, double longitude, String hours, double distance, String ownerRequest) {
//        this.resNo = resNo;
//        this.resName = resName;
//        this.address = address;
//        this.mobile = mobile;
//        this.resIcon = resIcon;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.hours = hours;
//        this.distance = distance;
//        this.ownerRequest = ownerRequest;
//    }

    protected HomeRestaurantListItem(Parcel in) {
        resNo = in.readInt();
        resName = in.readString();
        address = in.readString();
        mobile = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        byte[] bytes = in.createByteArray();
        resIcon = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        hours = in.readString();
        ownerRequest = in.readString();
    }



    public static final Creator<HomeRestaurantListItem> CREATOR = new Creator<HomeRestaurantListItem>() {
        @Override
        public HomeRestaurantListItem createFromParcel(Parcel in) {
            return new HomeRestaurantListItem(in);
        }

        @Override
        public HomeRestaurantListItem[] newArray(int size) {
            return new HomeRestaurantListItem[size];
        }
    };

    public int getResNo() {
        return resNo;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Bitmap getResIcon() {
        return resIcon;
    }

    public void setResIcon(Bitmap resIcon) {
        this.resIcon = resIcon;
    }

    private void setResIcon(String imageUrl) {
        new ImageLoader().execute("http://" + MiribomInfo.ipAddress + "/image/load", imageUrl);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getOwnerRequest() {
        return ownerRequest;
    }

    public void setOwnerRequest(String ownerRequest) {
        this.ownerRequest = ownerRequest;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString(){
        return "번호: "+ resNo +" 이름: " + resName + " 주소: " + this.address;
    }

    @Override
    public int compareTo(HomeRestaurantListItem o) {
        if (this.distance - o.getDistance() > 0)
            return 1;
        else
            return -1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resNo);
        dest.writeString(resName);
        dest.writeString(address);
        dest.writeString(mobile);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resIcon.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        dest.writeByteArray(bytes);
        dest.writeString(hours);
        dest.writeString(ownerRequest);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public class ImageLoader extends AsyncTask<String, String, String> {
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
                resIcon = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
