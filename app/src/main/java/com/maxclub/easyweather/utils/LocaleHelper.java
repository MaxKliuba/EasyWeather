package com.maxclub.easyweather.utils;

import com.maxclub.easyweather.SettingsPreferences;

import java.util.Locale;

public class LocaleHelper {

    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getLocaleUnits() {
        String countryCode = Locale.getDefault().getCountry().toUpperCase();

        switch (countryCode) {
            case "US":
            case "LR":
            case "MM":
                return SettingsPreferences.IMPERIAL;
            default:
                return SettingsPreferences.METRIC;
        }
    }
}
