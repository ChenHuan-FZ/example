package com.evideostb.training.chenhuan.mediaplayer.recyclerview_demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evideostb.training.chenhuan.mediaplayer.R;

import java.util.List;

/**
 * Created by ChenHuan on 2018/2/6.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<RecyclerViewSinger> mData;
    private Context mContext;

    /**
     * 事件回调监听
     */
    private RecyclerViewAdapter.OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(Context context, List data) {
        this.mData = data;
        this.mContext = context;
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(RecyclerViewAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 绑定数据
        holder.mTvName.setText(mData.get(position).getName());
        Glide.with(mContext).load(mData.get(position).getIcon())
                .placeholder(R.drawable.singer_image_default)
                .error(R.drawable.singer_image_default)
                .into(holder.mIvIcon);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvName;
        ImageView mIvIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
