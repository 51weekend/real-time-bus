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
import me.chengdong.bustime.utils.StringUtil;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * TODO.
 *
 * @author chengdong
 */
public class SingleLineAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<SingleLine> items = new ArrayList<SingleLine>(0);

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
            holder.rlStation = (RelativeLayout) convertView.findViewById(R.id.layout_single_line_detail);
            holder.rlCurrentStation = (RelativeLayout) convertView.findViewById(R.id.layout_single_line_detail_current);
            holder.tvStationName = (TextView) convertView.findViewById(R.id.tv_stationName);
            holder.tvCurrentStationName = (TextView) convertView.findViewById(R.id.tv_current_stationName);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SingleLine singleLine = items.get(position);

        if (StringUtil.isEmpty(singleLine.getTime())) {
            holder.rlStation.setVisibility(View.VISIBLE);
            holder.rlCurrentStation.setVisibility(View.GONE);
            holder.tvStationName.setText(Html.fromHtml("<u>" + singleLine.getStandName() + "</u>"));
        } else {
            holder.rlStation.setVisibility(View.GONE);
            holder.rlCurrentStation.setVisibility(View.VISIBLE);
            holder.tvCurrentStationName.setText(Html.fromHtml("<u>" + singleLine.getStandName() + "</u>"));
            holder.tvTime.setText("  "+singleLine.getTime());
        }

        return convertView;
    }

    class ViewHolder {
        RelativeLayout rlStation;
        RelativeLayout rlCurrentStation;
        TextView tvStationName;
        TextView tvCurrentStationName;
        TextView tvTime;
    }

}
