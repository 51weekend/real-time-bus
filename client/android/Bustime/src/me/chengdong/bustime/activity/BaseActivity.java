/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-29.
 */

package me.chengdong.bustime.activity;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * TODO.
 *
 * @author chengdong
 */
public class BaseActivity extends Activity {

    // 在子类中初始化
    protected ProgressDialog mLoadDialog;

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
