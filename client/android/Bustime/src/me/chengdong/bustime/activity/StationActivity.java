package me.chengdong.bustime.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.adapter.StationAdapter;
import me.chengdong.bustime.model.Station;
import me.chengdong.bustime.module.DownLoadData;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.StringUtil;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;

public class StationActivity extends BaseActivity implements OnItemClickListener {

    static final String TAG = StationActivity.class.getSimpleName();

    @InjectView(R.id.stationName)
    EditText mStationEditText;

    @InjectView(R.id.searchByStationName)
    Button mSearchBtn;

    @InjectView(R.id.station_info_listview)
    ListView stationListView;

    @Inject
    DownLoadData downLoadData;

    List<Station> mStationList = new ArrayList<Station>(0);

    StationAdapter mAdapter;

    String stationName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station);

        mSearchBtn.setOnClickListener(this);

        mStationEditText.setSingleLine(true);
        mStationEditText.clearFocus();

        mLoadDialog = new ProgressDialog(this);
        mLoadDialog.setMessage("正在查询车次信息...");

        mAdapter = new StationAdapter(StationActivity.this, mStationList);
        stationListView.setAdapter(mAdapter);
        stationListView.setOnItemClickListener(this);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStationList != null && mStationList.size() > 0) {
            return;
        }
        if (StringUtil.isEmpty(stationName)) {
            return;
        }
        openProgressDialog();
        new QueryStationTask().execute();
        LogUtil.d(TAG, "onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_info, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.searchByStationName:
            if (StringUtil.isEmpty(mStationEditText.getText().toString())) {
                Toast.makeText(StationActivity.this, R.string.line_required, Toast.LENGTH_SHORT).show();
                break;
            }

            stationName = mStationEditText.getText().toString();
            new QueryStationTask().execute();
            break;
        default:
            break;
        }

    }

    private class QueryStationTask extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
            if (StringUtil.isEmpty(stationName)) {
                return;
            }
            openProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                if (StringUtil.isEmpty(stationName)) {
                    return null;
                }
                String name = URLEncoder.encode(stationName, "utf-8");
                List<Station> temps = downLoadData.getStation(StationActivity.this, URLEncoder.encode(name, "utf-8"));
                mStationList.clear();
                mStationList.addAll(temps);

            } catch (Exception e) {
                LogUtil.e(TAG, "获取数据出错", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            closeProgressDialog();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }
}
