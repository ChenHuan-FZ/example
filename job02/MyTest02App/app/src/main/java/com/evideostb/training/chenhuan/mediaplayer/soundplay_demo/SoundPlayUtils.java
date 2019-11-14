package com.evideostb.training.chenhuan.mediaplayer.soundplay_demo;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ChenHuan on 2018/2/5.
 */

public class SoundPlayUtils {
    private static final int MAXNUM = 10;
    private static SoundPool mSoundPool;
    private static Context mContext;
    private HashMap<Integer,Integer> mSoundMap = new HashMap<>();

    private static SoundPlayUtils soundplayUtils = null;
    public static SoundPlayUtils getInstance() {
        if (null == soundplayUtils) {
            soundplayUtils = new SoundPlayUtils();
        }
        return soundplayUtils;
    }

    public void init(Context context,List<SoundItem> items) {
        // 初始化声音
        mContext = context;

        //当前系统的SDK版本大于等于21(Android 5.0)时
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频数量
            builder.setMaxStreams(2);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            mSoundPool = builder.build();
        }
        //当系统的SDK版本小于21时
        else {//设置最多可容纳2个音频流，音频的品质为5
            mSoundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        }

        /**
         * 参数1:加载音乐流第多少个 (只用了俩个音乐) 2:设置音乐的质量 音乐流 3:资源的质量  0
         */
        int i = 0;
        for (SoundItem item : items) {
            if (i >= MAXNUM)
                break;
            mSoundMap.put(i++,mSoundPool.load(item.getPath(),1));
        }
    }

    /**
     * 播放声音
     *
     * @param soundID  设置声音
     */
    public void play(int soundID) {
        mSoundPool.play(mSoundMap.get(soundID), 1, 1, 0, 0, 1);
    }

}
