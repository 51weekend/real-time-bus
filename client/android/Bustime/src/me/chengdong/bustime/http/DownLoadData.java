/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-28.
 */

package me.chengdong.bustime.http;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.activity.SingleLineActivity;
import me.chengdong.bustime.model.Line;
import me.chengdong.bustime.model.SingleLine;
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
public class DownLoadData {

    protected static final String TAG = DownLoadData.class.getSimpleName();

    public static final String SERVER_HOST = "http://dev.ll.sdo.com";

    public static final String SERVER_CONTEXT = "/api";
    public static final String QUERY_LINE_PATH = "/api/queryLine";
    public static final String QUERY_SINGLE_LINE_PATH = "/api/querySingleLine";

    /**
     * 获取车次信息
     * @param context
     * @param lineNumber
     * @return
     */
    public static List<Line> getLine(Context context, String lineNumber) {

        List<Line> lines = new ArrayList<Line>();
        if (!NetworkUtil.isNetworkAvailable(context)) {
            // TODO 提示用户、网络不可用+
            return lines;
        }

        try {
            String path = SERVER_CONTEXT + QUERY_LINE_PATH;

            String req = "lineNumber=" + lineNumber;

            HttpResult httpResult = HttpClientUtil.callServer(context, SERVER_HOST, false, path, req, "UTF-8");

            if ((httpResult == null) || !httpResult.isSuccess()) {
                return lines;
            }
            String jsonStr = httpResult.getResponse();
            LogUtil.d(TAG, jsonStr);
            JSONObject jsonObj = new JSONObject(jsonStr);
            int resultCode = jsonObj.optInt("resultCode", -1);
            if (resultCode != 0) {
                // TODO 按错误提示用户
                return lines;
            }

            JSONArray records = (JSONArray) jsonObj.get("data");

            if (records == null || records.length() == 0) {
                return lines;
            }
            for (int i = 0, len = records.length(); i < len; i++) {
                Line line = new Line();
                line.deserialize(records.getJSONObject(i));
                lines.add(line);
            }

        } catch (Exception e) {
            LogUtil.e(TAG, "从服务器端获取车次信息发生异常:", e);
        }

        return lines;

    }

    public static List<SingleLine> getSingleLine(SingleLineActivity context, String lineGuid) {
        List<SingleLine> lines = new ArrayList<SingleLine>();
        if (!NetworkUtil.isNetworkAvailable(context)) {
            // TODO 提示用户、网络不可用+
            return lines;
        }

        try {
            String path = SERVER_CONTEXT + QUERY_SINGLE_LINE_PATH;

            String req = "lineCode=" + lineGuid;

            HttpResult httpResult = HttpClientUtil.callServer(context, SERVER_HOST, false, path, req, "UTF-8");

            if ((httpResult == null) || !httpResult.isSuccess()) {
                return lines;
            }
            String jsonStr = httpResult.getResponse();
            LogUtil.d(TAG, jsonStr);
            JSONObject jsonObj = new JSONObject(jsonStr);
            int resultCode = jsonObj.optInt("resultCode", -1);
            if (resultCode != 0) {
                return lines;
            }

            JSONArray records = (JSONArray) jsonObj.get("data");

            if (records == null || records.length() == 0) {
                return lines;
            }
            for (int i = 0, len = records.length(); i < len; i++) {
                SingleLine singleLine = new SingleLine();
                singleLine.deserialize(records.getJSONObject(i));
                lines.add(singleLine);

            }

        } catch (Exception e) {
            LogUtil.e(TAG, "从服务器获取车次运行信息异常:", e);
        }

        return lines;
    }
}
