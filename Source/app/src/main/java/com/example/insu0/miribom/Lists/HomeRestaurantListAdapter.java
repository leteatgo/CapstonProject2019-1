package com.example.insu0.miribom.Lists;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insu0.miribom.Data.DataUtils;
import com.example.insu0.miribom.R;
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

public class HomeRestaurantListAdapter extends RecyclerView.Adapter<HomeRestaurantListAdapter.ViewHolder> {

    private ArrayList<HomeRestaurantListItem> itemList;
    private Context context;
    private View.OnClickListener onClickItem;


    public HomeRestaurantListAdapter(Context context, ArrayList<HomeRestaurantListItem> itemList, View.OnClickListener onClickItem) {
        this.itemList = itemList;
        this.context = context;
        this.onClickItem = onClickItem;
    }


    @NonNull
    @Override
    public HomeRestaurantListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.main_restaurant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeRestaurantListItem item = itemList.get(position);

        holder.imageView.setImageBitmap(item.getResIcon());
        holder.textview.setText(item.getResName());
        holder.textview.setTag(item);
        holder.textview.setOnClickListener(onClickItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textview;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.main_restaurant_item_img);
            textview = itemView.findViewById(R.id.main_restaurant_item_title);
        }
    }


}
