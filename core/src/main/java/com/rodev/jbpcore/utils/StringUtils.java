package com.rodev.jbpcore.utils;

public class StringUtils {

    public static String capitalize(String string) {
        if(string.isEmpty() || string.isBlank()) return string;
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

}
