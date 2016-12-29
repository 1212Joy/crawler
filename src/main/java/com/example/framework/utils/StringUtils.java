package com.example.framework.utils;

/**
 * Created by ZhaiJiaYi on 2016/12/29.
 */
public final class StringUtils extends org.apache.commons.lang3.StringUtils {
    private StringUtils() {
    }

    public static boolean notEquals(CharSequence cs1, CharSequence cs2) {
        return !equals(cs1, cs2);
    }

    public static boolean notEqualsIgnoreCase(CharSequence cs1, CharSequence cs2) {
        return !equalsIgnoreCase(cs1, cs2);
    }
}

