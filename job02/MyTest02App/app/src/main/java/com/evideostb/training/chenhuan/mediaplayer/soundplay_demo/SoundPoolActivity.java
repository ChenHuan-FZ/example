package com.evideostb.training.chenhuan.mediaplayer.soundplay_demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.evideostb.training.chenhuan.mediaplayer.R;

import java.util.List;

/**
 * Created by ChenHuan on 2018/2/5.
 */

public class SoundPoolActivity extends Activity
    implements View.OnClickListener, SoundLoader.OnMusicLoadListener
{
    private SoundListAdapter mAdapter;
    private ListView mSoundListView;

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SoundItem item = mAdapter.getItem(position);
            SoundPlayUtils.getInstance().play(item.getId());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundpool);

        mSoundListView = (ListView) findViewById(R.id.lv_soundpool);
        mAdapter = new SoundListAdapter(this);
        mSoundListView.setAdapter(mAdapter);
        mSoundListView.setOnItemClickListener(onItemClickListener);

        SoundModel.getInstance(this).load(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(List<SoundItem> items) {
        SoundPlayUtils.getInstance().init(this,items);
        // 更新list数据
        mAdapter.set(items);
    }
}
