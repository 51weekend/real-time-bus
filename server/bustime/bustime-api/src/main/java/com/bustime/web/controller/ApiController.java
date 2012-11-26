/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.bustime.web.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
            result.setResultCode(ResultModel.PARAMETER_ERROR);
            return result;
        }
        result.setData(apiService.queryLine(lineNumber));
        return result;
    }

    @RequestMapping
    @ResponseBody
    public ResultModel querySingleLine(@RequestParam(value = "lineCode", required = false) String lineCode) {
        ResultModel result = new ResultModel();
        if (StringUtils.isEmpty(lineCode)) {
            result.setResultCode(ResultModel.PARAMETER_ERROR);
            return result;
        }
        result.setData(apiService.querySingleLine(lineCode));
        return result;
    }

}
