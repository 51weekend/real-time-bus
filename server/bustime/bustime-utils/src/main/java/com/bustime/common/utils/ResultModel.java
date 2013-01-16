package com.bustime.common.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 返回结果封装.
 *
 * @author chengdong
 */
public class ResultModel extends HashMap<String, Object> implements Serializable {

    public static final int PARAMETER_ERROR = -14002001;
    public static final int URL_DECODER_ERROR = -14002003;
    public static final int DATA_FORMAT_ERROR = -14002004;

    public static final int NO_DATA = -14002005;
    public static final int SERVER_ERROR = -14002050;

    public static final int SAVE_FILE_ERROR = -14002099;

    private static final long serialVersionUID = 1L;

    private static Map<Integer, String> errorMessage = new HashMap<Integer, String>();

    private static String resultCode = "resultCode";
    private static String resultMsg = "resultMsg";

    public boolean isSuccess() {
        return "0".equals(this.get(resultCode).toString());
    }

    public boolean isFailed() {
        return !isSuccess();
    }

    public ResultModel() {
        this.put(resultCode, 0);
        this.put(resultMsg, "success");
    }

    public int getResultCode() {
        return Integer.valueOf(get(resultCode).toString());
    }

    public void setResultCode(int code) {
        this.put(resultCode, code);
        this.put(resultMsg, errorMessage.get(Integer.valueOf(code)));
    }

    public void setData(Object data) {
        this.put("data", data);
        if (this.get("timestamp") == null) {
            this.put("timestamp", new Date());
        }
    }

    public String getResultMsg() {
        return get(resultMsg).toString();
    }

    public void setTimestamp(Date timestamp) {
        if (timestamp == null) {
            this.put("timestamp", new Date());
        } else {
            this.put("timestamp", timestamp);
        }
    }

    public void setTimestamp() {
        this.setTimestamp(null);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    static {
        // TODO 移动到配置文件中去
        errorMessage.put(PARAMETER_ERROR, "请求参数不正确");
        errorMessage.put(URL_DECODER_ERROR, "URLDecoder字符串出错");
        errorMessage.put(DATA_FORMAT_ERROR, "数据格式错误,服务端已升级,请确认数据格式");
        errorMessage.put(NO_DATA, "无数据");
        errorMessage.put(SERVER_ERROR, "服务端出现异常");
        errorMessage.put(SAVE_FILE_ERROR, "保存文件失败");
    }

}
