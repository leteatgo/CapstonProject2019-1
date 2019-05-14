package com.example.insu0.miribom.Lists;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DateList extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.right = 1 ;
        }
    }
}
