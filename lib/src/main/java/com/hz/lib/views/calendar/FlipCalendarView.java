package com.hz.lib.views.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hz.lib.R;

/**
 * 可切换的calendarView，有星期、年月和上月下月按钮显示
 * Created by LiuPeng on 16/8/25.
 */
public class FlipCalendarView extends LinearLayout{

    public FlipCalendarView(Context context) {
        super(context);
        initView(context);
    }

    public FlipCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.view_flip_calendar, this, false);
        addView(view);
    }
}
