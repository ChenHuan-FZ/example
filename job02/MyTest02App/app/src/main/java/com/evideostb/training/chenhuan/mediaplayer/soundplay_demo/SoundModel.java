package com.evideostb.training.chenhuan.mediaplayer.soundplay_demo;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by ChenHuan on 2018/2/5.
 */

public class SoundModel implements SoundLoader.OnMusicLoadListener{
    private String TAG = "SoundModel";
    private static final String MUSIC_DIR = "/sdcard/Music";
//    private static final String MUSIC_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/Music/";
//    private static final String MUSIC_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/music/";
    private static SoundModel ourInstance;
    private Context mContext;
    //数据内容
    private SoundDatas mMusicDatas;

    //回调
    private SoundLoader.OnMusicLoadListener onMusicLoadListener;

    public static SoundModel getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SoundModel(context);
        }
        return ourInstance;
    }

    private SoundModel(Context context) {
        mContext = context.getApplicationContext();
        mMusicDatas = new SoundDatas();
    }

    public void load(final SoundLoader.OnMusicLoadListener listener) {
        Log.d(TAG,MUSIC_DIR);
        listener.onLoading();//告诉MainActivit正在加载中
        onMusicLoadListener = listener;
        new SoundLoader().load(MUSIC_DIR, this);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(List<SoundItem> items) {
        mMusicDatas.mDatas = items;
        onMusicLoadListener.onLoaded(mMusicDatas.mDatas);
    }
}
