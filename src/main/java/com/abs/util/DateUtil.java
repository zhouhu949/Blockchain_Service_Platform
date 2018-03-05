/*
 * IBM Confidential OCO Source Materials
 * 
 * #ID# Industries and Solutions team at IBM Research - China
 * 
 * (C) Copyright IBM Corp. 2016
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets.
 * 
 */
package com.abs.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static void main(String[] args) {
        System.out.println(parseDate("2017-04-06"));
        System.out.println(parseDate("2017-04-06 23:22"));
        System.out.println(parseDate("2017-04-06 23:33:22"));
        System.out.println(parseDate("2017-04-06 23:33:33:444"));
        System.out.println(parseDate("2017/04/06"));

    }

    /**
     * 格式化string为Date
     * 
     * @param datestr
     * @return date
     * @throws Exception
     */
    public static Date parseDate(String datestr, String fmtstr) {
        if (null == datestr || "".equals(datestr)) {
            return null;
        }
        String[] marks = {"-", "/"};
        boolean hasNoMark = true;
        try {
            if (fmtstr == null || fmtstr.trim().length() == 0) {
                fmtstr = "";
                for (int i = 0; i < marks.length && hasNoMark; i++) {
                    String mark = marks[i];
                    if (datestr.split(mark).length == 3) {
                        switch (datestr.split(":").length) {
                            case 1:
                                fmtstr = String.format("yyyy%sMM%sdd", mark, mark);
                                hasNoMark = false;
                                break;
                            case 2:
                                fmtstr = String.format("yyyy%sMM%sdd HH:mm", mark, mark);
                                hasNoMark = false;
                                break;
                            case 3:
                                if (datestr.split(" ").length == 3) {
                                    fmtstr = String.format("yyyy%sMM%sdd HH:mm:ss SSS", mark, mark);
                                } else {
                                    fmtstr = String.format("yyyy%sMM%sdd HH:mm:ss", mark, mark);
                                }
                                hasNoMark = false;
                                break;
                            case 4:
                                fmtstr = String.format("yyyy%sMM%sdd HH:mm:ss:SSS", mark, mark);
                                hasNoMark = false;
                                break;
                        }
                    } else {
                        switch (datestr.split(":").length) {
                            case 2:
                                fmtstr = String.format("HH:mm", mark, mark);
                                hasNoMark = false;
                                break;
                            case 3:
                                fmtstr = String.format("HH:mm:ss", mark, mark);
                                hasNoMark = false;
                                break;
                            case 4:
                                if (datestr.split(" ").length == 2) {
                                    fmtstr = String.format("HH:mm:ss SSS", mark, mark);
                                } else {
                                    fmtstr = String.format("HH:mm:ss:SSS", mark, mark);
                                }
                                hasNoMark = false;
                                break;
                        }
                    }
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr);
            return sdf.parse(datestr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String format(Date date,String fmtstr){
        SimpleDateFormat sdf = new SimpleDateFormat(fmtstr);
        return sdf.format(date); 
    }
    public static String format(long ms,String fmtstr){
        Date date = new Date(ms);
        return format(date,fmtstr);
    }
    public static Date parseDate(String datestr) {
        return parseDate(datestr, null);
    }

    public static String getYYYYMMdd(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String nowYYYYMMdd() {
        return getYYYYMMdd(new Date());
    }

    public static String getYYYYMMddHHmmss(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String nowYYYYMMddHHmmss() {
        return getYYYYMMddHHmmss(new Date());
    }

    public static String getYYYYMMddHHmmssSSS(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date);
    }

    public static String nowYYYYMMddHHmmssSSS() {
        return getYYYYMMddHHmmssSSS(new Date());
    }
    public static void sleep(long mm){
        try {
            Thread.sleep(mm);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// end
