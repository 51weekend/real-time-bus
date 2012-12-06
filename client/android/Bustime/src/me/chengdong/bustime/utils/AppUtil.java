package me.chengdong.bustime.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 跟app相关的工具类
 * 
 * @author chengdong
 * 
 */
public class AppUtil {
    private static final String TAG = "AppUtil";

    public static int getVersionCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            LogUtil.e(TAG, "获取本应用版本号出错: ", e);
        }
        return verCode;
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static final String getPkg(Context ctx) {
        return ctx.getPackageName();
    }

    public static final String getAppMetaData(Context context, String key) {
        if (context == null) {
            return "";
        }
        String channel = "";
        try {
            ApplicationInfo appi = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Object tmpObj = (Object) appi.metaData.get(key);
            if (tmpObj != null) {
                channel = tmpObj.toString();
            }
            return channel;
        } catch (Exception e) {
            LogUtil.e(TAG, "获取app meta数据出错：", e);
        }
        return "";
    }
}
