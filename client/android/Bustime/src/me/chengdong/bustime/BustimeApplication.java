package me.chengdong.bustime;

import me.chengdong.bustime.utils.LogUtil;
import android.app.Application;

/**
 * bustime
 * 
 * @author wanghuaiqiang
 * 
 */
public class BustimeApplication extends Application {
    private final String TAG = BustimeApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        // --- JPush ----
        // JPushInterface.setDebugMode(false);
        // JPushInterface.init(this);

        // String imei = DeviceUtil.getImei(this);
        // LogUtil.d(TAG, "imei[" + imei + "]");
        // Set<String> tags = new HashSet<String>();
        // tags.add(imei);
        // JPushInterface.setAliasAndTags(this, imei, tags);

        MyCrashHandler handler = MyCrashHandler.getInstance();

        handler.init(getApplicationContext());

        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    @Override
    public void onTerminate() {
        LogUtil.d(TAG, "onTerminate");
        super.onTerminate();
    }
}
