package com.hz.lib.views.calendar;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hz.lib.R;

import java.util.Calendar;
import java.util.List;

/**
 * 日历adapter
 * Created by LiuPeng on 16/8/25.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    /**
     * 普通文本
     */
    public static final int CALENDAR_TEXT_COLOR_NORMAL = Color.parseColor("#222222");
    /**
     * 选中后的文本颜色
     */
    public static final int CALENDAR_TEXT_COLOR_SELECTED = Color.parseColor("#000000");
    /**
     * 今天的普通颜色
     */
    public static final int CALENDAR_TEXT_COLOR_TODAY_NORMAL = Color.parseColor("#222222");
    /**
     * 选中后今天的颜色
     */
    public static final int CALENDAR_TEXT_COLOR_TODAY_SELECTED = Color.parseColor("#000000");

    List<CalendarItem> list;
    private OnCalendarItemClickListener onCalendarItemClickListener;

    public CalendarAdapter(List<CalendarItem> list, OnCalendarItemClickListener onCalendarItemClickListener) {
        this.list = list;
        this.onCalendarItemClickListener = onCalendarItemClickListener;
    }

    public void setOnCalendarItemClickListener(OnCalendarItemClickListener onCalendarItemClickListener) {
        this.onCalendarItemClickListener = onCalendarItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CalendarItem item = list.get(position);
        holder.tvDate.setText(item.calendar.get(Calendar.DAY_OF_MONTH) + "");
        holder.tvLunar.setText(item.lunarDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onCalendarItemClickListener != null){
                    onCalendarItemClickListener.onItemClick(item);
                }
                //刷新显示
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private boolean isToday(CalendarItem item){
        Calendar today = Calendar.getInstance();
        return (item.calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvDate;
        TextView tvLunar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvLunar = (TextView) itemView.findViewById(R.id.tv_lunar);
        }
    }
}
