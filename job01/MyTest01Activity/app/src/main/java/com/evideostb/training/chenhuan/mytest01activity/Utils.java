package com.evideostb.training.chenhuan.mytest01activity;

import java.text.SimpleDateFormat;

/**
 * Created by ChenHuan on 2018/2/1.
 */

public class Utils {
    /**
     * 日期格式的转换
     * @param time
     * @return
     */
    public static String transformDate(String time){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateformat.format(Long.valueOf(time));
        return dateStr;
    }

}
