package com.maxclub.easyweather.utils;

import java.util.Locale;

public class LocaleHelper {
    public static final String STANDARD = "standard";
    public static final String IMPERIAL = "imperial";
    public static final String METRIC = "metric";

    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getUnits() {
        return METRIC;

//        String countryCode = Locale.getDefault().getCountry().toUpperCase();
//
//        switch (countryCode) {
//            case "US":
//            case "LR":
//            case "MM":
//                return "imperial";
//            default:
//                return "metric";
//        }
    }
}
