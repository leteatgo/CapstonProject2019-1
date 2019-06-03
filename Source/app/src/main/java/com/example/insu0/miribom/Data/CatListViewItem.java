package com.example.insu0.miribom.Data;

import android.graphics.Bitmap;
import android.widget.ImageView;

//implemented by Insu Yang
public class CatListViewItem {
    private Bitmap catResImg;
    private String catResName;
    private String catResDesc;

    public void setCatResImage(Bitmap image){
        catResImg = image;
    }

    public void setCatResName(String title){
        catResName = title;
    }
    public void setCatResDesc(String desc){
        catResDesc = desc;
    }

    public Bitmap getCatResImg(){
        return this.catResImg;
    }

    public String getCatResName(){
        return this.catResName;
    }

    public String getCatResDesc(){
        return this.catResDesc;
    }
}
