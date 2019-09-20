package com.androidcodeshop.obviousassigment.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppUtils {

    public static String getTodayDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat currentDateSDFFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return currentDateSDFFormat.format(date);
    }

}
