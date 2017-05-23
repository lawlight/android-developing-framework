package com.hz.lib.views.calendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日历view，只是显示日期的部分
 * Created by LiuPeng on 16/8/25.
 */
public class SingleCalenderView extends RecyclerView {

    CalendarAdapter adapter;

    //今天
    private Calendar today;

    //当前显示的月份
    private Calendar currMonth;

    /**
     * 是否显示上个月的剩余天数和下个月的前几天
     */
    private boolean showPreAndNextMonthDays = true;

    //日期列表
    private List<CalendarItem> items = new ArrayList<>();

    public SingleCalenderView(Context context) {
        super(context);
        initView();
    }

    public SingleCalenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        today = Calendar.getInstance();
        currMonth = Calendar.getInstance();
        setLayoutManager(new GridLayoutManager(getContext(), 7));
        adapter = new CalendarAdapter(items, null);
        setAdapter(adapter);
        //当月为默认月
        setMonth(Calendar.getInstance());
    }

    /**
     * 刷新日历
     */
    private void refreshCalendar(){
        //重新组合items
        items.clear();

        //获取当前月有多少天
        int days = getDays(currMonth);

        //上一个月的天数
        Calendar calendarPreMonth = Calendar.getInstance();
        calendarPreMonth.setTime(currMonth.getTime());
        calendarPreMonth.add(Calendar.MONTH, -1);
        int preDays = getDays(calendarPreMonth);

        //本月1号
        Calendar calendarFirstDay = Calendar.getInstance();
        calendarFirstDay.setTime(currMonth.getTime());
        calendarFirstDay.set(Calendar.DAY_OF_MONTH, 1);
        //获得此月1号是星期几
        int firstDayOfWeek = calendarFirstDay.get(Calendar.DAY_OF_WEEK);
        //上个月
        if(firstDayOfWeek != 1)
        {
            for(int i = preDays - firstDayOfWeek + 2; i <= preDays; i++){
                CalendarItem item = new CalendarItem(calendarPreMonth.get(Calendar.YEAR), calendarPreMonth.get(Calendar.MONTH), i);
                items.add(item);
            }
        }
        //当月天数
        for(int i = 1; i <= days; i++){
            CalendarItem item = new CalendarItem(currMonth.get(Calendar.YEAR), currMonth.get(Calendar.MONTH), i);
            items.add(item);
        }
        //下个月天数
        Calendar calendarNextMonth = Calendar.getInstance();
        calendarNextMonth.setTime(currMonth.getTime());
        calendarNextMonth.add(Calendar.MONTH, 1);
        if((firstDayOfWeek - 1 + days) % 7 != 0){
            for(int i = 1; i<= 7 - (firstDayOfWeek - 1 + days) % 7; i++){
                CalendarItem item = new CalendarItem(calendarNextMonth.get(Calendar.YEAR), calendarNextMonth.get(Calendar.MONTH), i);
                items.add(item);
            }
        }

        this.getAdapter().notifyDataSetChanged();
    }

    /**
     * 获取此月有多少天
     * @param calendar 日历
     * @return days
     */
    private static int getDays(Calendar calendar) {
        int days = 0;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        boolean isLeapYear =  ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 2:
                days = isLeapYear ? 29 : 28;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
        }
        return days;
    }

    /**
     * 设置当前显示的月份
     * @param calendar 当前月
     */
    public void setMonth(Calendar calendar){
        this.currMonth = calendar;
        refreshCalendar();
    }

    public void setOnCalendarItemClickListener(OnCalendarItemClickListener onCalendarItemClickListener) {
         adapter.setOnCalendarItemClickListener(onCalendarItemClickListener);
    }
}
