package com.evideostb.training.chenhuan.mytest01activity;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.evideostb.training.chenhuan.aidl.IOutDataStream;
import com.evideostb.training.chenhuan.aidl.ISerialConfigParams;
import com.evideostb.training.chenhuan.aidl.OutDataStream;
import com.evideostb.training.chenhuan.mytest01activity.R;
import com.evideostb.training.chenhuan.aidl.SerialConfigParams;

import java.util.ArrayList;
import java.util.List;

public class MyTest01Activity extends Activity implements View.OnClickListener{
    private final String SERVICE_ACTION = "com.evideostb.training.chenhuan.aidl";
    private final String SERVICE_PACKAGE = "com.evideostb.training.chenhuan.mytest01service";
    private ISerialConfigParams managerImp;
    private Spinner sp_baudRate;
    private Spinner sp_dataBit;
    private Spinner sp_stopBit;
    private Spinner sp_checkBit;
    private EditText et_portName;
    private TextView mRecvMsgTextView;
    private Button btStartListent;
    private Button btStopListent;
    private TextView et_totalStr;
    private boolean isConnect;
    private String TAG = "MyTest01Activity";
    private IOutDataStream mCallBack;
    private List<DataCell> dataList = new ArrayList<>();
    private String totalStr;
    private static int nCount = 0;

    /**
     * 链接
     * */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onServiceConnected");
            isConnect = true;
            managerImp = ISerialConfigParams.Stub.asInterface(service);
            setConfiguration();
            registerDataCallBack();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisconnected");
            isConnect = false;
            managerImp = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test01);
        initView();
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.runlistening:
                if (isConnect) {
                    setConfiguration();
                } else {
                    attemptConnectService();
                }
                Toast.makeText(this, "button1.click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stoplistening:
                if (isConnect) {
                    attemptDisconnectService();
                }
                Toast.makeText(this, "button2.click", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化事件绑定
     */
    private void initView(){
        totalStr = getResources().getString(R.string.totalStr);
        et_totalStr = (TextView) findViewById(R.id.serialvaluetext);
        sp_baudRate = (Spinner) findViewById(R.id.spinnerbaudrate);
        sp_dataBit = (Spinner) findViewById(R.id.spinnerdatabit);
        sp_stopBit = (Spinner) findViewById(R.id.spinnerstopbit);
        sp_checkBit = (Spinner) findViewById(R.id.spinnerparitybit);
        et_portName = (EditText) findViewById(R.id.editserialname);
        mRecvMsgTextView = (TextView)findViewById(R.id.recvMsgTextView);
        btStartListent = (Button)findViewById(R.id.runlistening);
        btStopListent = (Button)findViewById(R.id.stoplistening);
        btStartListent.setOnClickListener(this);
        btStopListent.setOnClickListener(this);
        sp_dataBit.setSelection(3);
        et_portName.setText("/dev/ttyS1");
        String temp = String.format(totalStr, dataList.size());
        et_totalStr.setText(temp);
    }

    /**
     * 设置参数
     */
    private void setConfiguration(){
        SerialConfigParams configuration = new SerialConfigParams();
        configuration.setPortName(et_portName.getText().toString());
        configuration.setBaudRate(sp_baudRate.getSelectedItem().toString());
        configuration.setDataBit(sp_dataBit.getSelectedItem().toString());
        configuration.setStopBit(sp_stopBit.getSelectedItem().toString());
        configuration.setCheckBit(sp_checkBit.getSelectedItem().toString());
        try {
            managerImp.setConfigParams(configuration);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册数据回调监听
     */
    private void registerDataCallBack(){
        mCallBack = new IOutDataStream.Stub(){
            @Override
            public void GetOutData(OutDataStream output) throws RemoteException {
                Log.d(TAG, "GetOutData");
                Log.d(TAG, output.getTime());
                Log.d(TAG, output.getData());
                if (output != null) {
                    DataCell listBean = new DataCell();
                    listBean.setTime(output.getTime());
                    listBean.setData(output.getData());
                    dataList.add(listBean);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int nListSize = dataList.size();
                            String temp = String.format(totalStr, nListSize);
                            et_totalStr.setText(temp);
                            Log.i(TAG,"nListSize:"+nListSize);
                            String text = Utils.transformDate(dataList.get(nListSize-1).getTime())
                                    + " " + dataList.get(nListSize-1).getData();
                            if (nCount >= 3) {
                                mRecvMsgTextView.setText(null);
                                nCount = 0;
                            }
                            mRecvMsgTextView.append(text);
                            mRecvMsgTextView.append("\n");
                            nCount = nCount + 1;
                        }
                    });
                }
            }
        };
        try {
            managerImp.registerCallback(mCallBack);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注销数据监听
     */
    private void unregisterDataCallback(){
        try {
            managerImp.unregisterCallback(mCallBack);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试链接到输出服务器
     */
    private void attemptConnectService(){
        Log.d(TAG,"attemptConnectService");
        Intent intent = new Intent();
        intent.setAction(SERVICE_ACTION);
        intent.setPackage(SERVICE_PACKAGE);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 断开服务器链接
     */
    private void attemptDisconnectService(){
        if (isConnect){
            unregisterDataCallback();
            unbindService(mServiceConnection);
            isConnect = false;
        }
    }

    @Override
    protected void onDestroy() {
        attemptDisconnectService();
        super.onDestroy();
    }
}
