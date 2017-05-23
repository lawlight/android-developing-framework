package com.hz.lib.utils;

/**
 * 字符串工具类
 *
 * @author LiuPeng
 */
public class StringUtils {

    private StringUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 判断字符串是否为null或为空字符串或值为“null”的字符串
     *
     * @param text 待判断字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String text) {
        return (text == null || "".equals(text) || "null".equals(text));
    }

    /**
     * 将字符串转换为double类型
     *
     * @return 字符串
     */
    public static double string2Double(String text) {
        if (isEmpty(text)) {
            return 0.0;
        }
        return Double.valueOf(text);
    }

    /**
     * double转字符串
     *
     * @param num    double数据
     * @param retain 保留小数位
     * @return 字符串
     */
    public static String double2String(double num, int retain) {
        return String.format("%." + retain + "f", num);
    }

    /**
     * 数值字符串转换
     *
     * @param text   字符串
     * @param retain 保留小数位
     * @return 符合格式的字符串
     */
    public static String stringValue2String(String text, int retain) {
        return double2String(string2Double(text), retain);
    }

    /**
     * 阿拉伯数字转换成汉字
     *
     * @param number 阿拉伯数字
     */
    public static String outNum(int number) {
        String[] num = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] unit = new String[]{"", "十", "百", "千", "万", "亿"};
        String input = Integer.toString(number);
        String[] mid = new String[input.length()];
        String output = "";

        //转换数字
        for (int i = 0; i < input.length(); i++) {
            mid[i] = num[Integer.parseInt("" + input.charAt(i))];
            output += mid[i];
        }

        //加入单位
        String str = "";
        String result = "";
        for (int i = 0; i < output.length(); i++) {
            if (output.length() - i - 1 == 0) {
                str = "" + output.charAt(i);
            } else if ((output.length() - i - 1 + 4) % 8 == 0) {
                str = output.charAt(i) + unit[4];
            } else if ((output.length() - i - 1) % 8 == 0) {
                str = output.charAt(i) + unit[5];
            } else {
                str = output.charAt(i) + unit[(output.length() - i - 1) % 4];
            }
            result += str;
        }

        //格式化成中文习惯表达
        result = result.replaceAll("零[千百十]", "零");
        result = result.replaceAll("亿零+万", "亿零");
        result = result.replaceAll("万零+亿", "万亿");
        result = result.replaceAll("零+", "零");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零亿", "亿");
        result = result.replaceAll("^一十", "十");
        result = result.replaceAll("零$", "");
        return result;
    }
}
