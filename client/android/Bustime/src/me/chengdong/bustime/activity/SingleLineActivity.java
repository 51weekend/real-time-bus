package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

import com.google.inject.Inject;

import me.chengdong.bustime.adapter.SingleLineAdapter;
import me.chengdong.bustime.model.SingleLine;
import me.chengdong.bustime.module.DownLoadData;
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

public class SingleLineActivity extends BaseActivity {

	private static final String TAG = SingleLineActivity.class.getSimpleName();

	private String lineGuid;

	private SingleLineAdapter mAdapter;

	@InjectView(R.id.single_line_listview)
	ListView singleLineListView;

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

		Intent intent = getIntent();
		lineGuid = intent.getStringExtra(ParamUtil.LINE_GUID);

		mFrefreshBtn.setOnClickListener(this);

		mBackBtn.setOnClickListener(this);

		singleLineListView.setCacheColorHint(0);

		mLoadDialog = new ProgressDialog(this);
		mLoadDialog.setMessage("正在车次动态信息...");
		mAdapter = new SingleLineAdapter(SingleLineActivity.this,
				mSingleLineList);
		singleLineListView.setAdapter(mAdapter);

		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		mLoadDialog.show();
		new QuerySingleLineTask().execute();
		LogUtil.d(TAG, "onResume");
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
				List<SingleLine> temps = downLoadData.getSingleLine(
						SingleLineActivity.this, lineGuid);
				mSingleLineList.clear();
				mSingleLineList.addAll(temps);

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
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.iv_refresh:
			mLoadDialog.show();
			new QuerySingleLineTask().execute();
			break;
		case R.id.btn_logout:
			this.finish();
			break;
		default:
			break;
		}

	}
}
