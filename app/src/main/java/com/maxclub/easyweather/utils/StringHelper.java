package com.maxclub.easyweather.utils;

public class StringHelper {
    public static String capitalize(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }

        return Character.toUpperCase(source.charAt(0)) + source.substring(1).toLowerCase();
    }
}
