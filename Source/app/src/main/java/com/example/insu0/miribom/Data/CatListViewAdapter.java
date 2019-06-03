package com.example.insu0.miribom.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.insu0.miribom.R;

import java.util.ArrayList;

//implemented by Insu Yang
public class CatListViewAdapter extends BaseAdapter {

    private ArrayList<CatListViewItem> catListViewItemList = new ArrayList<CatListViewItem>();
    private Context context;
    private View.OnClickListener onClickItem;

    public CatListViewAdapter(Context context, View.OnClickListener onClickItem){
        this.context = context;
        this.onClickItem = onClickItem;
    }


    @Override
    public int getCount() {
        return catListViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return catListViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.catlistview_item,parent,false);
        }

        ImageView catRestImageView = convertView.findViewById(R.id.cat_res_img);
        TextView catRestTitle = convertView.findViewById(R.id.catsearch_restName);
        TextView catRestDesc = convertView.findViewById(R.id.catsearch_restDesc);

        CatListViewItem catListViewItem = catListViewItemList.get(position);

        catRestImageView.setTag(catListViewItem);
        catRestImageView.setOnClickListener(onClickItem);
        catRestTitle.setTag(catListViewItem);
        catRestTitle.setOnClickListener(onClickItem);
        catRestDesc.setTag(catListViewItem);
        catRestDesc.setOnClickListener(onClickItem);

        catRestImageView.setImageBitmap(catListViewItem.getCatResImg());
        catRestTitle.setText(catListViewItem.getCatResName());
        catRestDesc.setText(catListViewItem.getCatResDest());

        return convertView;
    }

    public void addItem(CatListViewItem item){
        catListViewItemList.add(item);
    }
}
