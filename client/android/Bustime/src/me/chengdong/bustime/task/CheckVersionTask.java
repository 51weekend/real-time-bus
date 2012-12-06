/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-6.
 */

package me.chengdong.bustime.task;

import static me.chengdong.bustime.utils.CfgConstant.UPDATE_NOTIFY_ID;
import me.chengdong.bustime.R;
import me.chengdong.bustime.activity.UpdateVersionActivity;
import me.chengdong.bustime.model.Config;
import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.module.DownLoadData;
import me.chengdong.bustime.utils.AppUtil;
import me.chengdong.bustime.utils.ParamUtil;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * TODO.
 *
 * @author chengdong
 */
public class CheckVersionTask extends AsyncTask<Void, Void, Void> {

    public CheckVersionTask(Context context) {
        this.context = context;
    }

    private Context context;

    @Override
    protected Void doInBackground(Void... arg0) {
        ResultData result = DownLoadData.queryConfig(context, "versionCode");
        if (result.isSuccess()) {
            Config config = result.getData() == null ? null : (Config) result.getData();
            if (config != null && Integer.valueOf(config.getConfigValue()) > AppUtil.getVersionCode(context)) {
                // TODO 在服务器上保存apk 文件名、修改读取配置的接口、一次将相关配置全部读到本地
                sendAppUpdateNotification(context, "bustime.apk");
            }
        }
        return null;
    }

    /**
     * 发送App更新通知
     * @param context
     */
    private static void sendAppUpdateNotification(Context context, String apkName) {
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = "APP更新通知";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        CharSequence contentTitle = "APP更新通知";
        CharSequence contentText = "发现新版本,请及时更新!";
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        Intent notificationIntent = new Intent(context, UpdateVersionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ParamUtil.BUNDLE_KEY_APP_APK_NAME, apkName);
        notificationIntent.putExtras(bundle);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        mNotificationManager.notify(UPDATE_NOTIFY_ID, notification);
    }

}
