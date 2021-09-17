package com.maxclub.easyweather.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeHelper {
    public static String getFormattedTime(Context context, Date date) {
        String pattern = DateFormat.is24HourFormat(context) ? "HH:mm" : "hh:mm a";
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        return simpleDateFormat.format(date);
    }

    public static String getFormattedDate(Context context, Date date) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("EE dd MMM", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        return simpleDateFormat.format(date);
    }

    public static String getFormattedDateTime(Context context, Date date) {
        String pattern = DateFormat.is24HourFormat(context) ? "HH:mm" : "hh:mm a";
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(String.format("EE dd MMM, %s", pattern), Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        return simpleDateFormat.format(date);
    }
}
