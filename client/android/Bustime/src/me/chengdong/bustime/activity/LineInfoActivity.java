package me.chengdong.bustime.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.adapter.LineInfoAdapter;
import me.chengdong.bustime.model.Line;
import me.chengdong.bustime.module.DownLoadData;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.StringUtil;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;

public class LineInfoActivity extends BaseActivity implements
		OnItemClickListener {

	private final static String TAG = LineInfoActivity.class.getSimpleName();

	@InjectView(R.id.searchByLine)
	Button mQueryLineBtn;

	@InjectView(R.id.line)
	EditText mLineEdittext;

	@InjectView(R.id.line_info_listview)
	private ListView lineListView;

	@Inject
	DownLoadData downLoadData;

	private String lineNumber;

	private LineInfoAdapter mLineAdapter;
	private final List<Line> mLineList = new ArrayList<Line>(0);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "onCreate");
		setContentView(R.layout.line_info);

		mQueryLineBtn.setOnClickListener(this);

		mLineEdittext.setSingleLine(true);
		mLineEdittext.clearFocus();

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
		if (StringUtil.isEmpty(lineNumber)) {
			return;
		}
		openProgressDialog();
		new QueryLineTask().execute();
		LogUtil.d(TAG, "onResume");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.searchByLine:
			if (StringUtil.isEmpty(mLineEdittext.getText().toString())) {
				Toast.makeText(LineInfoActivity.this, R.string.line_required,
						Toast.LENGTH_SHORT).show();
				break;
			}

			lineNumber = mLineEdittext.getText().toString();
			new QueryLineTask().execute();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView,
			int position, long id) {

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
			if (StringUtil.isEmpty(lineNumber)) {
				return;
			}
			openProgressDialog();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				if (StringUtil.isEmpty(lineNumber)) {
					return null;
				}
				String name = URLEncoder.encode(lineNumber, "utf-8");
				List<Line> temps = downLoadData.getLine(LineInfoActivity.this,
						URLEncoder.encode(name, "utf-8"));
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

}
