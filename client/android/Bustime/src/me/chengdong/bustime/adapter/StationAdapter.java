/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-29.
 */

package me.chengdong.bustime.adapter;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.activity.R;
import me.chengdong.bustime.activity.StationInfoActivity;
import me.chengdong.bustime.model.Station;
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
public class StationAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Station> items = new ArrayList<Station>();

	public StationAdapter(StationInfoActivity context, List<Station> items) {
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
			convertView = inflater.inflate(R.layout.station_info_item, null);
			holder = new ViewHolder();

			holder.tvStandName = (TextView) convertView
					.findViewById(R.id.tv_stationName);
			holder.tvRoad = (TextView) convertView.findViewById(R.id.tv_road);
			holder.tvRoadSection = (TextView) convertView
					.findViewById(R.id.tv_roadSection);
			holder.tvArea = (TextView) convertView.findViewById(R.id.tv_road);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Station station = items.get(position);
		holder.tvStandCode.setText(station.getStandCode());
		holder.tvStandName.setText(station.getStandName());
		holder.tvRoad.setText(station.getRoad());
		holder.tvArea.setText(station.getArea());

		return convertView;
	}

	class ViewHolder {

		TextView tvStandCode;
		TextView tvStandName;
		TextView tvRoad;
		TextView tvRoadSection;
		TextView tvArea;

	}

}
