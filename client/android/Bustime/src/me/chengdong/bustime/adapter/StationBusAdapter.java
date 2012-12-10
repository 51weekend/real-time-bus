/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-3.
 */

package me.chengdong.bustime.adapter;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.model.StationBus;
import me.chengdong.bustime.utils.StringUtil;
import android.app.Activity;
import android.text.Html;
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
public class StationBusAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<StationBus> items = new ArrayList<StationBus>();

    public StationBusAdapter(Activity context, List<StationBus> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.station_bus_item, null);
            holder = new ViewHolder();

            holder.tvLineNumber = (TextView) convertView.findViewById(R.id.tv_lineNumber);
            holder.tvStandNum = (TextView) convertView.findViewById(R.id.tv_standNum);
            holder.tvStationTag = (TextView) convertView.findViewById(R.id.station_tag);
            holder.tvStartStation = (TextView) convertView.findViewById(R.id.tv_startStation);
            holder.tvEndStation = (TextView) convertView.findViewById(R.id.tv_endStation);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StationBus stationBus = items.get(position);
        holder.tvLineNumber.setText(Html.fromHtml("<u>" + stationBus.getLineNumber() + "</u>"));
        holder.tvStandNum.setText(stationBus.getStandNum());
        holder.tvStartStation.setText(stationBus.getStartStation());
        holder.tvEndStation.setText(stationBus.getEndStation());

        if (StringUtil.isNumeric(stationBus.getStandNum())) {
            holder.tvStationTag.setVisibility(View.VISIBLE);
        } else {
            holder.tvStationTag.setVisibility(View.GONE);
            if ("无车".equals(stationBus.getStandNum())) {
                holder.tvStandNum.setText("待发");
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvLineNumber;
        TextView tvStandNum;
        TextView tvStationTag;
        TextView tvStartStation;
        TextView tvEndStation;

    }

}
