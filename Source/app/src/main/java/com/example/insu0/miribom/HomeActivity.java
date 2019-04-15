package com.example.insu0.miribom;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.insu0.miribom.Lists.HomeRestaurantList;
import com.example.insu0.miribom.Lists.HomeRestaurantListAdapter;
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
import java.util.ArrayList;

//implemented by Jason Yang

public class HomeActivity extends AppCompatActivity {
    private RecyclerView homeRestaurantListView;
    private HomeRestaurantListAdapter homeRestaurantListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*List 초기화*/
        homeRestaurantListView = findViewById(R.id.home_restaurant_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        homeRestaurantListView.setLayoutManager(layoutManager);

        setHomeRestaurantListTest(); //서버 구현 전 Testing Code
    }


    private void setHomeRestaurantListTest(){ //서버 구현 전 Testing Code

        ArrayList<String> itemList = new ArrayList<>();
        itemList.add("우뇽파스타");
        itemList.add("음식점2");
        itemList.add("음식점3");
        itemList.add("음식점4");
        itemList.add("음식점5");
        itemList.add("음식점6");

        homeRestaurantListAdapter = new HomeRestaurantListAdapter(this, itemList, onClickItem);
        homeRestaurantListView.setAdapter(homeRestaurantListAdapter);
        HomeRestaurantList homeRestaurantList = new HomeRestaurantList();
        homeRestaurantListView.addItemDecoration(homeRestaurantList);

    }

    private View.OnClickListener onClickItem = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            String str = (String) v.getTag();
            Toast.makeText(HomeActivity.this,str,Toast.LENGTH_SHORT).show();
            // 서버에서 레스토랑 정보 가져와서 RestaurantInfoActivity
            // params: url, restaurant no.
            new RestaurantInfoRequestTask().execute("http://" + MiribomInfo.ipAddress + "/restaurant/getRestInfo", "1");

        }
    };


    public class RestaurantInfoRequestTask extends AsyncTask<String, String, String> {
        String TAG = "RestaurantInfoRequestTask>>>";

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject reqInfo = new JSONObject();
                reqInfo.accumulate("resNo", strings[1]);
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
                Intent intent = new Intent(HomeActivity.this, RestaurantInfoActivity.class);
                JSONObject jsonObject = new JSONObject(result);
//                Toast.makeText(getApplicationContext(),"get JSON Object!!",Toast.LENGTH_LONG).show();
                intent.putExtra("resNo", jsonObject.getInt("no"));
                intent.putExtra("restName", jsonObject.getString("name"));
                intent.putExtra("address", jsonObject.getString("address"));
                intent.putExtra("mobile", jsonObject.getString("mobile"));
//                intent.putExtra("longitude", jsonObject.getString("longitude"));
//                intent.putExtra("latitude", jsonObject.getString("latitude"));
//                intent.putExtra("r_type", jsonObject.getString("r_type"));
//                intent.putExtra("s_type", jsonObject.getString("s_type"));
//                intent.putExtra("f_type", jsonObject.getString("f_type"));
//                intent.putExtra("r_num", jsonObject.getString("r_num"));
//                intent.putExtra("image", jsonObject.getString("image"));
                intent.putExtra("hours", jsonObject.getString("hours"));
//                intent.putExtra("owner_request", jsonObject.getString("owner_request"));
                intent.putExtra("totalPlaceNum", jsonObject.getInt("s_total_num"));
                intent.putExtra("availablePlaceNum", jsonObject.getInt("s_left_num"));
                intent.putExtra("reservablePlaceNum", jsonObject.getInt("s_ava_num"));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), "JSON 형식 아님", Toast.LENGTH_LONG).show();
            }
        }
    }

}
