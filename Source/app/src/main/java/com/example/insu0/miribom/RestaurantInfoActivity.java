package com.example.insu0.miribom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insu0.miribom.Data.DataUtils;
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

import static com.example.insu0.miribom.Data.DataUtils.hoursFormatter;

/* author ckddn
 * */

public class RestaurantInfoActivity extends AppCompatActivity {

    private int uNo, resNo;
    private String name;
    private String mobile;
    private String address;
    private String hours;
    private String image;
    private byte[] imageDatas;
    private int totalPlaceNum, availablePlaceNum, reservablePlaceNum;
    private TextView lunchtimeTextView;
    private TextView restName;
    private TextView telTextView;
    private TextView addrTextView;
    private TextView seatinfoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
//        uNo = getIntent().getIntExtra("uNo", -1);
//        resNo = getIntent().getIntExtra("resNo", -1);
//
//        lunchtimeTextView = (TextView) findViewById(R.id.lunchtimeTextView);
//        restName = (TextView) findViewById(R.id.restName);
//        telTextView = (TextView) findViewById(R.id.telTextView);
//        addrTextView = (TextView) findViewById(R.id.addrTextView);
//        seatinfoTextView = (TextView) findViewById(R.id.seatinfoTextView);
//
//
//        new RestaurantInfoRequestTask().execute("http://" + MiribomInfo.ipAddress + "/restaurant/getRestInfo", Integer.toString(resNo));
//
//        ImageView resTab = (ImageView) findViewById(R.id.resTab);
//        resTab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new FindRequestTask().execute("http://" + MiribomInfo.ipAddress + "/restaurant/reservation/find", Integer.toString(resNo), "19-05-07");
//            }
//        });
//        ImageView infoTab = (ImageView) findViewById(R.id.infoTab);
//        infoTab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new MakeRequestTask().execute("http://" + MiribomInfo.ipAddress + "/restaurant/reservation/make",
//                        Integer.toString(uNo) ,Integer.toString(resNo), "19-05-07", "23:30:00", Integer.toString(5000),
//                        Integer.toString(4), "I need a parking lot!");
//            }
//        });
    }

//    public class MakeRequestTask extends AsyncTask<String, String, String> {
//        String TAG = "MakeRequestTask>>>";
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                JSONObject reqInfo = new JSONObject();
//                reqInfo.accumulate("uNo", strings[1]);
//                reqInfo.accumulate("resNo", strings[2]);
//                reqInfo.accumulate("date", strings[3]);
//                reqInfo.accumulate("time", strings[4]);
//                reqInfo.accumulate("deposit", strings[5]);
//                reqInfo.accumulate("peopleNum", strings[6]);
//                reqInfo.accumulate("userRequest", strings[7]);
//
//                HttpURLConnection conn = null;
//                BufferedReader reader = null;
//                try {
//                    URL url = new URL(strings[0]);
//                    // settings
//                    conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Cache-Control", "no-cache");
//                    conn.setRequestProperty("Content-Type", "application/json");
//                    conn.setRequestProperty("Accept", "application/text");
//                    conn.setRequestProperty("Accept", "application/json");
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//                    conn.connect();
//
//                    OutputStream outputStream = conn.getOutputStream();
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
//                    writer.write(reqInfo.toString());
//                    writer.flush();
//                    writer.close();
//
//                    InputStream stream = conn.getInputStream();
//                    reader = new BufferedReader(new InputStreamReader(stream));
//                    StringBuffer buffer = new StringBuffer();
//                    String line = "";
//                    while ((line = reader.readLine()) != null) {
//                        buffer.append(line);
//                        Log.d(TAG, "doInBackground: readLine, " + line);
//                    }
//                    return buffer.toString();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Log.d(TAG, "onPostExecute: "+ result);
//            // 예약되었습니다. or 예약 등록에 실패 하였습니다.
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    public class FindRequestTask extends AsyncTask<String, String, String> {
//        String TAG = "FindRequestTask>>>";
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                JSONObject reqInfo = new JSONObject();
//                reqInfo.accumulate("resNo", strings[1]);
//                reqInfo.accumulate("date", strings[2]);
//                HttpURLConnection conn = null;
//                BufferedReader reader = null;
//                try {
//                    URL url = new URL(strings[0]);
//                    // settings
//                    conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Cache-Control", "no-cache");
//                    conn.setRequestProperty("Content-Type", "application/json");
//                    conn.setRequestProperty("Accept", "application/text");
//                    conn.setRequestProperty("Accept", "application/json");
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//                    conn.connect();
//
//                    OutputStream outputStream = conn.getOutputStream();
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
//                    writer.write(reqInfo.toString());
//                    writer.flush();
//                    writer.close();
//
//                    InputStream stream = conn.getInputStream();
//                    reader = new BufferedReader(new InputStreamReader(stream));
//                    StringBuffer buffer = new StringBuffer();
//                    String line = "";
//                    while ((line = reader.readLine()) != null) {
//                        buffer.append(line);
//                        Log.d(TAG, "doInBackground: readLine, " + line);
//                    }
//                    return buffer.toString();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Log.d(TAG, "onPostExecute: "+ result);
//            StringBuilder sb = new StringBuilder();
//
//            try {
//                JSONArray array = new JSONArray(result);
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject jsonObject = array.getJSONObject(i);
//                    sb.append(jsonObject.getString("time") + " " + jsonObject.getInt("count"));
//                    sb.append('\n');
//                }
//            } catch (JSONException e) {
//                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//            Toast.makeText(getApplicationContext(), sb, Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//
//    public class RestaurantInfoRequestTask extends AsyncTask<String, String, String> {
//        String TAG = "RestaurantInfoRequestTask>>>";
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                JSONObject reqInfo = new JSONObject();
//                reqInfo.accumulate("resNo", strings[1]);
//                HttpURLConnection conn = null;
//                BufferedReader reader = null;
//                try {
//                    URL url = new URL(strings[0]);
//                    // settings
//                    conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Cache-Control", "no-cache");
//                    conn.setRequestProperty("Content-Type", "application/json");
//                    conn.setRequestProperty("Accept", "application/text");
//                    conn.setRequestProperty("Accept", "application/json");
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//                    conn.connect();
//
//                    OutputStream outputStream = conn.getOutputStream();
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
//                    writer.write(reqInfo.toString());
//                    writer.flush();
//                    writer.close();
//
//                    InputStream stream = conn.getInputStream();
//                    reader = new BufferedReader(new InputStreamReader(stream));
//                    StringBuffer buffer = new StringBuffer();
//                    String line = "";
//                    while ((line = reader.readLine()) != null) {
//                        buffer.append(line);
//                        Log.d(TAG, "doInBackground: readLine, " + line);
//                    }
//                    return buffer.toString();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Log.d(TAG, "onPostExecute: "+ result);
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                name = jsonObject.getString("name");
//                address = jsonObject.getString("address");
//                mobile = jsonObject.getString("mobile");
//                image = jsonObject.getString("image");
//                hours = jsonObject.getString("hours");
//                totalPlaceNum = jsonObject.getInt("s_total_num");
//                availablePlaceNum = jsonObject.getInt("s_left_num");
//                reservablePlaceNum = jsonObject.getInt("s_ava_num");
//
//
//                DisplayMetrics displayMetrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                int width = displayMetrics.widthPixels;
////        int height = displayMetrics.heightPixels;
//                ImageView restImageView = (ImageView) findViewById(R.id.restImageView);
//                /* put Image*/
//
//                JSONObject object = new JSONObject(image);
//                String data = object.getString("data");
//                String[] imageStrs = data.substring(1, data.length() - 1).split(",");
//                imageDatas = new byte[imageStrs.length];
//                for (int i = 0; i < imageStrs.length; i++) {
//                    imageDatas[i] = DataUtils.intToByteArray(Integer.parseInt(imageStrs[i]));
//                }
//                Bitmap bmp = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
//                bmp = Bitmap.createScaledBitmap(bmp, width, (int) restImageView.getScaleY() * bmp.getHeight(), true);
//                restImageView.setImageBitmap(bmp);
//
//                restName.setText(name);
//                telTextView.setText(mobile);
//                addrTextView.setText(address);
//                lunchtimeTextView.setText(DataUtils.hoursFormatter(hours));
//                seatinfoTextView.setText("현재 잔여 좌석: " + availablePlaceNum + "석");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}


