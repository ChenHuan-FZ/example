package com.evideostb.training.chenhuan.mediaplayer.glsufaceview_demo;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class GLSufaceViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setRenderer(new GLsufaceViewRenderer(false));
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {

        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
        mGLSurfaceView.onPause();
    }

    private GLSurfaceView mGLSurfaceView;
}
