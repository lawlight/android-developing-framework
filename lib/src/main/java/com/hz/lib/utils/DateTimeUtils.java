package com.hz.lib.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间工具类
 *
 * @author LiuPeng
 */
@SuppressLint("SimpleDateFormat")
public class DateTimeUtils {

    private DateTimeUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 将日期转换成给定格式的字符串
     *
     * @param date   待转换时间，如果为null，则默认为现在时间
     * @param format 目标格式，传null默认为yyyy-MM-dd HH:mm:ss
     * @return 转换完成的字符串
     */
    public static String date2String(Date date, String format) {
        String toFormat = format == null ? "yyyy-MM-dd HH:mm:ss" : format;
        SimpleDateFormat simpleFormat = new SimpleDateFormat(toFormat);
        if (date == null) {
            date = new Date();
        }
        return simpleFormat.format(date);
    }

    /**
     * 按照给定格式，将字符串转换为Date类型对象
     *
     * @param fromFormat 给定的日期格式，传null默认为yyyy-MM-dd HH:mm:ss
     * @param text 给定的字符串
     * @return Date对象，如果转换失败，返回null
     */
    public static Date string2Date(String fromFormat, String text) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
        try {
            return sdf.parse(text);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 按照格式转换日期字符串<br/>
     * 如果出现异常，则原样返回数据
     *
     * @param text   原日期字符串，必须符合原格式
     * @param fromFormat 原格式，传null默认为yyyy-MM-dd HH:mm:ss
     * @param toFormat   目标格式，传null默认为yyyy-MM-dd HH:mm:ss
     * @return 目标格式的日期字符串
     */
    public static String formatDateTime(String text, String fromFormat, String toFormat) {
        SimpleDateFormat fromSDF = new SimpleDateFormat(fromFormat);
        SimpleDateFormat toSDK = new SimpleDateFormat(toFormat);
        try {
            Date date = fromSDF.parse(text);
            return toSDK.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return text;
        }
    }

    /**
     * 获取输入时间的时间段描述，如刚刚、N分钟前、N小时前等等<br/>
     * 1分钟内显示刚刚<br/>
     * 1小时内显示XX分钟前<br/>
     * 1天内显示XX小时前<br/>
     * 超过一天显示yyyy/MM/dd格式日期
     *
     * @param dateText 日期字符串的格式必须为yyyy-MM-dd HH:mm:ss
     * @return 日期描述，或原始字符串
     */
    public static String getTimeDescription(String dateText) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateText);
            Date nowDate = new Date();
            long diff = (nowDate.getTime() - date.getTime()) / 1000;
            if (diff < 60) {
                return "刚刚";
            }
            if (diff < 3600) {
                return (diff / 60) + "分钟前";
            }
            if (diff < 24 * 3600) {
                return (diff / 3600) + "小时前";
            }
            SimpleDateFormat returnSdf = new SimpleDateFormat("yyyy-MM-dd");
            return returnSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateText;
        }
    }
    /**
     * 从毫秒数时间转为日期格式
     * @param dateTime 毫秒数
     */
    public static String milliseconds2DateTime(long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sDateFormat.format(new Date(dateTime + 0));
    }

}
