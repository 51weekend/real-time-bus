package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.StationBusAdapter;
import me.chengdong.bustime.db.TbFavoriteHandler;
import me.chengdong.bustime.db.TbLineHandler;
import me.chengdong.bustime.db.TbStationHandler;
import me.chengdong.bustime.meta.FavoriteType;
import me.chengdong.bustime.model.Favorite;
import me.chengdong.bustime.model.Line;
import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.model.Station;
import me.chengdong.bustime.model.StationBus;
import me.chengdong.bustime.module.DownloadData;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StationBusActivity extends BaseActivity implements
		OnItemClickListener {

	private static final String TAG = StationBusActivity.class.getSimpleName();

	String stationCode;

	StationBusAdapter mAdapter;

	ListView stationBusListView;

	Button mBackBtn, mChangeErroBtn;
	ImageButton mFavoriteBtn, mRefreshBtn;

	TextView mTitle;

	final List<StationBus> mStationBusList = new ArrayList<StationBus>(0);

	private TbLineHandler tbLineHandler = new TbLineHandler(
			StationBusActivity.this);
	private TbStationHandler tbStationHandler = new TbStationHandler(
			StationBusActivity.this);
	private TbFavoriteHandler tbFavoriteHandler = new TbFavoriteHandler(
			StationBusActivity.this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station_bus);

		stationBusListView = (ListView) findViewById(R.id.station_bus_listview);

		mChangeErroBtn = (Button) findViewById(R.id.change_error);

		mBackBtn = (Button) findViewById(R.id.back_btn);
		mBackBtn.setOnClickListener(this);

		mFavoriteBtn = (ImageButton) findViewById(R.id.btn_favorite);
		mFavoriteBtn.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.title_textview);

		mRefreshBtn = (ImageButton) findViewById(R.id.btn_refresh);
		mRefreshBtn.setOnClickListener(this);

		mLoadDialog = new ProgressDialog(this);
		mLoadDialog.setMessage("正在查询站台车辆信息...");
		mAdapter = new StationBusAdapter(StationBusActivity.this,
				mStationBusList);
		stationBusListView.setAdapter(mAdapter);
		stationBusListView.setOnItemClickListener(this);

		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();

		Intent intent = getIntent();
		stationCode = intent.getStringExtra(ParamUtil.STATION_CODE);
		mTitle.setText(intent.getStringExtra(ParamUtil.STATION_NAME));

		openProgressDialog();
		new QueryStationBusTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.station_bus, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.btn_refresh:
			mLoadDialog.show();
			new QueryStationBusTask().execute();
			break;
		case R.id.btn_favorite:
			Favorite favorite = new Favorite();
			Station station = tbStationHandler.selectOne(stationCode);
			favorite.setCode(stationCode);
			favorite.setName(station.getStandName());
			favorite.setPropertyOne(station.getRoad());
			favorite.setPropertyTwo(station.getTrend());
			favorite.setPropertyThree(station.getLines());
			favorite.setType(FavoriteType.STATION.getType());

			tbFavoriteHandler.saveOrUpdate(favorite);

			Toast.makeText(this, "收藏站台信息成功", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView,
			int position, long id) {
		StationBus stationBus = this.mStationBusList.get((int) id);
		if (stationBus == null) {
			LogUtil.e(TAG, "stationBus is null ");
			return;
		}

		Intent intent = new Intent();
		intent.setClass(this, SingleLineActivity.class);
		intent.putExtra(ParamUtil.LINE_GUID, stationBus.getLineGuid());
		intent.putExtra(ParamUtil.LINE_NUMBER, stationBus.getLineNumber());
		startActivity(intent);

	}

	private class QueryStationBusTask extends AsyncTask<Void, Void, Void> {

		@Override
		public void onPreExecute() {
			openProgressDialog();
		}

		@Override
		protected Void doInBackground(Void... params) {
			ResultData result = DownloadData.getStationBus(
					StationBusActivity.this, stationCode);
			if (result.success()) {
				@SuppressWarnings("unchecked")
				List<StationBus> temps = (List<StationBus>) result.getData();
				StringBuilder sb = new StringBuilder();

				for (int i = 0, n = temps.size(); i < n; i++) {
					StationBus stationBus = temps.get(i);
					sb.append("'").append(stationBus.getLineGuid()).append("'");
					if (i != (n - 1)) {
						sb.append(",");
					}
				}
				List<Line> lines = tbLineHandler.selectListByManyGuid(sb
						.toString());
				for (Line line : lines) {
					for (StationBus stationBus : temps) {
						if (line.getLineGuid().equals(stationBus.getLineGuid())) {
							stationBus.setStartStation(line.getStartStation());
							stationBus.setEndStation(line.getEndStation());
						}
					}
				}
				mStationBusList.clear();
				mStationBusList.addAll(temps);
			} else {
				// TODO 进行错误提示
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			closeProgressDialog();
			mAdapter.notifyDataSetChanged();
		}

	}
}
