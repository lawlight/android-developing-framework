package com.hz.lib.utils;

import android.util.Log;

/**
 * 日志打印类，正式运行时，可以讲LOGLEVEL值进行改变来控制全局是否输出log
 */
public class LogUtils {

    public static int NORLOGLEVEL = 1;   //正常输出的地方
    public static int NOLOGLEVEL = 0;    //不输出
    public static int LOGLEVEL = NORLOGLEVEL;

    private static String content;


    public static void e(String tag, String content) {
        if (LOGLEVEL >= NOLOGLEVEL) {
            Log.e(tag, content);
        }
    }

    public static void d(String tag, String content) {
        LogUtils.content = content;
        if (LOGLEVEL >= NOLOGLEVEL) {
            Log.d(tag, content);
        }
    }

    public static void i(String tag, String content) {
        LogUtils.content = content;
        if (LOGLEVEL >= NOLOGLEVEL) {
            Log.i(tag, content);
        }
    }

}
