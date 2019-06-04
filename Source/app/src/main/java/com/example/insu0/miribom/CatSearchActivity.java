package com.example.insu0.miribom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insu0.miribom.Data.CatListViewAdapter;
import com.example.insu0.miribom.Data.CatListViewItem;
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
import java.util.Comparator;
import java.util.List;

//implemented by Yang Insu
public class CatSearchActivity extends AppCompatActivity {

    private TextView typeTextView;
    private ListView catlistview;
    private List<CatListViewItem> list;
    private CatListViewAdapter catListViewAdapter;
    private int uno;
    private int f_type; // food type     ( from HomeActivity )
    private int c_type; // category type ( from SearchActivity)
    private double lon, lat;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_search);
        uno = getIntent().getIntExtra("uno", -1);
        f_type = getIntent().getIntExtra("f_type", -1);
        c_type = getIntent().getIntExtra("c_type", -1);
        lon = getIntent().getDoubleExtra("lon", -1);
        lat = getIntent().getDoubleExtra("lat", -1);


        catListViewAdapter = new CatListViewAdapter(this, onClickItem);

        catlistview = findViewById(R.id.catlistView);
        catlistview.setAdapter(catListViewAdapter);

        typeTextView = (TextView) findViewById(R.id.typeTextView);
        String typeStr = "";
        if (f_type == -1) {
            if (c_type == 0) {  //  주변 음식점 보러가기
                typeStr = "가까운 음식점 순";
                nearCatListInit();
            } else if (c_type == 1) { // 기다리지 않고 바로가기
                typeStr = "자리있는 음식점 순";
                seatCatListInit();
            }
        } else {
            if (f_type == 1) {
                typeStr  = "한 식";
            } else if (f_type == 2) {
                typeStr  = "중 식";
            } else if (f_type == 3) {
                typeStr  = "일 식";
            } else if (f_type == 4) {
                typeStr  = "양 식";
            }
            foodCatListInit();
        }

        typeTextView.setText(typeStr );


    }

    private void nearCatListInit() {
        new GetRestaurantDistance().execute("http://" + MiribomInfo.ipAddress + "/home/search/distance", Double.toString(lon), Double.toString(lat), Integer.toString(3));
    }
    private void seatCatListInit() {
        new GetRestaurantRemains().execute("http://" + MiribomInfo.ipAddress + "/home/search/remains");
    }

    private void foodCatListInit(){
        new CategorySearchTask().execute("http://" + MiribomInfo.ipAddress + "/home/search/category/" + f_type ,Double.toString(lon), Double.toString(lat));
    }


    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CatListViewItem item = (CatListViewItem) v.getTag();
            Toast.makeText(getApplicationContext(), item.getCatResName(), Toast.LENGTH_LONG).show();
            new MakeHomeRestaurantItem(item).execute("http://" + MiribomInfo.ipAddress + "/restaurant/cat/info", Integer.toString(item.getResNo()));
        }
    };
    /*  For Category Search */
    public class CategorySearchTask extends AsyncTask<String, String, String> {
        String TAG = "CategorySearchTask>>>";

        @Override
        protected String doInBackground(String... strings) {
            JSONObject reqInfo = new JSONObject();
            try {
                reqInfo.accumulate("longitude", strings[1]);
                reqInfo.accumulate("latitude", strings[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: " + result);
            list = new ArrayList<>();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    int no = object.getInt("no");
                    String name = object.getString("name");
                    String imageUrl = object.getString("image");
                    String address = object.getString("address");
                    double distance = object.getDouble("distance");
                    list.add(new CatListViewItem(no, imageUrl, name, address, distance, catListViewAdapter));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.sort(new Comparator<CatListViewItem>() {
                @Override
                public int compare(CatListViewItem o1, CatListViewItem o2) {
                    if (o1.getDistance() - o2.getDistance() > 0)
                        return 1;
                    else
                        return -1;
                }
            });
            for (int i = 0; i < list.size(); i++) {
                catListViewAdapter.addItem(list.get(i));
                catListViewAdapter.notifyDataSetChanged();
            }
        }
    }



    public class GetRestaurantDistance extends AsyncTask<String, String, String> {
        String TAG = "GetRestaurantDistance>>>";

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject reqInfo = new JSONObject();
                reqInfo.accumulate("longitude", strings[1]);
                reqInfo.accumulate("latitude", strings[2]);
                reqInfo.accumulate("distance", strings[3]);

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
            list = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    int no = object.getInt("no");
                    String name = object.getString("name");
                    String imageUrl = object.getString("image");
                    String address = object.getString("address");
                    double distance = object.getDouble("distance");
                    list.add(new CatListViewItem(no, imageUrl, name, address, distance, catListViewAdapter));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.sort(new Comparator<CatListViewItem>() {
                @Override
                public int compare(CatListViewItem o1, CatListViewItem o2) {
                    if (o1.getDistance() - o2.getDistance() > 0)
                        return 1;
                    else
                        return -1;
                }
            });
            for (int i = 0; i < list.size(); i++) {
                catListViewAdapter.addItem(list.get(i));
                catListViewAdapter.notifyDataSetChanged();
            }

        }
    }

    public class GetRestaurantRemains extends AsyncTask<String, String, String> {
        String TAG = "GetRestaurantRemains>>>";

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
            list = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    int no = object.getInt("no");
                    String name = object.getString("name");
                    String imageUrl = object.getString("image");
                    String address = object.getString("address");
                    int leftNum = object.getInt("remains");
                    if (leftNum > 0)    //  잔여좌석 존재시
                        list.add(new CatListViewItem(no, imageUrl, name, address, leftNum, catListViewAdapter));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.sort(new Comparator<CatListViewItem>() {
                @Override
                public int compare(CatListViewItem o1, CatListViewItem o2) {
                    return o2.getCatLeftSeatNum() - o1.getCatLeftSeatNum();
                }
            });
            for (int i = 0; i < list.size(); i++) {
                catListViewAdapter.addItem(list.get(i));
                catListViewAdapter.notifyDataSetChanged();
            }
        }
    }


    /* CatListViewItem Click Event */
    public class MakeHomeRestaurantItem extends AsyncTask<String, String, String> {
        private String TAG = "MakeHomeRestaurantItem>>>";
        private CatListViewItem item;

        public MakeHomeRestaurantItem(CatListViewItem item) {
            this.item = item;
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject reqInfo = new JSONObject();
            try {
                reqInfo.accumulate("resNo", strings[1]);
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
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                HomeRestaurantListItem hItem = new HomeRestaurantListItem(item.getResNo(), item.getCatResName(), item.getCatResAddress(), jsonObject.getString("mobile"),
                        item.getCatResImg(), jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"), jsonObject.getString("hours"), jsonObject.getString("owner_request"));
                Intent intent = new Intent(CatSearchActivity.this, ReserveActivity.class);
                intent.putExtra("ResItem", hItem);
                intent.putExtra("uNo", uno);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
