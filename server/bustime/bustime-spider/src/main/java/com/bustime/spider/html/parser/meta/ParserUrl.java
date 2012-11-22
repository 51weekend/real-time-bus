/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-8-22.
 */

package com.bustime.spider.html.parser.meta;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * urls.
 *
 * @author chengdong
 */
public enum ParserUrl {

    StationUrl("http://www.szjt.gov.cn/apts/default.aspx",
            "/wEWBQLi9afBDgLq+uyKCAKkmJj/DwL0+sTIDgLl5vKEDpw94uqehgsgP25TIVg5cQBBQZj0",
            "/wEPDwULLTE5ODM5MjcxNzlkZAPmAlFZ6B58iSScBgX0og8wpbih"), LineUrl(
            "http://www.szjt.gov.cn/apts/APTSLine.aspx", "/wEWAwKi6d2xDAL88Oh8AqX89aoKpYwmbFse8btxSRSeF1SjYe7FDBo=",
            "/wEPDwUJNDk3MjU2MjgyD2QWAmYPZBYCAgMPZBYCAgEPZBYCAgYPDxYCHgdWaXNpYmxlaGRkZLjIYtHjVvVwuQEcmZdla0sXvFjO"), SingleLineUrl(
            "http://www.szjt.gov.cn/apts/APTSLine.aspx?LineGuid=", "", ""), StationBusUrl(
            "http://www.szjt.gov.cn/apts/default.aspx?StandCode=", "", "");

    private String viewState;
    private String eventValidation;
    private String url;

    ParserUrl(String url, String eventValidation, String viewState) {
        this.url = url;
        this.eventValidation = eventValidation;
        this.viewState = viewState;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getViewState() throws UnsupportedEncodingException {
        return URLEncoder.encode(viewState, "utf-8");
    }

    public void setViewState(String viewState) {
        this.viewState = viewState;
    }

    public String getEventValidation() throws UnsupportedEncodingException {
        return URLEncoder.encode(eventValidation, "utf-8");
    }

    public void setEventValidation(String eventValidation) {
        this.eventValidation = eventValidation;
    }

}
