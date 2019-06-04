package com.example.insu0.miribom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.insu0.miribom.Data.CatListViewItem;
import com.example.insu0.miribom.Data.DataUtils;
import com.example.insu0.miribom.Lists.HomeRestaurantListItem;
import com.example.insu0.miribom.Lists.SearchAdapter;
import com.example.insu0.miribom.Lists.SearchItem;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//implemented by Insu Yang

public class SearchActivity extends AppCompatActivity {

    private List<SearchItem> list = new ArrayList<SearchItem>();    //  no, name
    private List<SearchItem> list2 = new ArrayList<SearchItem>();
    private ListView listView;
    private SearchAdapter adapter;
    private SearchAdapter adapter2;
    private ArrayList<SearchItem> emptyList;
    private ArrayList<SearchItem> arrayList;
    private EditText editSearch;
    private JSONArray allRestArray;
    private int uno;
    private double lon, lat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        uno = getIntent().getIntExtra("uno", -1);
        lon = getIntent().getDoubleExtra("lon", -1);
        lat = getIntent().getDoubleExtra("lat", -1);

        String arrayStr = getIntent().getStringExtra("list");
        try {
            allRestArray = new JSONArray(arrayStr);
            for (int i = 0; i < allRestArray.length(); i++) {
                list.add(new SearchItem(allRestArray.getJSONObject(i).getInt("no"),allRestArray.getJSONObject(i).getString("name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editSearch = (EditText)findViewById(R.id.editSearch);
        listView = (ListView)findViewById(R.id.listView);


        //검색에 사용할 리스트데이터 설정

        categorize();

        arrayList = new ArrayList<SearchItem>();
        emptyList = new ArrayList<SearchItem>();

        arrayList.addAll(list);
        emptyList.addAll(list2);

        adapter = new SearchAdapter(list, this);
        adapter2 = new SearchAdapter(emptyList, this);

//        listView.setAdapter(adapter);
        listView.setAdapter(adapter2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchActivity.this ,list2.get(position).getName(),Toast.LENGTH_LONG).show();
                if (position == 0) {
                    Intent intent = new Intent(SearchActivity.this, CatSearchActivity.class);
                    intent.putExtra("c_type", position);
                    intent.putExtra("uno", uno);
                    intent.putExtra("lon", lon);
                    intent.putExtra("lat", lat);
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(SearchActivity.this, CatSearchActivity.class);
                    intent.putExtra("c_type", position);
                    intent.putExtra("uno", uno);
                    startActivity(intent);
                }

            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                listView.setAdapter(adapter2);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals(null))
                    Toast.makeText(SearchActivity.this, "None", Toast.LENGTH_SHORT).show();
                listView.setAdapter(adapter2);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    listView.setAdapter(adapter2);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Toast.makeText(SearchActivity.this ,list2.get(position),Toast.LENGTH_LONG).show();
                            if (position == 0) {
                                Intent intent = new Intent(SearchActivity.this, CatSearchActivity.class);
                                intent.putExtra("c_type", position);
                                intent.putExtra("uno", uno);
                                intent.putExtra("lon", lon);
                                intent.putExtra("lat", lat);
                                startActivity(intent);
                            }
                            if (position == 1) {
                                Intent intent = new Intent(SearchActivity.this, CatSearchActivity.class);
                                intent.putExtra("c_type", position);
                                intent.putExtra("uno", uno);
                                startActivity(intent);
                            }

                        }
                    });
                } else {    //  from EditText: keyword를 통한 검색
                    listView.setAdapter(adapter);
                    String text = editSearch.getText().toString();
                    search(text);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(SearchActivity.this , list.get(position).getName(),Toast.LENGTH_LONG).show();
                            new MakeHomeRestaurantItemFromSearch(list.get(position).getResNo()).execute("http://" + MiribomInfo.ipAddress + "/restaurant/search/info");
                        }
                    });
                }
            }
        });

    }

    public void search(String charText){
        list.clear();
        if(charText.length()==0){
            list.addAll(arrayList);
        }
        else{
            for(int i = 0;i<arrayList.size();i++){
                if(arrayList.get(i).getName().toLowerCase().contains(charText)){
                    list.add(arrayList.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void categorize(){
        list2.add(new SearchItem(-1,"주변 음식점 보러가기"));
        list2.add(new SearchItem(-1, "기다리지 않고 바로가기"));
    }


    /* CatListViewItem Click Event */
    public class MakeHomeRestaurantItemFromSearch extends AsyncTask<String, String, String> {
        private String TAG = "MakeHomeRestaurantItemFromSearch>>>";
        private int resNo;
        public MakeHomeRestaurantItemFromSearch(int resNo) {
            this.resNo = resNo;
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject reqInfo = new JSONObject();
            try {
                reqInfo.accumulate("resNo", resNo);
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
            Log.d(TAG, "onPostExecute: " + result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String image = jsonObject.getString("image");
                JSONObject object = new JSONObject(image);
                String data = object.getString("data");
                String[] imageStrs = data.substring(1, data.length() - 1).split(",");
                byte[] imageDatas = new byte[imageStrs.length];
                for (int i = 0; i < imageStrs.length; i++) {
                    imageDatas[i] = DataUtils.intToByteArray(Integer.parseInt(imageStrs[i]));
                }
                Bitmap bmp = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
                HomeRestaurantListItem hItem = new HomeRestaurantListItem(resNo, jsonObject.getString("name"), jsonObject.getString("address")
                        , jsonObject.getString("mobile"), bmp, jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude")
                        , jsonObject.getString("hours"), jsonObject.getString("owner_request"));
                Intent intent = new Intent(SearchActivity.this, ReserveActivity.class);
                intent.putExtra("ResItem", hItem);
                intent.putExtra("uNo", uno);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
