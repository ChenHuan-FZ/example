package com.evideostb.training.chenhuan.mytest01service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.evideostb.training.chenhuan.mytest01service.R;

public class MyTest01Service extends Activity implements View.OnClickListener {

    static {
        System.loadLibrary("serial_commu");
    }

    //声明一个本地方法，用native关键字修饰
    public native String getStringFromNative();

    private TextView tv_data;
    private EditText et_input;
    private Button bt_write;
    private String TAG = "MyTest01Service";
    private boolean mbStop=true;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加載布局文件
//        setContentView(R.layout.activity_my_test01);
//        initView();
        startService();
        Log.d(TAG,getStringFromNative());
    }

    private void initView(){
        tv_data = (TextView) findViewById(R.id.tv_data);
        et_input = (EditText) findViewById(R.id.et_input);
        bt_write = (Button) findViewById(R.id.bt_write);
        bt_write.setOnClickListener(this) ;
    }

    private void startService(){
        Intent intent = new Intent(this,ManagerService.class);
        startService(intent);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_write:
                writeToSerial(et_input.getText().toString());
                break;
            default:
                break;
        }
    }

    /**
     * 写数据到串口
     * @param data
     */
    private void writeToSerial(String data){
        SerialPortUtil.getInstance().sendCmds(data);
    }

}
