/*
 * Copyright (c) 2011 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-06-11.
 */

package me.chengdong.bustime.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import me.chengdong.bustime.module.DownLoadData;
import me.chengdong.bustime.utils.LogUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

/**
 * 升级Task.
 *
 * @author chengdong
 */
public class UpdateTask extends AsyncTask<String, String, String> {
    public static final String TAG = "UpdateTask";

    private Context ctx;
    private ProgressDialog pBar;
    private String apkName;

    public UpdateTask(Context ctx, String apkName) {
        this.ctx = ctx;
        this.apkName = apkName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pBar = new ProgressDialog(ctx);
        pBar.setTitle("正在下载安装文件");
        pBar.setMessage("请稍候...");
        pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pBar.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String url = DownLoadData.SERVER_HOST + DownLoadData.SERVER_CONTEXT + "/apks/" + this.apkName;

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            FileOutputStream fileOutputStream = null;
            if (is != null) {
                File file = new File(Environment.getExternalStorageDirectory() + "/Download/", apkName);
                fileOutputStream = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int ch = -1;
                while ((ch = is.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, ch);
                }
            }
            fileOutputStream.flush();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "下载Apk出错: ", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        pBar.cancel();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/", apkName)),
                "application/vnd.android.package-archive");
        this.ctx.startActivity(intent);
    }
}
