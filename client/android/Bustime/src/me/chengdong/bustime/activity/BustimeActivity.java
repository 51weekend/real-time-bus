package me.chengdong.bustime.activity;

import roboguice.inject.InjectView;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.StringUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BustimeActivity extends BaseActivity {

	private final static String TAG = "BustimeActivity";

	@InjectView(R.id.searchByStationName)
	Button mQueryStationBtn;

	@InjectView(R.id.searchByLine)
	Button mQueryLineBtn;

	@InjectView(R.id.btn_logout)
	protected Button mLogoutBtn;

	@InjectView(R.id.line)
	private EditText mLineEdittext;

	@InjectView(R.id.stationName)
	EditText mStationEdittext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "onCreate");
		setContentView(R.layout.bustime);

		mQueryStationBtn.setOnClickListener(this);
		mQueryLineBtn.setOnClickListener(this);
		mLogoutBtn.setOnClickListener(this);

		mLineEdittext.setSingleLine(true);
		mLineEdittext.clearFocus();

		mStationEdittext.setSingleLine(true);
		mStationEdittext.clearFocus();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.searchByLine:
			if (StringUtil.isEmpty(mLineEdittext.getText().toString())) {
				Toast.makeText(BustimeActivity.this, R.string.line_required,
						Toast.LENGTH_SHORT).show();
				break;
			}
			Intent intent = new Intent();
			intent.setClass(this, LineInfoActivity.class);
			intent.putExtra(ParamUtil.LINE_GUID, mLineEdittext.getText()
					.toString());
			startActivity(intent);
			break;
		case R.id.searchByStationName:
			if (StringUtil.isEmpty(mStationEdittext.getText().toString())) {
				Toast.makeText(BustimeActivity.this, R.string.station_required,
						Toast.LENGTH_SHORT).show();
				break;
			}
			break;
		case R.id.btn_logout:
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG, "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "onPause");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.d(TAG, "onStart");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "onDestroy");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(TAG, "onStop");
	}

	@Override
	public void onRestart() {
		super.onRestart();
		LogUtil.d(TAG, "onRestart");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bustime, menu);
		return true;
	}

}
