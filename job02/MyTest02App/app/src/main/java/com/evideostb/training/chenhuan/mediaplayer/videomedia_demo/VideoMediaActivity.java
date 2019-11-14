package com.evideostb.training.chenhuan.mediaplayer.videomedia_demo;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.evideostb.training.chenhuan.mediaplayer.R;
import com.evideostb.training.chenhuan.mediaplayer.utils.SimpleDateFormatUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ChenHuan on 2018/2/1.
 */

public class VideoMediaActivity extends Activity
    implements View.OnClickListener
{
    private Button bt_play;
    private Button bt_pause;
    private Button bt_stop;
    private Button bt_soundAdd;
    private Button bt_soundReduce;
    private SeekBar sb_process;
    private MediaPlayer mPlayer;
    private SurfaceView surfaceView;
    private int postion;
    private final String VIDEO_PATH = "/sdcard/Movies/test.mp4";
    private boolean isPrepare = false;
    private TextView tv_currentTime,tv_totalTime,tv_currentVolume;
    private final Float VOLUME_UNIT = 1/20F;
    private Float currentVolume = 0.5F;
    private TimerTask mProgressTask ;
    private Timer mProgressTimer;
    private boolean isDragProgress = false;
    private String TAG = "VideoMediaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        bt_play = (Button)findViewById(R.id.bt_play_pause);
        bt_soundAdd = (Button)findViewById(R.id.bt_add);
        bt_soundReduce = (Button)findViewById(R.id.bt_reduce);
        sb_process = (SeekBar)findViewById(R.id.sb_play);

        tv_currentTime = (TextView)findViewById(R.id.tv_curtime);
        tv_totalTime = (TextView)findViewById(R.id.tv_totaltime);
        tv_currentVolume = (TextView)findViewById(R.id.tv_volume);

        bt_play.setOnClickListener(this);
        bt_soundAdd.setOnClickListener(this);
        bt_soundReduce.setOnClickListener(this);

        //创建MediaPlayer
        mPlayer = new MediaPlayer();
        surfaceView = (SurfaceView)findViewById(R.id.sf_view_video);
        // 设置播放时打开屏幕
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceListener());

        initView();
        prepareVideo();
        initMediaPlayerListener();
    }

    private void initView() {
        tv_currentVolume.setText(String.valueOf((int)(currentVolume/VOLUME_UNIT)));
        mProgressTask = new TimerTask() {
            @Override
            public void run() {
                if (isDragProgress){
                    return;
                }
                Log.d(TAG,"in timertask");
                sb_process.setProgress(mPlayer.getCurrentPosition());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_currentTime.setText(SimpleDateFormatUtil.formatTime(mPlayer.getCurrentPosition()));
                    }
                });
            }
        };
    }

    /**
     * 初始化MediaPlayer监听
     */
    private void initMediaPlayerListener(){
        //数据源准备好了监听
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepare = true;
                //设置最大时长
                sb_process.setMax(mp.getDuration());
                tv_totalTime.setText(SimpleDateFormatUtil.formatTime(mp.getDuration()));
                //开启定时器刷新进度条
                mProgressTimer = new Timer();
                mProgressTimer.schedule(mProgressTask,0,100);
                initSeekBarListener();
            }
        });
        //播放完成监听
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        //出错监听
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

    /**
     * 初始化进度条拖动监听
     */
    private void initSeekBarListener(){
        sb_process.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isDragProgress = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isDragProgress = false;
                mPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.bt_play_pause:
                    if (!mPlayer.isPlaying()) {
                        play();
                    } else {
                        pause();
                    }
                    break;
                case R.id.bt_add:
                    upVolume();
                    break;
                case R.id.bt_reduce:
                    downVolume();
                    break;
                default:
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void prepareVideo() {
        try {
            mPlayer.reset();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频
            mPlayer.setDataSource(VIDEO_PATH);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void play() {
        if (mPlayer != null && isPrepare){
            mPlayer.start();
        }
    }

    private void pause() {
        if (mPlayer != null && mPlayer.isPlaying()){
            mPlayer.pause();
        }
    }
    /**
     * 增大音量
     */
    private void upVolume(){
        currentVolume += VOLUME_UNIT;
        if (currentVolume > 1F){
            currentVolume = 1F;
        }
        mPlayer.setVolume(Float.valueOf(currentVolume),Float.valueOf(currentVolume));
        tv_currentVolume.setText(String.valueOf((int)(currentVolume/VOLUME_UNIT)));
    }
    /**
     * 减小音量
     */
    private void downVolume(){
        currentVolume -= VOLUME_UNIT;
        if (currentVolume < 0F){
            currentVolume = 0F;
        }
        mPlayer.setVolume(Float.valueOf(currentVolume),Float.valueOf(currentVolume));
        tv_currentVolume.setText(String.valueOf((int)(currentVolume/VOLUME_UNIT)));
    }

    private class SurfaceListener implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (mPlayer != null){
                mPlayer.setDisplay(holder);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    @Override
    protected void onDestroy() {
        if (mProgressTimer != null) {
            mProgressTimer.cancel();
        }
        mProgressTask.cancel();
        mProgressTimer = null;
        if (mPlayer != null){
            mPlayer.release();
        }
        super.onDestroy();
    }
}

