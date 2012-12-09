/*
 * Copyright (c) 2011 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-06-11.
 */

package me.chengdong.bustime.task;

import me.chengdong.bustime.db.TbConfigHandler;
import me.chengdong.bustime.module.DownLoadData;
import me.chengdong.bustime.module.ReaderFileData;
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

	public LoadDataTask(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		TbConfigHandler tbConfigHandler = new TbConfigHandler(ctx);

		if (!tbConfigHandler.hasStationData()) {
			DownLoadData.downloadStation(ctx);
		}

		if (!tbConfigHandler.hasLineData()) {
			ReaderFileData.readLineFile(ctx);
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
	}
}
