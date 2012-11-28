/*
 * Copyright (c) 2011 Shanda Corporation. All rights reserved.
 *
 * Created on 2011-11-28.
 */

package me.chengdong.bustime.http;

import java.util.Map;

import me.chengdong.bustime.utils.ErrorCode;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.NetworkUtil;
import me.chengdong.bustime.utils.StringUtil;
import android.content.Context;

/**
 * 跟HttpClient相关的工具类.
 *
 * @author wanghuaiqiang
 */
public class HttpClientUtil {
    private static final String TAG = "HttpClientUtil";

    public static HttpResult callServer(Context ctx, String host, boolean isHttps, String path, String req,
            String encoding) {

        if (StringUtil.isBlank(NetworkUtil.getNetworkType(ctx))) {
            if (NetworkUtil.isAirplaneModeOn(ctx)) {
                return new HttpResult(ErrorCode.HTTP_AIRPLANE_MODE);
            }
            return new HttpResult(ErrorCode.ERR_CODE_NO_NET);
        }
        long start = System.currentTimeMillis();
        HttpResult httpResult = HttpClientTools.httpGet(ctx, host, path, req, encoding);
        LogUtil.d(TAG, "http request cost time:" + (System.currentTimeMillis() - start));
        if (httpResult == null) {
            return new HttpResult(ErrorCode.HTTP_EXCEPTION, "resp null");
        }
        LogUtil.i(TAG, "[" + host + path + "?" + req + "] return[" + httpResult + "]");
        return httpResult;
    }

    public static HttpResult submitByPost(Context ctx, String url, Map<String, String> paramsMap) {
        if (StringUtil.isBlank(NetworkUtil.getNetworkType(ctx))) {
            if (NetworkUtil.isAirplaneModeOn(ctx)) {
                return new HttpResult(ErrorCode.HTTP_AIRPLANE_MODE);
            }
            return new HttpResult(ErrorCode.ERR_CODE_NO_NET);
        }

        HttpResult httpResult = HttpClientTools.httpPost(ctx, url, paramsMap);
        if (httpResult == null) {
            return new HttpResult(ErrorCode.HTTP_EXCEPTION, "resp null");
        }
        LogUtil.i(TAG, "" + url + ", params[" + paramsMap + "] return[" + httpResult + "]");
        return httpResult;
    }
}
