package com.hz.lib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * URL相关工具类
 */
public class URLUtils {

    private URLUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }


    /**
     * 检查是否是合法的URL
     * @param url url字符串
     * @return 是否合法
     */
    static public boolean checkURL(String url){
        String check = "((http|ftp|https|file)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        Pattern p = Pattern.compile(check);
        Matcher m = p.matcher(url);
        return m.matches();
    }

    /**
     * 为url添加一个get参数
     * @param url url
     * @param key 参数key
     * @param value 参数value
     * @return 带参数的url
     */
    static public String addParam(String url, String key, String value){
        String result;
        if(url.contains("?")){
            result = url + "&" + key + "=" + value;
        }else{
            result = url + "?" + key + "=" + value;
        }
        return result;
    }
}
