package com.evideostb.training.chenhuan.mediaplayer.utils;

import android.util.Log;

public class LogUtil {
    private static final String TAG = "MediaPlayer";

    public static final void d(String msg) {
        Log.d(TAG, msg);
    }

    public static final void w(String msg) {
        Log.w(TAG, msg);
    }

    public static final void e(String msg) {
        Log.e(TAG, msg);
    }

}
