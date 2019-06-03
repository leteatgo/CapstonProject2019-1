package com.example.insu0.miribom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insu0.miribom.Data.DataUtils;
import com.example.insu0.miribom.Data.InfoFragment;
import com.example.insu0.miribom.Data.ReserveData;
import com.example.insu0.miribom.Data.ReserveFragment;
import com.example.insu0.miribom.Lists.HomeRestaurantListItem;
import com.example.insu0.miribom.Servers.MiribomInfo;

import org.json.JSONArray;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReserveActivity extends AppCompatActivity
{
    private static final String TAG = "ReserveActivity";
    private HomeRestaurantListItem restaurant;

    private int uNo, resNo;
//    private String name;
//    private String mobile;
//    private String address;
//    private String hours;
//    private String image;
//    private byte[] imageDatas;
//    private int totalPlaceNum, availablePlaceNum, reservablePlaceNum;

//    private TextView lunchtimeTextView;
    private TextView restName;
//    private TextView telTextView;
//    private TextView addrTextView;
//    private TextView seatinfoTextView;
    private ImageView res_restImageView;

    /*Reservation Information*/
//    private int res_ppl_num;
//    private String res_day;
//    private String res_time;
//    private String usr_Req;
//    private String owner_Req;

//    private ReserveData reserveData;



    ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        //////////////////////////////////////
        Bundle bundle = getIntent().getExtras();
        restaurant = bundle.getParcelable("ResItem");
        Log.d(TAG, "onCreate: " + restaurant.toString());   //  succeed
        uNo = getIntent().getIntExtra("uNo", -1);
        resNo = restaurant.getResNo();

        restName = (TextView) findViewById(R.id.res_restName);
        restName.setText(restaurant.getResName());
        res_restImageView = (ImageView) findViewById(R.id.res_restImageView);
        res_restImageView.setImageBitmap(restaurant.getResIcon());

        vp = (ViewPager)findViewById(R.id.vp);
        ImageButton infoBtn = (ImageButton)findViewById(R.id.infoBtn);
        ImageButton resBtn = (ImageButton)findViewById(R.id.resBtn);

        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        infoBtn.setOnClickListener(movePageListener);
        infoBtn.setTag(0);
        resBtn.setOnClickListener(movePageListener);
        resBtn.setTag(1);
    }

    View.OnClickListener movePageListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
        }
    };

    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(android.support.v4.app.FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new InfoFragment(restaurant,getApplicationContext());
                case 1:
                    return new ReserveFragment(uNo, restaurant, getApplicationContext());
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 2;
        }
    }
}