/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-29.
 */

package me.chengdong.bustime.adapter;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import android.content.Context;
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
public class UpdateIntroAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> items = new ArrayList<String>();

    public UpdateIntroAdapter(Context context, List<String> items) {
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
            convertView = inflater.inflate(R.layout.update_version_item, null);
            holder = new ViewHolder();

            holder.tvUpdateIntro = (TextView) convertView.findViewById(R.id.tv_update_intro);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String intro = items.get(position);
        holder.tvUpdateIntro.setText(intro);

        return convertView;
    }

    class ViewHolder {
        TextView tvUpdateIntro;
    }

}
