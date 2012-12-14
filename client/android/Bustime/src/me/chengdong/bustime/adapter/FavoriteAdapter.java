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

    public FavoriteAdapter(Activity context, List<Favorite> items) {
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
            convertView = inflater.inflate(R.layout.favorite_item, null);
            holder = new ViewHolder();

            holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvPropertyOne = (TextView) convertView.findViewById(R.id.tv_property_one);
            holder.tvpropertyTwo = (TextView) convertView.findViewById(R.id.tv_property_two);
            holder.tvpropertyThree = (TextView) convertView.findViewById(R.id.tv_property_three);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Favorite favorite = items.get(position);
        if (favorite == null) {
            return convertView;
        }
        holder.tvType.setText(FavoriteType.resolve(favorite.getType()).getName());
        holder.tvName.setText(favorite.getName());

        holder.tvPropertyOne.setText(favorite.getPropertyOne());
        holder.tvpropertyTwo.setText(favorite.getPropertyTwo());
        holder.tvpropertyThree.setText(favorite.getPropertyThree());

        return convertView;
    }

    class ViewHolder {
        TextView tvType;
        TextView tvName;
        TextView tvPropertyOne;
        TextView tvpropertyTwo;
        TextView tvpropertyThree;
    }

}
