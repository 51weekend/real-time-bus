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

import com.bustime.core.utils.ResultModel;
import com.bustime.spider.html.parser.LineParser;

/**
 * TODO.
 *
 * @author chengdong
 */
@Controller
public class LineController {

    @Autowired
    private LineParser lineParser;

    @RequestMapping
    @ResponseBody
    public ResultModel query(@RequestParam(value = "lineNumber", required = false) String lineNumber) {
        ResultModel result = new ResultModel();
        if (StringUtils.isEmpty(lineNumber)) {
            result.setResultCode(ResultModel.PARAMETER_ERROR);
            return result;
        }
        result.setData(lineParser.getLines(lineNumber));
        return result;
    }

}
