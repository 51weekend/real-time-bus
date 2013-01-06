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

import me.chengdong.bustime.db.TbConfigHandler;
import me.chengdong.bustime.db.TbLineHandler;
import me.chengdong.bustime.db.TbStationHandler;
import me.chengdong.bustime.http.HttpClientUtil;
import me.chengdong.bustime.http.HttpResult;
import me.chengdong.bustime.model.CodeValue;
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
public class DownloadData {

    protected static final String TAG = DownloadData.class.getSimpleName();
    public static final String SERVER_HOST = "http://42.121.117.61:6068";

    public static final String SERVER_CONTEXT = "/bustime/api";
    public static final String QUERY_LINE_PATH = SERVER_CONTEXT + "/queryLine";
    public static final String QUERY_SINGLE_LINE_PATH = SERVER_CONTEXT + "/querySingleLine";
    public static final String QUERY_RUN_SINGLE_LINE_PATH = SERVER_CONTEXT + "/queryRunSingleLine";
    public static final String QUERY_STATION_PATH = SERVER_CONTEXT + "/queryStation";
    public static final String QUERY_STATION_BUS_PATH = SERVER_CONTEXT + "/queryStationBus";
    public static final String QUERY_CONFIG_PATH = SERVER_CONTEXT + "/queryConfig";
    public static final String DOWNLOAD_STATION_PATH = SERVER_CONTEXT + "/downloadStation";
    public static final String DOWNLOAD_LINE_PATH = SERVER_CONTEXT + "/downloadLine";

    /**
     * 获取车次信息
     * 
     * @param context
     * @param lineNumber
     * @return
     */
    public static ResultData getLine(Context context, String lineNumber) {

        ResultData result = getDataFromRemote(context, QUERY_LINE_PATH, "lineNumber=" + lineNumber);
        if (result.failed()) {
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

    public static ResultData getSingleLine(Activity context, String lineGuid) {

        ResultData result = getDataFromRemote(context, QUERY_SINGLE_LINE_PATH, "lineCode=" + lineGuid);
        if (result.failed()) {
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

    public static ResultData getRunSingleLine(Activity context, String lineGuid) {

        ResultData result = getDataFromRemote(context, QUERY_RUN_SINGLE_LINE_PATH, "lineCode=" + lineGuid);
        if (result.failed()) {
            return result;
        }
        List<CodeValue> runData = new ArrayList<CodeValue>();
        try {
            JSONArray records = (JSONArray) result.getData();
            for (int i = 0, len = records.length(); i < len; i++) {
                CodeValue codeValue = new CodeValue();
                codeValue.deserialize(records.getJSONObject(i));
                runData.add(codeValue);

            }
        } catch (Exception e) {
            result.setResultCode(DECODE_JSON_ERROR);
            return result;
        }

        result.setData(runData);

        return result;
    }

    public static ResultData getStation(Activity context, String stationName) {

        ResultData result = getDataFromRemote(context, QUERY_STATION_PATH, "stationName=" + stationName);
        if (result.failed()) {
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

    public static void downloadStation(Activity context) {

        ResultData result = getDataFromRemote(context, DOWNLOAD_STATION_PATH, "");
        if (result.failed()) {
            return;
        }
        try {
            TbStationHandler tbStationHandler = new TbStationHandler(context);
            TbConfigHandler tbConfigHandler = new TbConfigHandler(context);
            String records = result.getData().toString();
            String[] stationArray = records.split("\\$");
            for (String stationString : stationArray) {
                tbStationHandler.saveOrUpdate(stationString);
            }
            tbConfigHandler.saveStationData();
        } catch (Exception e) {
            LogUtil.e(TAG, "download station data error", e);
        }

    }

    public static void downloadLine(Activity context) {

        ResultData result = getDataFromRemote(context, DOWNLOAD_LINE_PATH, "");
        if (result.failed()) {
            return;
        }
        try {
            TbLineHandler tbLineHandler = new TbLineHandler(context);
            TbConfigHandler tbConfigHandler = new TbConfigHandler(context);
            String records = result.getData().toString();
            String[] lineArray = records.split("\\$");
            for (String lineString : lineArray) {
                tbLineHandler.saveOrUpdate(lineString);
            }
            tbConfigHandler.saveLineData();
        } catch (Exception e) {
            LogUtil.e(TAG, "download line data error", e);
        }

    }

    public static ResultData getStationBus(Activity context, String stationCode) {

        List<StationBus> stationBuses = new ArrayList<StationBus>();
        ResultData result = getDataFromRemote(context, QUERY_STATION_BUS_PATH, "stationCode=" + stationCode);
        if (result.failed()) {
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

    public static ResultData queryConfigByKey(Context context, String key) {

        ResultData result = getDataFromRemote(context, QUERY_CONFIG_PATH, "key=" + key);
        if (result.failed()) {
            return result;
        }
        JSONObject json = (JSONObject) result.getData();

        Config config = new Config();
        config.deserialize(json);
        result.setData(config);
        return result;

    }

    public static ResultData queryConfigByType(Context context, String type) {

        ResultData result = getDataFromRemote(context, QUERY_CONFIG_PATH, "type=" + type);
        if (result.failed()) {
            return result;
        }

        List<Config> configs = new ArrayList<Config>();
        try {
            JSONArray records = (JSONArray) result.getData();
            for (int i = 0, len = records.length(); i < len; i++) {
                Config station = new Config();
                station.deserialize(records.getJSONObject(i));
                configs.add(station);
            }
        } catch (Exception e) {
            result.setResultCode(DECODE_JSON_ERROR);
            return result;
        }
        result.setData(configs);
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
