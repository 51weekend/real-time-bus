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
        InputStream inputStream = context.getResources().openRawResource(R.raw.line);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            TbLineHandler tbLineHandler = new TbLineHandler(context);
            while (true) {
                String lineString = reader.readLine();
                if (StringUtil.isEmpty(lineString)) {
                    break;
                }
                tbLineHandler.saveOrUpdate(lineString);
            }

        } catch (Exception e) {
            LogUtil.e(TAG, "reader the file error", e);
        } finally {
            try {

                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e2) {
                LogUtil.e(TAG, "close file error", e2);
            }
        }

    }

}
