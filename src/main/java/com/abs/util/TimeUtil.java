package com.abs.util;

public class TimeUtil {
    public static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

