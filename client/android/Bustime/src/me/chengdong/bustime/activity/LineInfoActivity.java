package me.chengdong.bustime.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.adapter.LineInfoAdapter;
import me.chengdong.bustime.http.DownLoadData;
import me.chengdong.bustime.model.Line;
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
import android.widget.ListView;

public class LineInfoActivity extends BaseActivity implements OnItemClickListener {

    private final static String TAG = "LineInfoActivity";

    private LineInfoAdapter mLineAdapter;
    private final List<Line> mLineList = new ArrayList<Line>(0);

    private Button mBackBtn;

    private ListView lineListView;

    private String stationName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_info);
        Intent intent = getIntent();
        stationName = intent.getStringExtra(ParamUtil.LINE_GUID);

        mBackBtn = (Button) findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(this);

        lineListView = (ListView) this.findViewById(R.id.line_info_listview);
        lineListView.setCacheColorHint(0);

        mLoadDialog = new ProgressDialog(this);
        mLoadDialog.setMessage("正在查询车次信息...");
        mLineAdapter = new LineInfoAdapter(LineInfoActivity.this, mLineList);
        lineListView.setAdapter(mLineAdapter);
        lineListView.setOnItemClickListener(this);

        mLineAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLineList != null && mLineList.size() > 0) {
            return;
        }
        mLoadDialog.show();
        new QueryLineTask().execute();
        LogUtil.d(TAG, "onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line_info, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {

        Line line = this.mLineList.get((int) id);
        if (line == null) {
            LogUtil.d(TAG, "line info is null ");
            return;
        }

        Intent intent = new Intent();
        intent.setClass(this, SingleLineActivity.class);
        intent.putExtra(ParamUtil.LINE_GUID, line.getLineGuid());
        startActivity(intent);
    }

    private class QueryLineTask extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
            openProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String name = URLEncoder.encode(stationName, "utf-8");
                List<Line> temps = DownLoadData.getLine(LineInfoActivity.this, URLEncoder.encode(name, "utf-8"));
                mLineList.clear();
                mLineList.addAll(temps);

            } catch (Exception e) {
                LogUtil.e(TAG, "获取数据出错", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            closeProgressDialog();
            mLineAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.back_btn:
            this.finish();
            break;
        default:
            break;
        }

    }

}
