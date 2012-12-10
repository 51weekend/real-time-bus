package me.chengdong.bustime.module;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.chengdong.bustime.R;
import me.chengdong.bustime.db.TbLineHandler;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.StringUtil;
import android.content.Context;

public class ReaderFileData {

    private static final String TAG = ReaderFileData.class.getSimpleName();

    public static void readLineFile(Context context) {
        InputStream lineFile = context.getResources().openRawResource(R.raw.line);
        BufferedReader lineReader = null;

        try {
            lineReader = new BufferedReader(new InputStreamReader(lineFile, "UTF-8"));

            TbLineHandler tbLineHandler = new TbLineHandler(context);
            while (true) {
                String lineString = lineReader.readLine();
                if (StringUtil.isEmpty(lineString)) {
                    break;
                }
                tbLineHandler.saveOrUpdate(lineString);
            }

        } catch (Exception e) {
            LogUtil.e(TAG, "reader the line file error", e);
        } finally {
            try {

                if (lineReader != null) {
                    lineReader.close();
                }
                if (lineFile != null) {
                    lineFile.close();
                }
            } catch (Exception e2) {
                LogUtil.e(TAG, "close line file error", e2);
            }
        }

    }

}
