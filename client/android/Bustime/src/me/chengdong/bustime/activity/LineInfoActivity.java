package me.chengdong.bustime.activity;

import java.net.URLEncoder;

import me.chengdong.bustime.http.DownLoadDataService;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

public class LineInfoActivity extends Activity {

    private final static String TAG = "LineInfoActivity";

    private String stationName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_info);
        Intent intent = getIntent();
        stationName = intent.getStringExtra(ParamUtil.STATION_NAME);
    }

    @Override
    public void onResume() {
        super.onResume();
        new QueryLineTask().execute();
        LogUtil.d(TAG, "onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_line_info, menu);
        return true;
    }

    private class QueryLineTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String name = URLEncoder.encode(stationName, "utf-8");
                DownLoadDataService.getLine(LineInfoActivity.this, URLEncoder.encode(name, "utf-8"));

            } catch (Exception e) {
                LogUtil.e(TAG, "获取数据出错", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            LogUtil.d(TAG, "onPostExecute start ...");

        }
    }
}
