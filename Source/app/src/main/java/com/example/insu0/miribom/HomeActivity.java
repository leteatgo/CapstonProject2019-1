package com.example.insu0.miribom;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.insu0.miribom.Data.DataUtils;
import com.example.insu0.miribom.Lists.HomeRestaurantList;
import com.example.insu0.miribom.Lists.HomeRestaurantListAdapter;
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
import java.util.ArrayList;
import java.util.List;

// implemented by Jason Yang
// Updated by ckddn 090417

public class HomeActivity extends AppCompatActivity {
    private RecyclerView homeRestaurantListView;
    private HomeRestaurantListAdapter homeRestaurantListAdapter;

    private int uno;
    private double longitude, latitude;
    Button searchBtn;

    private ArrayList<HomeRestaurantListItem> itemList = new ArrayList<>();
    private JSONArray allRestArray;
    private List<String> allRestList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        uno = getIntent().getIntExtra("no", -1);
        if (uno == -1) {
            Toast.makeText(getApplicationContext(), "회원정보를 불러오는데 실패 하였습니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                intent.putExtra("uno", uno);
                intent.putExtra("list", allRestArray.toString());
                intent.putExtra("lon", location.getLongitude());
                intent.putExtra("lat", location.getLatitude());
                startActivity(intent);
            }
        });


        /*List 초기화*/
        homeRestaurantListView = findViewById(R.id.home_restaurant_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        homeRestaurantListView.setLayoutManager(layoutManager);

        setHomeRestaurantListTest(); //서버 구현 전 Testing Code

        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.d("Locations>>", "onCreate: " + longitude + ", " + latitude);
        }

        if (longitude > 0)
            new HomeSettingTask().execute("http://" + MiribomInfo.ipAddress + "/home/"
            , Integer.toString(uno), Double.toString(longitude), Double.toString(latitude));
        else
            new HomeSettingTask().execute("http://" + MiribomInfo.ipAddress + "/home/"
                    , Integer.toString(uno), "126", "37");

        new GetRestaurantALL().execute("http://" + MiribomInfo.ipAddress + "/home/search/all");
    }


    private void setHomeRestaurantListTest() { //서버 구현 전 Testing Code

        homeRestaurantListAdapter = new HomeRestaurantListAdapter(this, itemList, onClickItem);
        homeRestaurantListView.setAdapter(homeRestaurantListAdapter);
        HomeRestaurantList homeRestaurantList = new HomeRestaurantList();
        homeRestaurantListView.addItemDecoration(homeRestaurantList);

    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            HomeRestaurantListItem item = (HomeRestaurantListItem) v.getTag();
            Toast.makeText(getApplicationContext(), item.toString(), Toast.LENGTH_SHORT).show();
            // 서버에서 레스토랑 정보 가져와서 RestaurantInfoActivity
            // params: url, restaurant no
//            Intent intent = new Intent(HomeActivity.this, RestaurantInfoActivity.class);
            Intent intent = new Intent(HomeActivity.this, ReserveActivity.class);
            intent.putExtra("uNo", uno);
            intent.putExtra("resNo", item.getResNo());
            startActivity(intent);
        }
    };


    /* author ckddn
     * */
    public class HomeSettingTask extends AsyncTask<String, String, String> {
        String TAG = "HomeSettingTask>>>";

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject reqInfo = new JSONObject();
                reqInfo.accumulate("no", strings[1]);
                reqInfo.accumulate("longitude", strings[2]);
                reqInfo.accumulate("latitude", strings[3]);

                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    JSONObject imageObject = new JSONObject(jsonObject.getString("image"));
                    String data = imageObject.getString("data");
                    String[] imageStrs = data.substring(1, data.length()-1).split(",");
                    byte[] imageDatas = new byte[imageStrs.length];
                    for (int j = 0; j < imageStrs.length; j++) {
                        imageDatas[j] = DataUtils.intToByteArray(Integer.parseInt(imageStrs[j]));
                    }
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageDatas, 0 , imageDatas.length);
                    itemList.add(new HomeRestaurantListItem(jsonObject.getInt("no"), jsonObject.getString("name"), bmp));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            homeRestaurantListAdapter.notifyDataSetChanged();
        }
    }


    public class GetRestaurantALL extends AsyncTask<String, String, String> {
        String TAG = "GetRestaurantALL>>>";

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject reqInfo = new JSONObject();
                reqInfo.accumulate("uno", "1");

                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: "+ result);
            // 예약되었습니다. or 예약 등록에 실패 하였습니다.
            try {
                allRestArray = new JSONArray(result);
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "못가져옴", Toast.LENGTH_LONG).show();
            }
        }
    }

}
