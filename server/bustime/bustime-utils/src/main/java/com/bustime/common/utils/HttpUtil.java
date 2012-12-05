/*
 * Copyright (c) 2011 Shanda Corporation. All rights reserved.
 *
 * Created on 2011-12-9.
 */

package com.bustime.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

/**
 * HTTP接口访问工具类.
 *
 * @author daiwei02
 */
public class HttpUtil {

    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 从指定的URL地址读取数据.
     * 
     * @param strUrl
     *            url地址,若有参数，格式为?param1={0}&param2={1}
     * @param charSet
     *            指定的编码
     * @param params
     *            参数
     * @return 指定编码的字符串
     */
    public static String readStringFromUrl(String strUrl, String charSet, Object... params) {
        return readStringFromUrl(strUrl, charSet, 0, params);
    }

    /**
     * 从指定的URL地址读取数据.
     * 
     * @param strUrl
     *            url地址,若有参数，格式为?param1={0}&param2={1}
     * @param charSet
     *            指定的编码
     * @param timeout
     *            超时时间
     * @param params
     *            参数
     * @return 指定编码的字符串
     */
    public static String readStringFromUrl(String strUrl, String charSet, int timeout, Object... params) {
        if (charSet == null || charSet.length() == 0) {
            charSet = DEFAULT_CHARSET;
        }
        String ret = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = getInputStreamFromUrl(strUrl, timeout, params);
            if (in == null) {
                return ret;
            }
            out = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            in.close();
            byte[] content = out.toByteArray();
            ret = new String(content, charSet);
        } catch (MalformedURLException me) {
        } catch (IOException ioe) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return ret;
    }

    /**
     * 从指定的URL地址读取数据流.
     * @param strUrl url地址,若有参数，格式为?param1={0}&param2={1}
     * @param params
     *            参数
     * @param timeout
     *            超时时间
     * @return 数据流
     */
    public static InputStream getInputStreamFromUrl(String strUrl, int timeout, Object... params) {
        InputStream in = null;
        try {
            strUrl = MessageFormat.format(strUrl, params);
            URL url = new URL(strUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);
            in = urlConnection.getInputStream();
        } catch (MalformedURLException me) {
        } catch (IOException ioe) {
        }
        return in;
    }
}
