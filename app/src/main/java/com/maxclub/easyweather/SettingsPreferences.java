package com.maxclub.easyweather;

import android.content.Context;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.maxclub.easyweather.utils.LocaleHelper;

public class SettingsPreferences {

    public static final int LIGHT_THEME = AppCompatDelegate.MODE_NIGHT_NO;
    public static final int AUTO_THEME = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    public static final int NIGHT_THEME = AppCompatDelegate.MODE_NIGHT_YES;

    public static final String STANDARD = "standard";
    public static final String IMPERIAL = "imperial";
    public static final String METRIC = "metric";

    private static final String PREF_THEME = "com.maxclub.easyweather.SettingsPreferences.theme";
    private static final String PREF_UNITS = "com.maxclub.easyweather.SettingsPreferences.units";

    public static int getTheme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public static void setTheme(Context context, int theme) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_THEME, theme)
                .apply();

        AppCompatDelegate.setDefaultNightMode(theme);
    }

    public static void setThemeFromPreferences(Context context) {
        AppCompatDelegate.setDefaultNightMode(getTheme(context));
    }

    public static String getUnits(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_UNITS, LocaleHelper.getLocaleUnits());
    }

    public static void setUnits(Context context, String units) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_UNITS, units)
                .apply();
    }
}
