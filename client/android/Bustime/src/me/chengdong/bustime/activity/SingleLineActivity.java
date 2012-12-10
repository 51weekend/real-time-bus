package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.SingleLineAdapter;
import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.model.SingleLine;
import me.chengdong.bustime.module.DownloadData;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SingleLineActivity extends BaseActivity {

    private static final String TAG = SingleLineActivity.class.getSimpleName();

    private String lineGuid;

    private SingleLineAdapter mAdapter;

    ListView singleLineListView;

    TextView mTitle;

    ImageView mFrefreshBtn;

    Button mBackBtn;

    private final List<SingleLine> mSingleLineList = new ArrayList<SingleLine>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_line);

        singleLineListView = (ListView) this.findViewById(R.id.single_line_listview);

        mTitle = (TextView) this.findViewById(R.id.title_textview);

        mFrefreshBtn = (ImageView) this.findViewById(R.id.iv_refresh);

        mBackBtn = (Button) this.findViewById(R.id.back_btn);

        mFrefreshBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);

        mLoadDialog = new ProgressDialog(this);
        mLoadDialog.setMessage("正在查询车次动态信息...");
        mAdapter = new SingleLineAdapter(SingleLineActivity.this, mSingleLineList);
        singleLineListView.setAdapter(mAdapter);

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
                ResultData result = DownloadData.getSingleLine(SingleLineActivity.this, lineGuid);
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
