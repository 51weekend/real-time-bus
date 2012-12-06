/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-28.
 */

package me.chengdong.bustime.module;

import static me.chengdong.bustime.model.ResultData.DECODE_JSON_ERROR;
import static me.chengdong.bustime.model.ResultData.NETWORK_DISABLED;
import static me.chengdong.bustime.model.ResultData.NO_DATA;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.http.HttpClientUtil;
import me.chengdong.bustime.http.HttpResult;
import me.chengdong.bustime.model.Config;
import me.chengdong.bustime.model.Line;
import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.model.SingleLine;
import me.chengdong.bustime.model.Station;
import me.chengdong.bustime.model.StationBus;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
    public static final String QUERY_LINE_PATH = SERVER_CONTEXT + "/api/queryLine";
    public static final String QUERY_SINGLE_LINE_PATH = SERVER_CONTEXT + "/api/querySingleLine";
    public static final String QUERY_STATION_PATH = SERVER_CONTEXT + "/api/queryStation";
    public static final String QUERY_STATION_BUS_PATH = SERVER_CONTEXT + "/api/queryStationBus";
    public static final String QUERY_CONFIG_PATH = SERVER_CONTEXT + "/api/queryConfig";

    /**
     * 获取车次信息
     * 
     * @param context
     * @param lineNumber
     * @return
     */
    public ResultData getLine(Context context, String lineNumber) {

        ResultData result = getDataFromRemote(context, QUERY_LINE_PATH, "lineNumber=" + lineNumber);
        if (result.isFailed()) {
            return result;
        }
        List<Line> lines = new ArrayList<Line>();

        try {
            JSONArray records = (JSONArray) result.getData();
            for (int i = 0, len = records.length(); i < len; i++) {
                Line line = new Line();
                line.deserialize(records.getJSONObject(i));
                lines.add(line);
            }
        } catch (JSONException e) {
            result.setResultCode(DECODE_JSON_ERROR);
            return result;
        }

        result.setData(lines);
        return result;

    }

    public ResultData getSingleLine(Activity context, String lineGuid) {

        ResultData result = getDataFromRemote(context, QUERY_SINGLE_LINE_PATH, "lineCode=" + lineGuid);
        if (result.isFailed()) {
            return result;
        }
        List<SingleLine> lines = new ArrayList<SingleLine>();
        try {
            JSONArray records = (JSONArray) result.getData();
            for (int i = 0, len = records.length(); i < len; i++) {
                SingleLine singleLine = new SingleLine();
                singleLine.deserialize(records.getJSONObject(i));
                lines.add(singleLine);

            }
        } catch (Exception e) {
            result.setResultCode(DECODE_JSON_ERROR);
            return result;
        }

        result.setData(lines);

        return result;

    }

    public ResultData getStation(Activity context, String stationName) {

        ResultData result = getDataFromRemote(context, QUERY_STATION_PATH, "stationName=" + stationName);
        if (result.isFailed()) {
            return result;
        }
        List<Station> stations = new ArrayList<Station>();
        try {
            JSONArray records = (JSONArray) result.getData();

            for (int i = 0, len = records.length(); i < len; i++) {
                Station station = new Station();
                station.deserialize(records.getJSONObject(i));
                stations.add(station);

            }
        } catch (Exception e) {
            result.setResultCode(DECODE_JSON_ERROR);
            return result;
        }
        result.setData(stations);

        return result;
    }

    public ResultData getStationBus(Activity context, String stationCode) {

        List<StationBus> stationBuses = new ArrayList<StationBus>();
        ResultData result = getDataFromRemote(context, QUERY_STATION_BUS_PATH, "stationCode=" + stationCode);
        if (result.isFailed()) {
            return result;
        }

        try {
            JSONArray records = (JSONArray) result.getData();
            for (int i = 0, len = records.length(); i < len; i++) {
                StationBus station = new StationBus();
                station.deserialize(records.getJSONObject(i));
                stationBuses.add(station);
            }
        } catch (Exception e) {
            result.setResultCode(DECODE_JSON_ERROR);
            return result;
        }
        result.setData(stationBuses);
        return result;
    }

    public static ResultData queryConfig(Context context, String key) {

        ResultData result = getDataFromRemote(context, QUERY_CONFIG_PATH, "key=" + key);
        if (result.isFailed()) {
            return result;
        }
        JSONObject json = (JSONObject) result.getData();

        Config config = new Config();
        config.deserialize(json);
        result.setData(config);
        return result;

    }

    private static ResultData getDataFromRemote(Context context, String path, String req) {
        ResultData result = new ResultData();
        if (!NetworkUtil.isNetworkAvailable(context)) {
            result.setResultCode(NETWORK_DISABLED);
        }

        HttpResult httpResult = HttpClientUtil.callServer(context, SERVER_HOST, false, path, req, "UTF-8");

        if (!httpResult.isSuccess()) {
            result.setResultCode(httpResult.getCode());
            return result;
        }
        String jsonStr = httpResult.getResponse();
        try {

            JSONObject jsonObj = new JSONObject(jsonStr);
            int resultCode = jsonObj.optInt("resultCode", -1);
            if (resultCode != 0) {
                result.setResultCode(resultCode);
                return result;
            }
            if (jsonObj.get("data") == null) {
                result.setResultCode(NO_DATA);
                return result;
            }
            if ("null".equals(jsonObj.get("data"))) {
                result.setResultCode(NO_DATA);
                return result;
            } else {
                result.setData(jsonObj.get("data"));
            }
        } catch (Exception e) {
            result.setResultCode(DECODE_JSON_ERROR);
            LogUtil.e(TAG, "json error", e);
        }

        return result;
    }
}
