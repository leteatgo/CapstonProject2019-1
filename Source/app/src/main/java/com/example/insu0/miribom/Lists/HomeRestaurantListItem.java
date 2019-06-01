package com.example.insu0.miribom.Lists;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

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
    private String hours;
    private double distance;

    public HomeRestaurantListItem(int resNo, String resName, String address, String mobile, Bitmap resIcon, double latitude, double longitude, String hours, double distance) {
        this.resNo = resNo;
        this.resName = resName;
        this.address = address;
        this.mobile = mobile;
        this.resIcon = resIcon;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hours = hours;
        this.distance = distance;
    }

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
        distance = in.readDouble();
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
        resIcon.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        dest.writeByteArray(bytes);
        dest.writeString(hours);
        dest.writeDouble(distance);
    }

    @Override
    public int describeContents() {
        return 0;
    }



}
