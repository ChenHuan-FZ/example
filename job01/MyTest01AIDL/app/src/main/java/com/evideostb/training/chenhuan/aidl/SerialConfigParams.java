package com.evideostb.training.chenhuan.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ChenHuan on 2018/1/29.
 */

public class SerialConfigParams implements Parcelable {
    private String mSerialPort;
    private String mBaudRate;
    private String mDataBits;
    private String mStopBits;
    private String mParityBits;

    public SerialConfigParams() {
    }

    protected SerialConfigParams(Parcel in) {
        this.mSerialPort = in.readString();
        this.mBaudRate = in.readString();
        this.mDataBits = in.readString();
        this.mStopBits = in.readString();
        this.mParityBits = in.readString();
    }

    public String getPortName() {
        return mSerialPort;
    }

    public void setPortName(String portName) {
        mSerialPort = portName;
    }

    public String getBaudRate() {
        return mBaudRate;
    }

    public void setBaudRate(String baudRate) {
        mBaudRate = baudRate;
    }

    public String getDataBit() {
        return mDataBits;
    }

    public void setDataBit(String dataBit) {
        mDataBits = dataBit;
    }

    public String getStopBit() {
        return mStopBits;
    }

    public void setStopBit(String stopBit) {
        mStopBits = stopBit;
    }

    public String getCheckBit() {
        return mParityBits;
    }

    public void setCheckBit(String checkBit) {
        mParityBits = checkBit;
    }

    /**
     * 在想要进行序列号传递的实体类内部一定要声明该常量。常量名只能是CREATOR,类型也必须是
     * Parcelable.Creator<T>  T:就是当前对象类型
     */
    public static final Creator<SerialConfigParams> CREATOR = new Creator<SerialConfigParams>() {

        /***
         * 根据序列化的Parcel对象，反序列化为原本的实体对象
         * 读出顺序要和writeToParcel的写入顺序相同
         */
        @Override
        public SerialConfigParams createFromParcel(Parcel in) {
            return new SerialConfigParams(in);
        }

        /**
         * 创建一个要序列化的实体类的数组，数组中存储的都设置为null
         */
        @Override
        public SerialConfigParams[] newArray(int size) {
            return new SerialConfigParams[size];
        }
    };

    /**
     * 将对象写入到Parcel（序列化）
     * @param dest：就是对象即将写入的目的对象
     * @param flags: 有关对象序列号的方式的标识
     * 这里要注意，写入的顺序要和在createFromParcel方法中读出的顺序完全相同。例如这里先写入的为name，
     * 那么在createFromParcel就要先读name
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSerialPort);
        dest.writeString(this.mBaudRate);
        dest.writeString(this.mDataBits);
        dest.writeString(this.mStopBits);
        dest.writeString(this.mParityBits);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
