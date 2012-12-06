/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-29.
 */

package me.chengdong.bustime.activity;

import roboguice.activity.RoboActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View.OnClickListener;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * TODO.
 * 
 * @author chengdong
 */
public abstract class BaseActivity extends RoboActivity implements OnClickListener {

    // 在子类中初始化
    protected ProgressDialog mLoadDialog;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    /**
     * 打开加载框
     */
    protected void openProgressDialog() {
        if (mLoadDialog != null && !mLoadDialog.isShowing()) {
            mLoadDialog.show();
        }
    }

    /**
     * 关闭加载框
     */
    protected void closeProgressDialog() {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
    }

}
