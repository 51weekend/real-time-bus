package me.chengdong.bustime.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.StationAdapter;
import me.chengdong.bustime.db.TbConfigHandler;
import me.chengdong.bustime.db.TbStationHandler;
import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.model.Station;
import me.chengdong.bustime.module.DownloadData;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.StringUtil;
import me.chengdong.bustime.utils.TipUtil;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class StationActivity extends BaseActivity implements OnItemClickListener {

    static final String TAG = StationActivity.class.getSimpleName();

    EditText mStationEditText;

    ImageView mSearchClear;

    ImageView mSearch;

    ListView stationListView;

    List<Station> mStationList = new ArrayList<Station>(0);

    TbConfigHandler tbConfigHandler = new TbConfigHandler(StationActivity.this);

    StationAdapter mAdapter;

    String stationName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station);

        mStationEditText = (EditText) this.findViewById(R.id.stationName);

        mSearchClear = (ImageView) this.findViewById(R.id.iv_search_clear);

        mSearch = (ImageView) this.findViewById(R.id.iv_search);

        stationListView = (ListView) this.findViewById(R.id.station_info_listview);

        mSearchClear.setOnClickListener(this);
        mSearch.setOnClickListener(this);

        mStationEditText.setSingleLine(true);
        mStationEditText.clearFocus();

        mStationEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (StringUtil.isEmpty(mStationEditText.getText().toString())) {
                    mSearchClear.setVisibility(View.GONE);
                } else {
                    stationName = mStationEditText.getText().toString();
                    new QueryStationFromLocalTask().execute();
                    mSearchClear.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });

        mStationEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String lineNumber = mStationEditText.getText().toString();
                    if (StringUtil.isBlank(lineNumber)) {
                        TipUtil.tipDescription(StationActivity.this, "请输入站台名称！");
                    } else {
                        // TODO 查询内容
                        if (mStationList.size() != 0) {
                            return false;
                        }
                        new QueryStationFromServerTask().execute();
                    }
                }
                return false;
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

        String savedLineEditData = tbConfigHandler.getStationName();
        if (StringUtil.isEmpty(stationName) && !StringUtil.isEmpty(savedLineEditData)) {
            stationName = savedLineEditData;
            mStationEditText.setText(stationName);
            mStationEditText.clearFocus();
        }
        if (StringUtil.isEmpty(mStationEditText.getText().toString())) {
            return;
        } else {
            mSearchClear.setVisibility(View.VISIBLE);
        }
        new QueryStationFromLocalTask().execute();
    }

    public void onPause() {
        super.onPause();
        if (StringUtil.isEmpty(mStationEditText.getText().toString())) {
            return;
        }

        tbConfigHandler.saveStationName(mStationEditText.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_info, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.iv_search_clear:
            mStationEditText.setText("");
            break;
        case R.id.iv_search:
            new QueryStationFromServerTask().execute();
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

    private class QueryStationFromServerTask extends AsyncTask<Void, Void, Void> {

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
                ResultData result = DownloadData.getStation(StationActivity.this, URLEncoder.encode(name, "utf-8"));
                if (result.success()) {
                    @SuppressWarnings("unchecked")
                    List<Station> temps = (List<Station>) result.getData();
                    mStationList.clear();
                    mStationList.addAll(temps);

                    TbStationHandler tbStationHandler = new TbStationHandler(StationActivity.this);
                    for (Station station : temps) {
                        tbStationHandler.saveOrUpdate(station);
                    }
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

    private class QueryStationFromLocalTask extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
            if (StringUtil.isEmpty(stationName)) {
                return;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                if (StringUtil.isEmpty(stationName)) {
                    return null;
                }

                TbStationHandler stationHandler = new TbStationHandler(StationActivity.this);
                List<Station> stations = stationHandler.selectList(stationName);
                mStationList.clear();
                mStationList.addAll(stations);

            } catch (Exception e) {
                LogUtil.e(TAG, "获取数据出错", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
