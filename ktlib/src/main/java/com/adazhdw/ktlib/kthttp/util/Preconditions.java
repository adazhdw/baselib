package com.adazhdw.ktlib.kthttp.util;

/**
 * author：daguozhu
 * date-time：2020/11/16 15:36
 * description：
 **/
public class Preconditions {

    private Preconditions() {
        throw new UnsupportedOperationException();
    }

    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }
}
