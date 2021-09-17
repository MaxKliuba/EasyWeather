package com.maxclub.easyweather.utils;

import java.util.Locale;

public class LocaleHelper {
    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getUnits() {
        return "metric";

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
