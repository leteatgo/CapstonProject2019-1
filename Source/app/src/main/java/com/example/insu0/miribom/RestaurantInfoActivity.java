package com.example.insu0.miribom;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashSet;

public class RestaurantInfoActivity extends AppCompatActivity {

    private int resNo;
    private String name;
    private String mobile;
    private String address;
    private String hours;
    private int totalPlaceNum, availablePlaceNum, reservablePlaceNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
        resNo = getIntent().getIntExtra("resNo",-1);
        name = getIntent().getStringExtra("restName");
        address = getIntent().getStringExtra("address");
        mobile = getIntent().getStringExtra("mobile");
        hours = getIntent().getStringExtra("hours");
        totalPlaceNum = getIntent().getIntExtra("totalPlaceNum", 0);
        availablePlaceNum = getIntent().getIntExtra("availablePlaceNum", 0);
        reservablePlaceNum = getIntent().getIntExtra("reservablePlaceNum",0);


        TextView restName = (TextView) findViewById(R.id.restName);
        TextView telTextView = (TextView) findViewById(R.id.telTextView);
        TextView addrTextView = (TextView) findViewById(R.id.addrTextView);
        TextView lunchtimeTextView = (TextView) findViewById(R.id.lunchtimeTextView);
        TextView seatinfoTextView = (TextView) findViewById(R.id.seatinfoTextView);

        restName.setText(name);
        telTextView.setText(mobile);
        addrTextView.setText(address);
        lunchtimeTextView.setText(hours);
        seatinfoTextView.setText("현재 잔여 좌석: " + availablePlaceNum + "석");

    }




}
