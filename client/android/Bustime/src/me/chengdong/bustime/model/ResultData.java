package me.chengdong.bustime.model;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

/**
 * 返回结果封装.
 *
 * @author chengdong
 */
public class ResultData {

    public static final int PARAMETER_ERROR = -14002001;
    public static final int NO_DATA = -14002002;
    public static final int SERVER_ERROR = -14002003;

    public static final int NETWORK_DISABLED = -14002101;
    public static final int DECODE_JSON_ERROR = -14002102;
    
    public static final int SUCCESS = 0;

    public static final int ERR_CODE_NO_NET = -14002111;

    public static final int HTTP_SOCKET_FAIL = -14002112;

    public static final int HTTP_CODE_ERROR = -14002113;

    public static final int HTTP_RESPONSE_ERROR = -14002114;

    public static final int HTTP_EXCEPTION = -14002115;

    public static final int HTTP_RESPONSE_ERROR_WIFI = -14002116;

    public static final int HTTP_AIRPLANE_MODE = -14002117;


    private int code = 0;
    private Object data;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, String> MAP_ERROR_CODE = new HashMap<Integer, String>();

    public boolean success() {
        return 0 == code;
    }

    public boolean failed() {
        return !success();
    }

    public void setResultCode(int code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public String getMessage() {
        return MAP_ERROR_CODE.get(code);
    }

    static {
        MAP_ERROR_CODE.put(PARAMETER_ERROR, "请求参数不正确");
        MAP_ERROR_CODE.put(NO_DATA, "无数据");
        MAP_ERROR_CODE.put(SERVER_ERROR, "服务端出现异常");
        MAP_ERROR_CODE.put(NETWORK_DISABLED, "手机无法连接网络!");
        MAP_ERROR_CODE.put(DECODE_JSON_ERROR, "解析json数据出错!");
        
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
