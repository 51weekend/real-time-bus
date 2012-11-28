package me.chengdong.bustime.utils;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

/**
 * 错误码常量
 * @author chengdong
 *
 */
public class ErrorCode {
    public static final int SUCCESS = 0;

    public static final int ERR_CODE_NO_NET = -14002111;

    public static final int HTTP_SOCKET_FAIL = -14002112;

    public static final int HTTP_CODE_ERROR = -14002113;

    public static final int HTTP_RESPONSE_ERROR = -14002114;

    public static final int HTTP_EXCEPTION = -14002115;

    public static final int HTTP_RESPONSE_ERROR_WIFI = -14002116;

    public static final int HTTP_AIRPLANE_MODE = -14002117;

    @SuppressLint("UseSparseArrays")
    public static final Map<Integer, String> MAP_ERROR_CODE = new HashMap<Integer, String>();

    static {
        MAP_ERROR_CODE.put(SUCCESS, "成功");

        MAP_ERROR_CODE.put(ERR_CODE_NO_NET, "手机没有联网");
        MAP_ERROR_CODE.put(HTTP_SOCKET_FAIL, "网络连接异常，请检测您的网络是否连接正常"); // "连接不上后台服务"
        MAP_ERROR_CODE.put(HTTP_CODE_ERROR, "网络连接异常，请检测您的网络是否连接正常"); // "后台服务HTTP响应异常"
        MAP_ERROR_CODE.put(HTTP_RESPONSE_ERROR, "网络连接异常，请检测您的网络是否连接正常"); // "后台服务响应数据异常"
                                                                         // http
                                                                         // CODE
                                                                         // !=
                                                                         // 200
        MAP_ERROR_CODE.put(HTTP_EXCEPTION, "网络连接异常，请检测您的网络是否连接正常"); // "HTTP解析异常"
        MAP_ERROR_CODE.put(HTTP_RESPONSE_ERROR_WIFI, "wifi连接异常,请检测你的网络");
        MAP_ERROR_CODE.put(HTTP_AIRPLANE_MODE, "您设置了飞行模式");
    }

}
