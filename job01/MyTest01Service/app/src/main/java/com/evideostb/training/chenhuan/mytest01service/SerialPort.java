package com.evideostb.training.chenhuan.mytest01service;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ChenHuan on 2018/1/26.
 */

public class SerialPort {
    static {
        System.loadLibrary("serial_commu");
    }
    public native FileDescriptor open(String path, int baudrate);
    public native void close();

    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private final String TAG = "SerialPort";

    /**
     * @param  device 串口文件路径
     * @param baudrate 波特率，不同设备波特率有区别
     * */
    public SerialPort(File device, int baudrate) throws SecurityException, IOException {
        Log.d(TAG,"open device" + device.getAbsolutePath());
        mFd = open(device.getAbsolutePath(), baudrate);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

}
