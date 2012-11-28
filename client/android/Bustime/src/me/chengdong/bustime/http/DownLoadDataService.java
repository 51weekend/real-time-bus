/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-28.
 */

package me.chengdong.bustime.http;

import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

/**
 * TODO.
 *
 * @author chengdong
 */
public class DownLoadDataService {

    protected static final String TAG = DownLoadDataService.class.getSimpleName();

    public static final String SERVER_HOST = "http://10.241.41.10:8080";

    public static final String SERVER_CONTEXT = "/bustime";
    public static final String QUERY_LINE_PATH = "/api/queryLine";

    /**
     * 获取指定范围内的动态列表
     * 
     * @param context
     * @param userId
     * @param sinceId
     * @param endId
     * @param pageCount
     * @return
     */
    public static void getLine(Context context, String line) {

        if (!NetworkUtil.isNetworkAvailable(context)) {
            // TODO 提示用户、网络不可用+
            return;
        }

        try {
            String path = SERVER_CONTEXT + QUERY_LINE_PATH;

            String req = "lineNumber=" + line;

            HttpResult httpResult = HttpClientUtil.callServer(context, SERVER_HOST, false, path, req, "UTF-8");

            if ((httpResult != null) && httpResult.isSuccess()) {
                String jsonStr = httpResult.getResponse();
                LogUtil.d(TAG, jsonStr);
                JSONObject jsonObj = new JSONObject(jsonStr);
                int resultCode = jsonObj.optInt("resultCode", -1);
                if (resultCode != 0) {
                    // TODO 按错误提示用户
                    return;
                }

                JSONArray records = (JSONArray) jsonObj.get("data");
                if (records != null && records.length() > 0) {
                    for (int i = 0, len = records.length(); i < len; i++) {
                        JSONObject record = records.getJSONObject(i);
                        record.optString("lineNumber", "");
                        record.optString("lineGuid", "");
                        record.optString("lineInfo", "");

                    }
                }
            }

        } catch (Exception e) {
            LogUtil.e(TAG, "获取服务器端动态列表发生异常:", e);
        }

    }
}
