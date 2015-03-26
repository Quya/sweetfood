package com.quya.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.quya.ConstantDataManager;


public class DateUtils {

    private static final int MILLISECOND_RATE = 1000;
    public static String format(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
     public static String seqformat(Date date) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }
    public static String shortFormat(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    public static String dateformat(Date date) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }
    public static String getCurrentYYMM(){
    	Date date = new Date();
    	DateFormat format = new SimpleDateFormat("yyyyMMdd");
    	return format.format(date).substring(0,6);
    }
    public static Date getNewDate(int year, int month, int day) {
        return getNewCalendar(year, month, day, 0, 0, 0).getTime();
    }

    public static Calendar getNewCalendar(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ConstantDataManager.getDataBaseServerTimeMIllis());
        calendar.set(year, month - 1, day, hour, minute, second);
        clearMillisecond(calendar);
        return calendar;
    }

    private static void clearMillisecond(Calendar calendar) {
        long time = calendar.getTimeInMillis() / MILLISECOND_RATE;
        calendar.setTimeInMillis(time * MILLISECOND_RATE);
    }

    public static Date getNewDate(int year, int month, int day, int hour, int minute, int second) {
        return getNewCalendar(year, month, day, hour, minute, second).getTime();
    }
    
    public static Calendar getNewCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar;
    }

    public static Calendar getFirstDayOfMonth(Calendar date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.getTime());
        calendar.set(Calendar.DATE, 1);
        return calendar;
    }

    public static Date getStartDate(int startYear, int startMonth) {
        return getNewDate(startYear, startMonth, 1, 0, 0, 0);
    }

    public static Date getEndDate(int endYear, int endMonth) {
        return getNewDate(endYear, endMonth + 1, 1, 0, 0, 0);
    }
    /**
     * 获取当前日期中午12：00对应的毫秒数
     * @return
     */
    public static long getNowDateTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ConstantDataManager.getDataBaseServerTimeMIllis());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    } 
}