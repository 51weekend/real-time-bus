/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-29.
 */

package me.chengdong.bustime.adapter;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.activity.R;
import me.chengdong.bustime.activity.SingleLineActivity;
import me.chengdong.bustime.model.SingleLine;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * TODO.
 *
 * @author chengdong
 */
public class SingleLineAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<SingleLine> items = new ArrayList<SingleLine>();

    public SingleLineAdapter(SingleLineActivity context, List<SingleLine> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_line_item, null);
            holder = new ViewHolder();

            holder.tvStationName = (TextView) convertView.findViewById(R.id.tv_stationName);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SingleLine singleLine = items.get(position);
        holder.tvStationName.setText(singleLine.getStandName());
        holder.tvTime.setText(singleLine.getTime());

        return convertView;
    }

    class ViewHolder {
        TextView tvStationName;
        TextView tvTime;
    }

}
