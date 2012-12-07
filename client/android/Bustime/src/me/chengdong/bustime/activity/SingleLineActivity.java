package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.SingleLineAdapter;
import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.model.SingleLine;
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

public class SingleLineActivity extends BaseActivity implements OnItemClickListener {

    private static final String TAG = SingleLineActivity.class.getSimpleName();

    private String lineGuid;

    private SingleLineAdapter mAdapter;

    @InjectView(R.id.single_line_listview)
    ListView singleLineListView;

    @InjectView(R.id.title_textview)
    TextView mTitle;

    @InjectView(R.id.iv_refresh)
    ImageView mFrefreshBtn;

    @InjectView(R.id.back_btn)
    Button mBackBtn;

    @Inject
    DownLoadData downLoadData;

    private final List<SingleLine> mSingleLineList = new ArrayList<SingleLine>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_line);

        mFrefreshBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);

        mLoadDialog = new ProgressDialog(this);
        mLoadDialog.setMessage("正在查询车次动态信息...");
        mAdapter = new SingleLineAdapter(SingleLineActivity.this, mSingleLineList);
        singleLineListView.setAdapter(mAdapter);
        singleLineListView.setOnItemClickListener(this);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = this.getIntent();
        lineGuid = intent.getStringExtra(ParamUtil.LINE_GUID);
        mTitle.setText(intent.getStringExtra(ParamUtil.LINE_NUMBER));
        openProgressDialog();
        new QuerySingleLineTask().execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.back_btn:
            this.finish();
            break;
        case R.id.iv_refresh:
            mLoadDialog.show();
            new QuerySingleLineTask().execute();
            break;
        default:
            break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
        SingleLine singleLine = this.mSingleLineList.get((int) id);
        if (singleLine == null) {
            LogUtil.e(TAG, "station info is null ");
            return;
        }

        Intent intent = new Intent();
        intent.setClass(this, StationBusActivity.class);
        intent.putExtra(ParamUtil.STATION_CODE, singleLine.getStandCode());
        intent.putExtra(ParamUtil.STATION_NAME, singleLine.getStandName());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_line, menu);
        return true;
    }

    private class QuerySingleLineTask extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
            openProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                ResultData result = downLoadData.getSingleLine(SingleLineActivity.this, lineGuid);
                if (result.success()) {
                    @SuppressWarnings("unchecked")
                    List<SingleLine> temps = (List<SingleLine>) result.getData();
                    mSingleLineList.clear();
                    mSingleLineList.addAll(temps);
                } else {
                    // TODO 提示错误给用户
                }

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

}
