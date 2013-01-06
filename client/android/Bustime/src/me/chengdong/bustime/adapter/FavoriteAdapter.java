/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-29.
 */

package me.chengdong.bustime.adapter;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.db.TbFavoriteHandler;
import me.chengdong.bustime.meta.FavoriteType;
import me.chengdong.bustime.model.Favorite;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * TODO.
 * 
 * @author chengdong
 */
@SuppressLint("ShowToast")
public class FavoriteAdapter extends BaseAdapter implements OnClickListener {

    private LayoutInflater inflater;
    private List<Favorite> items = new ArrayList<Favorite>();

    private FavoriteType favoriteType;

    TbFavoriteHandler tbFavoriteHandler;

    private boolean editable;

    public FavoriteAdapter(Activity context, List<Favorite> items, FavoriteType favoriteType, boolean editable) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.favoriteType = favoriteType;
        this.editable = editable;
        this.tbFavoriteHandler = new TbFavoriteHandler(context);
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
                holder.tvPropertyOne = (TextView) convertView.findViewById(R.id.tv_endStation);
                holder.tvpropertyTwo = (TextView) convertView.findViewById(R.id.tv_startStation);
                holder.tvpropertyThree = (TextView) convertView.findViewById(R.id.tv_run_time);
            }

            holder.ivDetail = (ImageView) convertView.findViewById(R.id.iv_detail);
            holder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);

            holder.btnDelete.setOnClickListener(this);

            if (editable) {
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.ivDetail.setVisibility(View.GONE);
            } else {
                holder.btnDelete.setVisibility(View.GONE);
                holder.ivDetail.setVisibility(View.VISIBLE);
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

        holder.btnDelete.setTag(favorite.getCode());

        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvPropertyOne;
        TextView tvpropertyTwo;
        TextView tvpropertyThree;
        Button btnDelete;
        ImageView ivDetail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_delete:
            tbFavoriteHandler.delete(String.valueOf(v.getTag()));
            Favorite delete = null;
            for (Favorite favorite : items) {
                if (favorite.getCode().equals(v.getTag())) {
                    delete = favorite;
                    break;
                }
            }
            if (delete != null) {
                items.remove(delete);
            }
            this.notifyDataSetChanged();
            break;

        default:
            break;
        }

    }
}
