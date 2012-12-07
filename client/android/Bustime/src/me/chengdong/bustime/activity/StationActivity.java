package me.chengdong.bustime.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.StationAdapter;
import me.chengdong.bustime.db.TbStationHandler;
import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.model.Station;
import me.chengdong.bustime.module.DownLoadData;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.StringUtil;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;

public class StationActivity extends BaseActivity implements OnItemClickListener {

    static final String TAG = StationActivity.class.getSimpleName();

    @InjectView(R.id.stationName)
    EditText mStationEditText;

    @InjectView(R.id.iv_search)
    ImageView mSearch;

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

        mSearch.setOnClickListener(this);

        mStationEditText.setSingleLine(true);
        mStationEditText.clearFocus();

        mStationEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (StringUtil.isEmpty(mStationEditText.getText().toString())) {
                    mSearch.setVisibility(View.GONE);
                } else {
                    mSearch.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });

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
        if (StringUtil.isEmpty(mStationEditText.getText().toString())) {
            return;
        } else {
            mSearch.setVisibility(View.VISIBLE);
        }
        openProgressDialog();
        new QueryStationTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_info, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.iv_search:
            if (StringUtil.isEmpty(mStationEditText.getText().toString())) {
                Toast.makeText(StationActivity.this, R.string.line_required, Toast.LENGTH_SHORT).show();
                break;
            }

            stationName = mStationEditText.getText().toString();

            TbStationHandler stationHandler = new TbStationHandler(StationActivity.this);
            List<Station> stations = stationHandler.selectList(stationName);
            for (Station station : stations) {
                LogUtil.i(TAG, station.getStandCode() + " " + station.getStandName());
            }
            new QueryStationTask().execute();
            break;
        default:
            break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {

        Station station = this.mStationList.get((int) id);
        if (station == null) {
            LogUtil.e(TAG, "station info is null ");
            return;
        }

        Intent intent = new Intent();
        intent.setClass(this, StationBusActivity.class);
        intent.putExtra(ParamUtil.STATION_CODE, station.getStandCode());
        intent.putExtra(ParamUtil.STATION_NAME, station.getStandName());
        startActivity(intent);

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
                ResultData result = downLoadData.getStation(StationActivity.this, URLEncoder.encode(name, "utf-8"));
                if (result.success()) {
                    @SuppressWarnings("unchecked")
                    List<Station> temps = (List<Station>) result.getData();
                    mStationList.clear();
                    mStationList.addAll(temps);
                } else {
                    // TODO 进行错误提示
                }

            } catch (Exception e) {
                LogUtil.e(TAG, "decode error", e);
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
