package com.hz.lib.views.calendar;

import java.util.Calendar;

/**
 * 日历项
 * Created by LiuPeng on 16/8/25.
 */
public class CalendarItem {

    /**
     * 日历
     */
    public Calendar calendar;

    /**
     * 农历日期
     */
    public String lunarDate;

    /**
     * 是否选中
     */
    public boolean selected;

    /**
     * 构造方法
     * @param year 年
     * @param month 月 0-11
     * @param day 日
     */
    public CalendarItem(int year, int month, int day) {
        this.calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        lunarDate = new ChineseCalendar(calendar).getChinese(ChineseCalendar.CHINESE_TERM_OR_DATE);
        selected = false;
    }
}
