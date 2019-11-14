package com.evideostb.training.chenhuan.mediaplayer.soundplay_demo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evideostb.training.chenhuan.mediaplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHuan on 2018/2/5.
 */

public class SoundListAdapter extends BaseAdapter {
    private List<SoundItem> mItems;
    private Context mContext;
    private int cur_pos = -1;

    public SoundListAdapter(Context context) {
        mItems = new ArrayList<>();
        mContext = context;
    }

    public void set(List<SoundItem> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void delete(SoundItem item) {
        mItems.remove(item);
    }

    public void setCurPos(int xPos) {
        cur_pos = xPos;
    }

    public int getCurPos() {return cur_pos;}

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public SoundItem getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_sp_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        if (position == cur_pos) {// 如果当前的行就是ListView中选中的一行，就更改显示样式
            updateChooseLineTextColor(getItem(position), (ViewHolder) convertView.getTag());
            convertView.setBackgroundColor(Color.LTGRAY);// 更改整行的背景色
        }
        return convertView;
    }

    private void initializeViews(SoundItem object, ViewHolder holder) {
        holder.musicTitle.setText(object.getFileName());
    }

    private void updateChooseLineTextColor(SoundItem object, ViewHolder holder) {
        holder.musicTitle.setTextColor(Color.RED);// 更改字体颜色
    }

    protected class ViewHolder {
        private TextView musicTitle;

        public ViewHolder(View view) {
            musicTitle = (TextView) view.findViewById(R.id.tx_name);
        }
    }
}
