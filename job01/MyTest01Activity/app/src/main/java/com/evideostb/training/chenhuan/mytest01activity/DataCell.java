package com.evideostb.training.chenhuan.mytest01activity;

/**
 * Created by ChenHuan on 2018/2/1.
 */

public class DataCell {
    private String m_strTime;
    private String m_strData;

    DataCell() {
        m_strTime = null;
        m_strData = null;
    }

    public void setTime(String strTime) {
        m_strTime = strTime;
    }

    public void setData(String strData) {
        m_strData = strData;
    }

    public String getTime() {
        return m_strTime;
    }

    public String getData() {
        return m_strData;
    }
}
