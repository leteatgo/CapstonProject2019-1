package com.example.insu0.miribom;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.insu0.miribom.Data.CatListViewAdapter;

//implemented by Yang Insu
public class CatSearchActivity extends AppCompatActivity {

    ListView catlistview;
    CatListViewAdapter catListViewAdapter;
    //Receive Object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_search);

        catListViewAdapter = new CatListViewAdapter();

        catlistview = findViewById(R.id.catlistView);
        catlistview.setAdapter(catListViewAdapter);

        init();

    }

    public void init(){
        catListViewAdapter.addItem(getResources().get);
        catListViewAdapter.addItem(new Bitm);
        catListViewAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.chinese),"Chinese Restaurant", "very tasty chinese cusine");
    }
}
