package me.chengdong.bustime.utils;

import android.util.Log;

/**
 *
 * 日志通用类
 * 
 * @author chengdong
 *
 */
public class LogUtil {
    private static boolean showLog = false;

    public static int v(String tag, String msg) {
        if (showLog) {
            Log.v("Total", tag + " -- " + msg);
            return Log.v(tag, msg);
        } else {
            return 0;
        }
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (showLog) {
            Log.v("Total", tag + " -- " + msg, tr);
            return Log.v(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static int d(String tag, String msg) {
        if (showLog) {
            Log.d("Total", tag + " -- " + msg);
            return Log.d(tag, msg);
        } else {
            return 0;
        }
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (showLog) {
            Log.d("Total", tag + " -- " + msg, tr);
            return Log.d(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static int i(String tag, String msg) {
        if (showLog) {
            Log.i("Total", tag + " -- " + msg);
            return Log.i(tag, msg);
        } else {
            return 0;
        }
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (showLog) {
            Log.i("Total", tag + " -- " + msg, tr);
            return Log.i(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static int w(String tag, String msg) {
        if (showLog) {
            Log.w("Total", tag + " -- " + msg);
            return Log.w(tag, msg);
        } else {
            return 0;
        }
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (showLog) {
            Log.w("Total", tag + " -- " + msg, tr);
            return Log.w(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static int w(String tag, Throwable tr) {
        if (showLog) {
            return Log.w(tag, tr);
        } else {
            return 0;
        }
    }

    public static int e(String tag, String msg) {
        if (showLog) {
            Log.e("Total", tag + " -- " + msg);
            return Log.e(tag, msg);
        } else {
            return 0;
        }
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (showLog) {
            Log.e("Total", tag + " -- " + msg, tr);
            return Log.e(tag, msg, tr);
        } else {
            return 0;
        }
    }
}
