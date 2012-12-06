package me.chengdong.bustime.activity;

import me.chengdong.bustime.R;
import me.chengdong.bustime.task.UpdateTask;
import me.chengdong.bustime.utils.CfgConstant;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.StringUtil;
import roboguice.inject.InjectView;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdateVersionActivity extends BaseActivity {

    @InjectView(R.id.app_version)
    TextView mAppVersion;

    @InjectView(R.id.iv_update_version)
    ImageView mUpdate;

    String apkName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_version);

        mUpdate.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if (intent != null) {
            String appApkName = intent.getStringExtra(ParamUtil.BUNDLE_KEY_APP_APK_NAME);
            if (StringUtil.isNotBlank(appApkName)) {
                apkName = appApkName;
                mAppVersion.setText(appApkName);
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
        case R.id.iv_update_version:

            new UpdateTask(UpdateVersionActivity.this, apkName).execute();
            break;

        default:
            break;
        }

    }
}
