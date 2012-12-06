package me.chengdong.bustime.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * 手机网络工具类.
 * 
 * @author chengdong
 */
public class NetworkUtil {
    public static final String TAG = "NetworkUtil";

    /**
     * 获取手机网络类型
     * 
     * @param ctx
     * @return
     */
    public static String getNetworkType(Context ctx) {
        try {
            // 获取系统的连接服务
            ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取网络的连接情况
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo == null)
                return "";

            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return "WIFI";
            } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                int type = tm.getNetworkType();
                if (type == TelephonyManager.NETWORK_TYPE_UMTS) {
                    return "UMTS";
                } else if (type == TelephonyManager.NETWORK_TYPE_GPRS) {
                    return "GPRS";
                } else if (type == TelephonyManager.NETWORK_TYPE_EDGE) {
                    return "EDGE";
                } else {
                    return "UIM";
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "获取网络类型出错: ", e);
        }
        return "";
    }

    /**
     * 判断是否开启飞行模式
     * 
     * @param context
     * @return
     */
    public static boolean isAirplaneModeOn(Context context) {
        return android.provider.Settings.System.getInt(context.getContentResolver(),
                android.provider.Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    /**
     * 获取用户手机的网络状态
     * 
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo mobile_network = connectivity.getNetworkInfo(0);
        if (mobile_network == null) {
            return false;
        }
        if (mobile_network.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        NetworkInfo wifi_network = connectivity.getNetworkInfo(1);
        return wifi_network.getState() == NetworkInfo.State.CONNECTED;
    }

    /**
     * 判断wifi是否开启（开启不代表连接）
     * 
     * @param context
     * @return
     */
    public static boolean isWifiOn(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager.isWifiEnabled()) {
            return true;
        } else {
            return false;
        }
    }
}
