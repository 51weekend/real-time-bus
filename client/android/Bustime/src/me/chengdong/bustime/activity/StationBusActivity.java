package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.adapter.StationBusAdapter;
import me.chengdong.bustime.model.StationBus;
import me.chengdong.bustime.module.DownLoadData;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;

public class StationBusActivity extends BaseActivity implements OnItemClickListener {

    private static final String TAG = StationBusActivity.class.getSimpleName();

    String stationCode;

    StationBusAdapter mAdapter;

    @InjectView(R.id.station_bus_listview)
    ListView stationBusListView;

    @InjectView(R.id.iv_refresh)
    ImageView mFrefreshBtn;

    @InjectView(R.id.back_btn)
    Button mBackBtn;

    @InjectView(R.id.title_textview)
    TextView mTitle;

    final List<StationBus> mStationBusList = new ArrayList<StationBus>(0);

    @Inject
    DownLoadData downloadData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_bus);

        mFrefreshBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);

        mLoadDialog = new ProgressDialog(this);
        mLoadDialog.setMessage("正在查询站台车辆信息...");
        mAdapter = new StationBusAdapter(StationBusActivity.this, mStationBusList);
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
        case R.id.iv_refresh:
            mLoadDialog.show();
            new QueryStationBusTask().execute();
            break;
        default:
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
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
            try {
                List<StationBus> temps = downloadData.getStationBus(StationBusActivity.this, stationCode);
                mStationBusList.clear();
                mStationBusList.addAll(temps);
            } catch (Exception e) {
                LogUtil.e(TAG, "获取stationBus 数据出错", e);
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
