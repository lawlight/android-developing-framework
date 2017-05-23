package com.hz.lib.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hz.lib.utils.DateTimeUtils;

import java.util.Calendar;

/**
 * 日期/时间选择器
 * Created by LiuPeng on 16/6/29.
 */
public class DateTimePickerView extends TextView {

    public interface OnDateTimeChangedListener{
        void onDateTimeChanged(Calendar calendar);
    }

    private OnDateTimeChangedListener onDateTimeChangedListener;

    public void setOnDateTimeChangedListener(OnDateTimeChangedListener onDateTimeChangedListener) {
        this.onDateTimeChangedListener = onDateTimeChangedListener;
    }

    //日期还是时间模式，默认为日期
    private int mode = MODE_DATE;
    public static final int MODE_DATE = 1;
    public static final int MODE_TIME = 2;

    //当前日期
    private Calendar calendar;

    //日期和时间默认的格式
    private String dateFormat = "yyyy/MM/dd";
    private String timeFormat = "HH:mm:ss";

    public DateTimePickerView(Context context) {
        super(context);
        initView();
    }

    public DateTimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        calendar = Calendar.getInstance();
        this.setText(DateTimeUtils.date2String(calendar.getTime(), mode == MODE_DATE ? dateFormat : timeFormat));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == MODE_DATE){
                    showDatePicker();
                }else{
                    showTimePicker();
                }
            }
        });
    }

    private void showDatePicker(){
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                setText(DateTimeUtils.date2String(calendar.getTime(), dateFormat));
                if(onDateTimeChangedListener != null){
                    onDateTimeChangedListener.onDateTimeChanged(calendar);
                }
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showTimePicker(){
        TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                setText(DateTimeUtils.date2String(calendar.getTime(), timeFormat));
                if(onDateTimeChangedListener != null){
                    onDateTimeChangedListener.onDateTimeChanged(calendar);
                }
            }
        },calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void updateText(){
        setText(DateTimeUtils.date2String(calendar.getTime(), mode == MODE_DATE ? dateFormat : timeFormat));
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        updateText();
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        updateText();
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
        updateText();
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        updateText();
    }


}
