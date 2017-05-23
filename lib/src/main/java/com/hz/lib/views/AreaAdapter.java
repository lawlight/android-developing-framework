package com.hz.lib.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hz.lib.R;
import com.hz.lib.base.KV;

import java.util.List;

/**
 * 省市区adapter
 */
public class AreaAdapter extends BaseAdapter {

    List<KV> list;

    public AreaAdapter(List<KV> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewByResource(position, convertView, parent, R.layout.spn_province_spinner);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewByResource(position, convertView, parent, R.layout.spn_province_spinner_dropdown);
    }

    private View createViewByResource(int position, View convertView, ViewGroup parent, int resource){
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        KV item = list.get(position);
        holder.tvText.setText(item.getValueString());

        return convertView;
    }

    class ViewHolder {
        TextView tvText;

        public ViewHolder(View view){
            tvText = (TextView) view.findViewById(R.id.tv_text);
        }
    }
}
