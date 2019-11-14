package com.evideostb.training.chenhuan.mediaplayer.recyclerview_demo;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ChenHuan on 2018/2/6.
 */

public class GridSpaceDecoration extends RecyclerView.ItemDecoration{
    private int space;

    public GridSpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildLayoutPosition(view) != 0)
            outRect.left = space;
    }
}
