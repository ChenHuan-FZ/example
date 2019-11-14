package com.evideostb.training.chenhuan.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.evideostb.training.chenhuan.mediaplayer.audiorecorder_demo.AudioRecorderActivity;
import com.evideostb.training.chenhuan.mediaplayer.glsufaceview_demo.GLSufaceViewActivity;
import com.evideostb.training.chenhuan.mediaplayer.recyclerview_demo.RecyclerViewActivity;
import com.evideostb.training.chenhuan.mediaplayer.soundplay_demo.SoundPoolActivity;
import com.evideostb.training.chenhuan.mediaplayer.videomedia_demo.VideoMediaActivity;

public class MainActivity extends Activity {
    private String TAG = "MainActivity";
    private String[] strMenu={"视频播放demo", "音频播放demo", "图片列表demo", "音频录音demo", "3D动画demo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (MainActivity.this,android.R.layout.simple_list_item_1,strMenu);//把每一行内容的XML文件与数据给ArrayAdapter
        ListView listView=(ListView)findViewById(R.id.lv_menu);
        listView.setAdapter(adapter);//把ArrayAdapter给ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                onMenuClick(position);
            }
        });
    }

    private void onMenuClick(int position) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(this, VideoMediaActivity.class);
                break;
            case 1:
                intent.setClass(this, SoundPoolActivity.class);
                break;
            case 2:
                intent.setClass(this, RecyclerViewActivity.class);
                break;
            case 3:
                intent.setClass(this,AudioRecorderActivity.class);
                break;
            case 4:
                intent.setClass(this,GLSufaceViewActivity.class);
                break;
            default:
                return;
        }
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
