package com.evideostb.training.chenhuan.aidl;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by ChenHuan on 2018/1/29.
 */

public class OutDataStream implements Parcelable {
    private String mData;
    private String mTime;
    private boolean mbRead;

    public OutDataStream() {
    }

    protected OutDataStream(Parcel in) {
        this.mData = in.readString();
        this.mTime = in.readString();
        this.mbRead = in.readByte() != 0;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        this.mData = data;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public boolean isRead() {
        return mbRead;
    }

    public void setRead(boolean read) {
        this.mbRead = read;
    }

    /**
     * 在想要进行序列号传递的实体类内部一定要声明该常量。常量名只能是CREATOR,类型也必须是
     * Parcelable.Creator<T>  T:就是当前对象类型
     */
    public static final Creator<OutDataStream> CREATOR = new Creator<OutDataStream>() {

        /***
         * 根据序列化的Parcel对象，反序列化为原本的实体对象
         * 读出顺序要和writeToParcel的写入顺序相同
         */
        @Override
        public OutDataStream createFromParcel(Parcel in) {
            return new OutDataStream(in);
        }

        /**
         * 创建一个要序列化的实体类的数组，数组中存储的都设置为null
         */
        @Override
        public OutDataStream[] newArray(int size) {
            return new OutDataStream[size];
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
        dest.writeString(this.mData);
        dest.writeString(this.mTime);
        dest.writeByte(this.mbRead ? (byte) 1 : (byte) 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
