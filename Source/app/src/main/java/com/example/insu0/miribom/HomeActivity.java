package com.example.insu0.miribom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.insu0.miribom.Lists.HomeRestaurantList;
import com.example.insu0.miribom.Lists.HomeRestaurantListAdapter;

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
        itemList.add("음식점1");
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
        }
    };

}
