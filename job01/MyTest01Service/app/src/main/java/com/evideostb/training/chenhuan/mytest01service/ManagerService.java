package com.evideostb.training.chenhuan.mytest01service;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.evideostb.training.chenhuan.aidl.IOutDataStream;
import com.evideostb.training.chenhuan.aidl.ISerialConfigParams;
import com.evideostb.training.chenhuan.aidl.OutDataStream;
import com.evideostb.training.chenhuan.aidl.SerialConfigParams;

import static java.lang.Thread.sleep;

/**
 * Created by ChenHuan on 2018/1/30.
 */

public class ManagerService extends Service {

    private Thread mOutputThread ;

    private Handler handler = new Handler();
    private String TAG = "ManagerService";
    private SerialConfigParams mSerialConfig;
    private SerialPortUtil mSerialPortUtil;

    private final RemoteCallbackList<IOutDataStream> mCallbacks = new RemoteCallbackList<IOutDataStream>();
    private ISerialConfigParams.Stub managerImp = new ISerialConfigParams.Stub() {
        @Override
        public void setConfigParams(SerialConfigParams serailConfig) throws RemoteException {
            mSerialConfig = serailConfig;
            //获得参数后请求Jni监听数据
            Log.d(TAG,"in setConfigParams");
            Log.d(TAG,mSerialConfig.getPortName());
            Log.d(TAG,mSerialConfig.getBaudRate());
            mSerialPortUtil = SerialPortUtil.getInstance();
            mSerialPortUtil.setPath(mSerialConfig.getPortName());
            mSerialPortUtil.init();
            mSerialPortUtil.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
                @Override
                public void onDataReceive(byte[] buffer, int size) {
                    //构建实体类
                    String data = new String(buffer,0,size);
                    OutDataStream output = new OutDataStream();
                    output.setData(data);
                    output.setTime(String.valueOf(System.currentTimeMillis()));
                    //回调给aidl监听
                    callback(output);
                    //传递给主界面更新数据
                    refreshFloatView(output.getData());
                    Log.d(TAG," DataReceive:" + new String(buffer,0,size));
                }
            });
        }

        @Override
        public SerialConfigParams getConfigParams() throws RemoteException {
            return mSerialConfig;
        }

        @Override
        public void registerCallback(IOutDataStream callback) throws RemoteException {
            if (callback != null){
                mCallbacks.register(callback);
            }
        }

        @Override
        public void unregisterCallback(IOutDataStream callback) throws RemoteException {
            if (callback != null){
                mCallbacks.unregister(callback);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent)
    {
        return managerImp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        initData();
//        SerialPortTest();
        createFloatView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFloatView();
    }

    /**
     * 刷新悬浮框
     * @param data 悬浮框显示的数据
     */
    private void refreshFloatView(final String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                WindowViewManager.updateData(data);
            }
        });
    }

    /**
     * 创建悬浮框
     */
    private void createFloatView(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                WindowViewManager.createSmallWindow(getApplicationContext());
            }
        });
    }

    /**
     * 移除悬浮框
     */
    private void removeFloatView(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                WindowViewManager.removeSmallWindow(getApplicationContext());
            }
        });
    }


    /**
     * 数据回调给接收者
     * @param data 数据
     */
    private void callback(OutDataStream data) {
        final int N = mCallbacks.beginBroadcast();
        for (int i=0; i<N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).GetOutData(data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallbacks.finishBroadcast();
    }

    private void initData(){
        mOutputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"initData");
                Integer i = 10;
                while (true){
                    Log.i(TAG,"i:%d" + i);
                    OutDataStream output = new OutDataStream();
                    output.setData("0x"+i);
                    output.setTime(System.currentTimeMillis()+"");

                    //回调给接收者
                    callback(output);

                    refreshFloatView(output.getData());
                    i++;
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mOutputThread.start(); //启动线程
    }

    public void SerialPortTest() {

        Log.d(TAG,"in setConfigParams");
        mSerialPortUtil = SerialPortUtil.getInstance();
        mSerialPortUtil.init();
        mSerialPortUtil.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                Log.d(TAG,"SerialPortTest");
                //构建实体类
                String data = new String(buffer,0,size);
                OutDataStream output = new OutDataStream();
                output.setData(data);
                output.setTime(String.valueOf(System.currentTimeMillis()));
                //回调给aidl监听
                callback(output);
                //传递给主界面更新数据
                refreshFloatView(output.getData());
                Log.d(TAG," DataReceive:" + new String(buffer,0,size));
            }
        });
    }
}
