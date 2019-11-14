package com.evideostb.training.chenhuan.mediaplayer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatUtil {

	public static String formatDate(String format, long date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(date));

	}
	/**
	 * 毫秒 转成 分:秒
	 * @param time
	 */
	public static String formatTime(int time){
		StringBuilder sb = new StringBuilder();
		int min = (int) Math.floor(time/(1000*60));
		int sec = (int) Math.floor(time/1000-min*60);
		sb.append( String.format("%02d",min) ).append(":").append(String.format("%02d",sec));
		return sb.toString();
	}

	/**
	 * 毫秒 转成 时:分:秒
	 * @param second
	 */
	public static String formatTime2(int second){
		String hh = second / 3600 > 9 ? second / 3600 + "" : "0" + second / 3600;
		String mm = (second % 3600)/60>9?(second % 3600)/60+"":"0"+(second % 3600)/60;
		String ss = (second % 3600) % 60>9?(second % 3600) % 60+"":"0"+(second % 3600) % 60;
		return hh + ":" + mm + ":" + ss;
	}
}
