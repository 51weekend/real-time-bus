package me.chengdong.bustime.activity;

import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.StringUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BustimeActivity extends BaseActivity implements OnClickListener {

    private final static String TAG = "BustimeActivity";

    protected Button mQueryStationBtn, mQueryLineBtn;
    private EditText mLineEdittext, mStationEdittext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate");
        setContentView(R.layout.bustime);

        mQueryStationBtn = (Button) findViewById(R.id.searchByStationName);
        mQueryStationBtn.setOnClickListener(this);
        mQueryLineBtn = (Button) findViewById(R.id.searchByLine);
        mQueryLineBtn.setOnClickListener(this);

        mLineEdittext = (EditText) findViewById(R.id.line);
        mLineEdittext.setSingleLine(true);

        mStationEdittext = (EditText) findViewById(R.id.stationName);
        mStationEdittext.setSingleLine(true);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.searchByStationName:
            if (StringUtil.isEmpty(mStationEdittext.getText().toString())) {
                Toast.makeText(BustimeActivity.this, R.string.station_required, Toast.LENGTH_SHORT).show();
                break;
            }
            break;
        case R.id.searchByLine:
            if (StringUtil.isEmpty(mLineEdittext.getText().toString())) {
                Toast.makeText(BustimeActivity.this, R.string.line_required, Toast.LENGTH_SHORT).show();
                break;
            }
            Intent intent = new Intent();
            intent.setClass(this, LineInfoActivity.class);
            intent.putExtra(ParamUtil.LINE_GUID, mLineEdittext.getText().toString());
            startActivity(intent);
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
