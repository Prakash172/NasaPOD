package com.androidcodeshop.obviousassigment.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppUtils {

    private static final String PATTERN = "yyyy-MM-dd";
    private static SimpleDateFormat currentDateSDFFormat;

    public static String getTodayDate() {
        Date date = Calendar.getInstance().getTime();
        currentDateSDFFormat = new SimpleDateFormat(PATTERN, Locale.ENGLISH);
        return currentDateSDFFormat.format(date);
    }

    public static String nextDay(String currentDay){
        String nextDayStr = "";
        DateTime dateTime = DateTime.parse(currentDay, DateTimeFormat.forPattern(PATTERN));
        nextDayStr = dateTime.plusDays(1).toString(PATTERN);
        return nextDayStr;
    }

    public static String previousDay(String currentDay){
        String nextDayStr = "";
        DateTime dateTime = DateTime.parse(currentDay, DateTimeFormat.forPattern(PATTERN));
        nextDayStr = dateTime.plusDays(-1).toString(PATTERN);
        return nextDayStr;
    }

}
