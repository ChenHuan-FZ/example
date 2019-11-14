package com.evideostb.training.chenhuan.mediaplayer.audiorecorder_demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.evideostb.training.chenhuan.mediaplayer.R;

/**
 * Created by ChenHuan on 2018/2/7.
 */

public class AudioRecorderActivity extends Activity implements View.OnClickListener{
    private String TAG = "AudioRecorderActivity";
    private LineWaveVoiceView mHorVoiceView;
    private Button mStartRecord;
    private Button mStopRecord;
    private Button mPlayRecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initView();
    }

    private void initView() {
        mHorVoiceView = (LineWaveVoiceView) findViewById(R.id.horvoiceview);
        mStartRecord = (Button)findViewById(R.id.btn_start_record);
        mStopRecord = (Button)findViewById(R.id.btn_stop_record);
        mPlayRecord = (Button)findViewById(R.id.btn_play_record);

        mStartRecord.setOnClickListener(this);
        mStopRecord.setOnClickListener(this);
        mPlayRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_record:
                play(AudioFileFunc.getWavFilePath());
                break;
            case R.id.btn_stop_record:
                stop();
                break;
            case R.id.btn_start_record:
                record();
                break;
            default:
                break;
        }
    }

    private void record() {
        mHorVoiceView.startRecord();
        AudioRecordManager.getInstance().startRecordAndFile();
    }

    private void stop() {
        AudioRecordManager.getInstance().stopRecordAndFile();
    }

    private void play(String path) {
        path = "/sdcard/FinalAudio.wav";
        AudioRecordManager.getInstance().playRecord(path);
        Log.d(TAG,path);
    }

}
