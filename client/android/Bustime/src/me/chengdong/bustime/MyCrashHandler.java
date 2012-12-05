package me.chengdong.bustime;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import me.chengdong.bustime.activity.MainActivity;
import me.chengdong.bustime.utils.LogUtil;
import android.content.Context;
import android.content.Intent;

public class MyCrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "MyCrashHandler";

    // 需求是 整个应用程序 只有一个 MyCrash-Handler
    private static MyCrashHandler myCrashHandler;
    private Context context;

    // 1.私有化构造方法
    private MyCrashHandler() {

    }

    public static synchronized MyCrashHandler getInstance() {
        if (myCrashHandler != null) {
            return myCrashHandler;
        } else {
            myCrashHandler = new MyCrashHandler();
            return myCrashHandler;
        }
    }

    public void init(Context context) {
        this.context = context;
    }

    public void uncaughtException(Thread arg0, Throwable arg1) {
        String errorInfo = getErrorInfo(arg1);
        LogUtil.e(TAG, errorInfo);

        if (this.context != null) {

            Intent intent = new Intent(this.context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(intent);
        }

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /** 
     * 获取错误的信息  
     * @param arg1 
     * @return 
     */
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

}
