/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-29.
 */

package me.chengdong.bustime.adapter;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.meta.FavoriteType;
import me.chengdong.bustime.model.Favorite;
import android.app.Activity;
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
public class FavoriteAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Favorite> items = new ArrayList<Favorite>();

    private FavoriteType favoriteType;

    public FavoriteAdapter(Activity context, List<Favorite> items, FavoriteType favoriteType) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.favoriteType = favoriteType;
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
            holder = new ViewHolder();
            if (favoriteType == FavoriteType.STATION) {

                convertView = inflater.inflate(R.layout.station_item, null);

                holder.tvName = (TextView) convertView.findViewById(R.id.tv_stationName);
                holder.tvPropertyOne = (TextView) convertView.findViewById(R.id.tv_road);
                holder.tvpropertyTwo = (TextView) convertView.findViewById(R.id.tv_trend);
                holder.tvpropertyThree = (TextView) convertView.findViewById(R.id.tv_lines);

            } else {
                convertView = inflater.inflate(R.layout.line_item, null);

                holder.tvName = (TextView) convertView.findViewById(R.id.tv_lineNumber);
                holder.tvPropertyOne = (TextView) convertView.findViewById(R.id.tv_startStation);
                holder.tvpropertyTwo = (TextView) convertView.findViewById(R.id.tv_run_time);
                holder.tvpropertyThree = (TextView) convertView.findViewById(R.id.tv_endStation);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Favorite favorite = items.get(position);
        if (favorite == null) {
            return convertView;
        }
        holder.tvName.setText(favorite.getName());

        holder.tvPropertyOne.setText(favorite.getPropertyOne());
        holder.tvpropertyTwo.setText(favorite.getPropertyTwo());
        holder.tvpropertyThree.setText(favorite.getPropertyThree());

        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvPropertyOne;
        TextView tvpropertyTwo;
        TextView tvpropertyThree;
    }

}
