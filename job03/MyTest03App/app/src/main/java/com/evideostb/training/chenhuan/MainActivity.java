package com.evideostb.training.chenhuan;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.TimerTask;

public class MainActivity extends Activity implements DialogFragment.NoticeDialogListener{

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
//            return null;
        }
    };

    private AsyncTask<Void,Void,Void> mInits = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_1);
        setDefaultFrame();
    }

    private void setDefaultFrame() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        FragmentActivity mWeixin = new FragmentActivity();
        transaction.add(R.id.fragme, mWeixin);
        transaction.addToBackStack(null);//回退
        transaction.commit();

    }

    private void myInits() {
        mInits.execute();
    }

    private void greateMyTimer() {

    }

    @Override
    public void onDialogPositiveClick() {
        Log.d("T","test");
    }
}
