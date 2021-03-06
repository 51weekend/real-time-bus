/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-29.
 */

package me.chengdong.bustime.adapter;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.activity.LineActivity;
import me.chengdong.bustime.model.Line;
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
public class LineInfoAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Line> items = new ArrayList<Line>();

	public LineInfoAdapter(LineActivity context, List<Line> items) {
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
			convertView = inflater.inflate(R.layout.line_item, null);
			holder = new ViewHolder();

			holder.tvLineNumber = (TextView) convertView
					.findViewById(R.id.tv_lineNumber);
			holder.tvStartStation = (TextView) convertView
					.findViewById(R.id.tv_startStation);
			holder.tvRunTime = (TextView) convertView
					.findViewById(R.id.tv_run_time);
			holder.tvEndStation = (TextView) convertView
					.findViewById(R.id.tv_endStation);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Line line = items.get(position);
		holder.tvLineNumber.setText(line.getLineNumber());
		holder.tvStartStation.setText(line.getStartStation());

		holder.tvRunTime.setText(line.getRunTime());
		holder.tvEndStation.setText(line.getEndStation());

		return convertView;
	}

	class ViewHolder {
		TextView tvLineNumber;
		TextView tvStartStation;
		TextView tvRunTime;
		TextView tvEndStation;
	}

}
