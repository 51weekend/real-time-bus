/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-6.
 */

package me.chengdong.bustime.task;

import static me.chengdong.bustime.utils.CfgConstant.UPDATE_NOTIFY_ID;

import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.activity.UpdateVersionActivity;
import me.chengdong.bustime.db.TbConfigHandler;
import me.chengdong.bustime.model.Config;
import me.chengdong.bustime.model.ResultData;
import me.chengdong.bustime.module.DownloadData;
import me.chengdong.bustime.utils.AppUtil;
import me.chengdong.bustime.utils.ParamUtil;
import me.chengdong.bustime.utils.StringUtil;
import android.app.Activity;
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

    public CheckVersionTask(Activity context) {
        this.context = context;
    }

    private Activity context;

    @Override
    protected Void doInBackground(Void... arg0) {
        TbConfigHandler tbConfigHandler = new TbConfigHandler(context);
        if (!tbConfigHandler.checkUpdate()) {
            return null;
        }
        ResultData result = DownloadData.queryConfigByKey(context, "versionCode");
        if (result.failed()) {
            return null;
        }
        Config config = result.getData() == null ? null : (Config) result.getData();
        if (config == null || AppUtil.getVersionCode(context) >= Integer.valueOf(config.getConfigValue())) {
            return null;
        }
        result = DownloadData.queryConfigByType(context, "appVersion");
        if (result.failed()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        List<Config> configs = (List<Config>) result.getData();
        String versionName = "";
        String versionIntro = "";
        String apkName = "";
        for (Config conf : configs) {
            if (conf.getConfigKey().equals("versionName")) {
                versionName = conf.getConfigValue();
            }
            if (conf.getConfigKey().equals("versionIntro")) {
                versionIntro = conf.getConfigValue();
            }
            if (conf.getConfigKey().equals("apkName")) {
                apkName = conf.getConfigValue();
            }

        }
        if (StringUtil.isNotEmpty(versionName) && StringUtil.isNotEmpty(versionIntro) && StringUtil.isNotEmpty(apkName)) {
            sendAppUpdateNotification(context, apkName, versionName, versionIntro);
        }
        return null;
    }

    /**
     * 发送App更新通知
     * @param context
     */
    private static void sendAppUpdateNotification(Context context, String apkName, String versionName,
            String versionIntro) {
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = "新版本更新通知";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        CharSequence contentTitle = "新版本更新通知";
        CharSequence contentText = "发现新版本" + versionName + ",请更新!";
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        Intent notificationIntent = new Intent(context, UpdateVersionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ParamUtil.BUNDLE_VERSION_INTRO, versionIntro);
        bundle.putString(ParamUtil.BUNDLE_VERSION_NAME, versionName);
        bundle.putString(ParamUtil.BUNDLE_APK_NAME, apkName);
        notificationIntent.putExtras(bundle);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        mNotificationManager.notify(UPDATE_NOTIFY_ID, notification);
    }

}
