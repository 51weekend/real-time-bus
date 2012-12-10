/*
 * Copyright (c) 2011 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-06-11.
 */

package me.chengdong.bustime.task;

import me.chengdong.bustime.db.TbConfigHandler;
import me.chengdong.bustime.module.ReaderFileData;
import me.chengdong.bustime.utils.LogUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 升级Task.
 * 
 * @author chengdong
 */
public class LoadDataTask extends AsyncTask<String, String, String> {
    public static final String TAG = LoadDataTask.class.getSimpleName();
    private Context ctx;
    TbConfigHandler tbConfigHandler;
    protected ProgressDialog mLoadDialog;
    private boolean hasLineData = true;

    /**
     * 打开加载框
     */
    protected void openProgressDialog() {
        if (!hasLineData && mLoadDialog != null && !mLoadDialog.isShowing()) {
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

    public LoadDataTask(Context ctx) {
        mLoadDialog = new ProgressDialog(ctx);
        mLoadDialog.setMessage("从本地导入初始化数据,请稍候...");
        this.ctx = ctx;
        tbConfigHandler = new TbConfigHandler(ctx);
        hasLineData = tbConfigHandler.hasLineData();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.openProgressDialog();
    }

    @Override
    protected String doInBackground(String... params) {

        long start = System.currentTimeMillis();
        if (hasLineData) {
            return null;
        }
        tbConfigHandler.saveLineData();
        ReaderFileData.readLineFile(ctx);

        LogUtil.i(TAG, "总耗时:" + (System.currentTimeMillis() - start) / 1000);

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        this.closeProgressDialog();
    }
}
