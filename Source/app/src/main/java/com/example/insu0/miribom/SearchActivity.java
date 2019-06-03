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

import com.example.insu0.miribom.Data.DataUtils;
import com.example.insu0.miribom.Lists.HomeRestaurantListItem;
import com.example.insu0.miribom.Lists.SearchAdapter;
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
import java.util.List;

//implemented by Insu Yang

public class SearchActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<String>();
    private List<String> list2 = new ArrayList<String>();
    private ListView listView;
    private SearchAdapter adapter;
    private SearchAdapter adapter2;
    private ArrayList<String> emptyList;
    private ArrayList<String> arrayList;
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
                list.add(allRestArray.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editSearch = (EditText)findViewById(R.id.editSearch);
        listView = (ListView)findViewById(R.id.listView);


        //검색에 사용할 리스트데이터 설정

        categorize();

        arrayList = new ArrayList<String>();
        emptyList = new ArrayList<String>();

        arrayList.addAll(list);
        emptyList.addAll(list2);

        adapter = new SearchAdapter(list, this);
        adapter2 = new SearchAdapter(emptyList, this);

//        listView.setAdapter(adapter);
        listView.setAdapter(adapter2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchActivity.this ,list2.get(position),Toast.LENGTH_LONG).show();
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                listView.setAdapter(adapter2);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.equals(null))
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
                }else {
                    listView.setAdapter(adapter);
                    String text = editSearch.getText().toString();
                    search(text);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(SearchActivity.this ,list.get(position),Toast.LENGTH_LONG).show();
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
                if(arrayList.get(i).toLowerCase().contains(charText)){
                    list.add(arrayList.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }


    private void categorize(){
        list2.add("주변 음식점 보러가기");
        list2.add("기다리지 않고 바로가기");
    }



}
