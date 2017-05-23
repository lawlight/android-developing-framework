package com.hz.lib.views;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hz.lib.R;

import java.util.Calendar;

/**
 * 倒计时view
 * Created by LiuPeng on 16/9/22.
 */
public class CountdownView extends LinearLayout {

    TextView tvDay;
    TextView tvSeparator0;
    TextView tvHour;
    TextView tvSeparator1;
    TextView tvMinute;
    TextView tvSeparator2;
    TextView tvSecond;

    //目标时间
    private Calendar targetTime;

    //是否显示剩余天
    private boolean showDays = true;

    //倒计时完成监听
    public interface OnCountdownFinishListener{
        void onFinish();
    }

    private OnCountdownFinishListener onCountdownFinishListener;

    //倒计时器
    CountDownTimer cdt;

    //是否已开始倒计时
    private boolean started = false;

    public CountdownView(Context context) {
        super(context);
        initView(context);
    }

    public CountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化布局
     *
     * @param context
     */
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_countdown, null);
        addView(view);

        tvDay = (TextView) view.findViewById(R.id.tv_day);
        tvHour = (TextView) view.findViewById(R.id.tv_hour);
        tvMinute = (TextView) view.findViewById(R.id.tv_minute);
        tvSecond = (TextView) view.findViewById(R.id.tv_second);
        tvSeparator0 = (TextView) view.findViewById(R.id.tv_separator0);
        tvSeparator1 = (TextView) view.findViewById(R.id.tv_separator1);
        tvSeparator2 = (TextView) view.findViewById(R.id.tv_separator2);
    }

    /**
     * 开启倒计时
     */
    public void startCountDown(){
        if(started){
            //如果已经开启
            return;
        }
        if(cdt == null){
            //获取结束时间
            Calendar today = Calendar.getInstance();
            if(targetTime == null){
                targetTime = today;
            }
            long future = targetTime.getTimeInMillis() - today.getTimeInMillis();
            if(future <= 0){
                //当前时间已经超过结束时间，不进行倒计时，直接调用结束回调
                if(onCountdownFinishListener != null){
                    onCountdownFinishListener.onFinish();
                }
                return;
            }
            cdt = new CountDownTimer(future, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long sec = millisUntilFinished/1000;
                    long day = sec / (24 * 3600);
                    long hour = sec / 3600;
                    //是否显示天数
                    if(showDays){
                        hour = hour % 24;
                    }
                    long minute = (sec % 3600) / 60;
                    long second = sec % 60;
                    tvDay.setText(String.format("%02d", day));
                    tvHour.setText(String.format("%02d",hour));
                    tvMinute.setText(String.format("%02d",minute));
                    tvSecond.setText(String.format("%02d",second));
                }

                @Override
                public void onFinish() {
                    started = false;
                    tvHour.setText("00");
                    tvMinute.setText("00");
                    tvSecond.setText("00");
                    if(onCountdownFinishListener != null){
                        onCountdownFinishListener.onFinish();
                    }
                }
            };
        }
        cdt.start();
        started = true;
    }

    /**
     * 取消倒计时
     */
    public void cancelCountDown(){
        cdt.cancel();
    }

    /**
     * 获取目标时间
     *
     * @return
     */
    public Calendar getTargetTime() {
        return targetTime;
    }

    /**
     * 设置目标时间
     *
     * @param targetTime
     */
    public void setTargetTime(Calendar targetTime) {
        this.targetTime = targetTime;
    }

    /**
     * 设置字号
     *
     * @param sizeDp
     */
    public void setTextSize(float sizeDp) {
        tvDay.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeDp);
        tvHour.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeDp);
        tvMinute.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeDp);
        tvSecond.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeDp);
        tvSeparator0.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeDp);
        tvSeparator1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeDp);
        tvSeparator2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeDp);
    }

    /**
     * 设置数字颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        tvDay.setTextColor(color);
        tvHour.setTextColor(color);
        tvMinute.setTextColor(color);
        tvSecond.setTextColor(color);
    }

    /**
     * 设置数字背景
     *
     * @param resId
     */
    public void setTextBackgroundResource(int resId) {
        tvDay.setBackgroundResource(resId);
        tvHour.setBackgroundResource(resId);
        tvMinute.setBackgroundResource(resId);
        tvSecond.setBackgroundResource(resId);
    }

    /**
     * 设置分隔符颜色
     *
     * @param color
     */
    public void setSeparatorTextColor(int color) {
        tvSeparator0.setTextColor(color);
        tvSeparator1.setTextColor(color);
        tvSeparator2.setTextColor(color);
    }

    /**
     * 倒计时是否开始
     * @return
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * 设置倒计时完成监听
     * @param onCountdownFinishListener
     */
    public void setOnCountdownFinishListener(OnCountdownFinishListener onCountdownFinishListener) {
        this.onCountdownFinishListener = onCountdownFinishListener;
    }

    /**
     * 是否显示剩余天，当目标时间与当前时间小时数过大时，推荐显示天数
     * @param showDays 显示剩余天数，或只显示小时
     */
    public void setShowDays(boolean showDays) {
        this.showDays = showDays;
        tvDay.setVisibility(showDays ? VISIBLE : GONE);
        tvSeparator0.setVisibility(showDays ? VISIBLE : GONE);
    }
}
