package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.UpdateIntroAdapter;
import me.chengdong.bustime.task.UpdateTask;
import me.chengdong.bustime.utils.CfgConstant;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.StringUtil;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class UpdateVersionActivity extends BaseActivity {

    TextView mVersionName;

    ListView mVersionIntro;

    Button mDownload;

    Button mCancel;

    UpdateIntroAdapter mAdapter;

    final List<String> mUpdateIntroList = new ArrayList<String>(0);

    String apkName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_version);

        mVersionName = (TextView) this.findViewById(R.id.app_version_name);

        mVersionIntro = (ListView) this.findViewById(R.id.update_intro_listview);

        mDownload = (Button) this.findViewById(R.id.update_download);

        mCancel = (Button) this.findViewById(R.id.update_cancel);

        mAdapter = new UpdateIntroAdapter(UpdateVersionActivity.this, mUpdateIntroList);
        mVersionIntro.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        mDownload.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if (intent != null) {
            String appApkName = intent.getStringExtra(ParamUtil.BUNDLE_APK_NAME);
            mVersionName.setText(intent.getStringExtra(ParamUtil.BUNDLE_VERSION_NAME));
            String introString = intent.getStringExtra(ParamUtil.BUNDLE_VERSION_INTRO);
            String[] intros = introString.split("\\|");
            mUpdateIntroList.clear();
            for (String string : intros) {
                mUpdateIntroList.add(string);
            }
            mAdapter.notifyDataSetChanged();
            if (StringUtil.isNotEmpty(appApkName)) {
                this.apkName = appApkName;
            }
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.cancel(CfgConstant.UPDATE_NOTIFY_ID);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.update_download:
            new UpdateTask(UpdateVersionActivity.this, apkName).execute();
            EasyTracker.getTracker().trackEvent("ui_action", "button_press", "update_download", 1l);
            break;

        case R.id.update_cancel:
            this.finish();
            EasyTracker.getTracker().trackEvent("ui_action", "button_press", "update_cancel", 1l);
            break;
        default:
            break;
        }

    }
}
