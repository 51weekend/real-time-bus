/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.bustime.web.controller;

import static com.bustime.common.utils.ResultModel.PARAMETER_ERROR;
import static com.bustime.common.utils.ResultModel.SERVER_ERROR;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bustime.common.logger.LoggerUtils;
import com.bustime.common.utils.ResultModel;
import com.bustime.core.service.ApiService;

/**
 * TODO.
 *
 * @author chengdong
 */
@Controller
public class ApiController {

    @Autowired
    private ApiService apiService;

    @RequestMapping
    @ResponseBody
    public ResultModel queryLine(@RequestParam(value = "lineNumber", required = false) String lineNumber) {
        ResultModel result = new ResultModel();
        if (StringUtils.isEmpty(lineNumber)) {
            result.setResultCode(PARAMETER_ERROR);
            return result;
        }
        // 线路信息基本不会变化、如果本地有、就不去远程爬取,客户端也可以做缓存
        try {
            result.setData(apiService.queryLine(URLDecoder.decode(lineNumber, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            LoggerUtils.error("decode lineNumber error", e);
            result.setResultCode(SERVER_ERROR);
        }
        return result;
    }

    /**
     * 站点信息做缓存、不用每次都返回给客户端、节省流量、只返回车辆所在站台及进站时间
     * @param lineCode
     * @return
     */
    @RequestMapping
    @ResponseBody
    public ResultModel querySingleLine(@RequestParam(value = "lineCode", required = false) String lineCode) {
        ResultModel result = new ResultModel();
        if (StringUtils.isEmpty(lineCode)) {
            result.setResultCode(PARAMETER_ERROR);
            return result;
        }
        result.setData(apiService.querySingleLine(lineCode));
        return result;
    }

    @RequestMapping
    @ResponseBody
    public ResultModel queryStation(@RequestParam(value = "stationName", required = false) String stationName) {
        ResultModel result = new ResultModel();
        if (StringUtils.isEmpty(stationName)) {
            result.setResultCode(PARAMETER_ERROR);
            return result;
        }
        try {
            result.setData(apiService.queryStation(URLDecoder.decode(stationName, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * trend,lineInfo,lineGuid,standCode 可以缓存.
     * busNumber,standNum,time 必须实时获取
     * @param stationCode 站台编号 如:CVZ
     * @return
     */
    @RequestMapping
    @ResponseBody
    public ResultModel queryStationBus(@RequestParam(value = "stationCode", required = false) String stationCode) {
        ResultModel result = new ResultModel();
        if (StringUtils.isEmpty(stationCode)) {
            result.setResultCode(PARAMETER_ERROR);
            return result;
        }
        result.setData(apiService.queryStationBus(stationCode));
        return result;
    }

}
