package me.chengdong.bustime.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.NetworkUtil;
import me.chengdong.bustime.utils.StringUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class HttpClientTools {
    private static final String TAG = "HttpClientTools";

    private static boolean cmwapDoCmnet = false;

    /**
     * 默认超时时间（毫秒数）
     */
    private static int DEFAULT_TIMEOUT_MILLISECOND = 30000;

    /**
     * get方式获取信息
     * @param ctx
     * @param protocolHostPort
     * @param path
     * @param req
     * @param encoding
     * @return
     */
    public static HttpResult httpGet(Context ctx, String protocolHostPort, String path, String req, String encoding) {
        return httpGet(ctx, protocolHostPort, path, req, encoding, DEFAULT_TIMEOUT_MILLISECOND);
    }

    /**
     * get方式获取信息
     * @param ctx
     * @param protocolHostPort
     * @param path
     * @param req
     * @param encoding
     * @param timeoutMillisecond
     * @return
     */
    public static HttpResult httpGet(Context ctx, String protocolHostPort, String path, String req, String encoding,
            int timeoutMillisecond) {
        String hostPort = "";
        boolean isHttps = false;
        boolean cmnet = true; // 接入点为cmnet还是cmwap
        if (protocolHostPort.startsWith("https://")) {
            hostPort = protocolHostPort.substring(8);
            isHttps = true;
        } else if (protocolHostPort.startsWith("http://")) {
            hostPort = protocolHostPort.substring(7);
            isHttps = false;
        } else {
            hostPort = protocolHostPort;
            protocolHostPort = "http://" + protocolHostPort;
            isHttps = false;
        }

        String url = chkReplaceHostServer(ctx, protocolHostPort, cmnet) + path + "?" + req;
        try {
            HttpResult httpResult = requestGetData(ctx, url, isHttps, hostPort, encoding, cmnet, timeoutMillisecond);

            return httpResult;
        } catch (Exception e) {
            LogUtil.e(TAG, "error: ", e);
            return new HttpResult(ResultData.HTTP_EXCEPTION, e.getMessage());
        }
    }

    /**
     * 检查是否需要替换地址前缀
     * @param ctx
     * @param protocolHostPort
     * @param cmnet
     * @return
     */
    private static String chkReplaceHostServer(Context ctx, String protocolHostPort, boolean cmnet) {
        // 先设定一下，如果是wap的话将会用wap特定的
        // 所以如果是https这里就会设为https
        cmnet = true;

        if (ctx == null) {
            LogUtil.e(TAG, "ctx is null");
            return protocolHostPort;
        }

        String apnType = null;
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo == null || !netInfo.isAvailable()) {
                return protocolHostPort;
            }
            String typeName = netInfo.getTypeName();
            String extra = netInfo.getExtraInfo();
            if (typeName.equalsIgnoreCase("MOBILE")) {
                if (extra == null) {
                    apnType = "unknown";
                } else {
                    apnType = extra;
                }
                if (extra == null) {
                    return protocolHostPort;
                }
                if (extra.toLowerCase().startsWith("cmwap") || extra.toLowerCase().startsWith("uniwap")
                        || extra.toLowerCase().startsWith("3gwap")) {
                    protocolHostPort = (cmwapDoCmnet) ? protocolHostPort : "http://10.0.0.172:80";
                    cmnet = false;
                    LogUtil.i(TAG, "切换到cmwap/uniwap/3gwap网络");
                } else if (extra.startsWith("#777")) {
                    Uri apn_uri = Uri.parse("content://telephony/carriers/preferapn");
                    Cursor c = ctx.getContentResolver().query(apn_uri, null, null, null, null);
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        String ctapn = c.getString(c.getColumnIndex("user"));
                        if (ctapn != null && !ctapn.equals("")) {
                            if (ctapn.startsWith("ctwap")) {
                                protocolHostPort = (cmwapDoCmnet) ? protocolHostPort : "http://10.0.0.200:80";
                                cmnet = false;
                                LogUtil.i(TAG, "切换到ctwap网络");
                            } else if (ctapn.toLowerCase().startsWith("wap")) {
                                protocolHostPort = (cmwapDoCmnet) ? protocolHostPort : "http://10.0.0.200:80";
                                cmnet = false;
                                LogUtil.i(TAG, "切换到ctwap网络");
                            } else if (ctapn.startsWith("ctnet")) {
                            } else if (ctapn.toLowerCase().startsWith("card")) {
                            }
                        }
                    }
                }
            } else if (typeName.equalsIgnoreCase("WIFI") || typeName.equalsIgnoreCase("WI FI")) {
                apnType = "wifi";
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "error", e);
        }
        LogUtil.i(TAG, "apntype: " + apnType);
        return protocolHostPort;
    }

    /**
     * 当设置了https并且现在时net或者wifi网络时候采用https
     * 否则普通的httpclient
     * @param isHttps
     * @param cmnet
     * @return
     */
    private static HttpClient getHttpClient(boolean isHttps, boolean cmnet) {
        if (isHttps && cmnet) {
            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            return httpClient;

        } else {
            return new DefaultHttpClient();
        }
    }

    /**
     * 发送http get 请求
     * @param http
     * @param hostPort
     * @param isHttps
     * @param cmnet
     * @param timeoutMillisecond
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private static HttpResponse sendGetData(HttpGet http, String hostPort, boolean isHttps, boolean cmnet,
            int timeoutMillisecond) throws ClientProtocolException, IOException {
        if (!cmnet && !cmwapDoCmnet) {
            http.addHeader("X-Online-Host", hostPort);
        }
        HttpClient client = getHttpClient(isHttps, cmnet);
        HttpParams params = client.getParams();
        // 连接超时：
        HttpConnectionParams.setConnectionTimeout(params, timeoutMillisecond);
        // Socket超时：
        HttpConnectionParams.setSoTimeout(params, timeoutMillisecond);
        return client.execute(http); // 发送请求并获取反馈
    }

    /**
     * 
     * @param ctx
     * @param url
     * @param isHttps
     * @param hostPort
     * @param encoding
     * @param cmnet
     * @param timeoutMillisecond
     * @return 
     */
    private static HttpResult requestGetData(Context ctx, String url, boolean isHttps, String hostPort,
            String encoding, boolean cmnet, int timeoutMillisecond) {
        HttpGet http = null;
        try {
            http = new HttpGet(url);

            HttpResponse httpResponse = sendGetData(http, hostPort, isHttps, cmnet, timeoutMillisecond);
            String contentType = null;
            if (httpResponse != null && httpResponse.getFirstHeader("Content-Type") != null) {
                contentType = httpResponse.getFirstHeader("Content-Type").getValue();
            }
            if (contentType != null && contentType.indexOf("text/vnd.wap.wml") != -1) {// 过滤移动资费页面
                LogUtil.i(TAG, "移动资费页面，过滤！");
                http.abort();
                http = null;
                http = new HttpGet(url);
                httpResponse = sendGetData(http, hostPort, isHttps, cmnet, timeoutMillisecond);
            }

            int code = 0;
            if (httpResponse != null && httpResponse.getStatusLine() != null) {
                code = httpResponse.getStatusLine().getStatusCode();
            }
            if (code == 200) {
                return new HttpResult(ResultData.SUCCESS, getResponseString(httpResponse, encoding));
            } else {
                LogUtil.i(TAG, "访问失败");
                if (code == 499 && !cmnet && !cmwapDoCmnet) {
                    cmwapDoCmnet = true;
                }
                return new HttpResult(ResultData.HTTP_CODE_ERROR, "http_code_" + code + "_resp_"
                        + getResponseString(httpResponse, encoding));
            }
        } catch (IOException e) {
            if (!cmnet && cmwapDoCmnet) {
                cmwapDoCmnet = false;
            }
            LogUtil.e(TAG, "联网发生异常:", e);
            return new HttpResult(ResultData.HTTP_SOCKET_FAIL, e.getMessage());
        } catch (Exception e) {
            LogUtil.e(TAG, "发生异常:", e);
            return new HttpResult(ResultData.HTTP_EXCEPTION, e.getMessage());
        } finally {
            if (http != null)
                try {
                    http.abort();
                } catch (Exception e) {
                    LogUtil.e(TAG, "发生异常：", e);
                }
            http = null;
        }
    }

    /**
     * 按照指定的编码获取http response
     * @param httpResponse
     * @param encoding
     * @return
     */
    private static String getResponseString(HttpResponse httpResponse, String encoding) {
        try {
            return EntityUtils.toString(httpResponse.getEntity(), encoding);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 提交参数里有文件的数据
     * @param ctx
     * @param url 服务器地址
     * @param paramsMap 
     * @return 
     */
    public static HttpResult httpPost(Context ctx, String url, Map<String, String> paramsMap) {
        // 检查网络情况
        if (ctx != null) {
            if (StringUtil.isBlank(NetworkUtil.getNetworkType(ctx))) {
                if (NetworkUtil.isAirplaneModeOn(ctx)) {
                    return new HttpResult(ResultData.HTTP_AIRPLANE_MODE);
                }
                return new HttpResult(ResultData.ERR_CODE_NO_NET);
            }
        }

        StringBuffer sb = new StringBuffer();
        try {
            HttpPost post = new HttpPost(url);
            MultipartEntity entity = new MultipartEntity();
            if (paramsMap != null && !paramsMap.isEmpty()) {
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
                }
            }
            post.setEntity(entity);

            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse response = httpClient.execute(post);
            int stateCode = response.getStatusLine().getStatusCode();
            if (stateCode == HttpStatus.SC_OK) {
                HttpEntity result = response.getEntity();
                if (result != null) {
                    InputStream is = result.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String tempLine;
                    while ((tempLine = br.readLine()) != null) {
                        sb.append(tempLine);
                    }
                }
                return new HttpResult(ResultData.SUCCESS, sb.toString());
            }
            post.abort();
        } catch (Exception e) {
            LogUtil.e(TAG, "http post error: ", e);
        }
        return new HttpResult(ResultData.HTTP_CODE_ERROR, sb.toString());
    }

}
