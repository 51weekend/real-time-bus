/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-29.
 */

package me.chengdong.bustime.activity;

import me.chengdong.bustime.utils.LogUtil;
import roboguice.activity.RoboActivity;
import android.app.ProgressDialog;
import android.view.View.OnClickListener;

/**
 * TODO.
 * 
 * @author chengdong
 */
public abstract class BaseActivity extends RoboActivity implements
		OnClickListener {

	private final static String TAG = BaseActivity.class.getSimpleName();

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

}
