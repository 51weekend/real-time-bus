package me.chengdong.bustime.module;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.chengdong.bustime.R;
import me.chengdong.bustime.db.TbConfigHandler;
import me.chengdong.bustime.db.TbLineHandler;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.StringUtil;
import android.content.Context;

public class ReaderFileData {

	private static final String TAG = ReaderFileData.class.getSimpleName();

	public static void readLineFile(Context context) {

		InputStream lineFile = context.getResources().openRawResource(
				R.raw.line);
		BufferedReader lineReader = null;
		try {
			lineReader = new BufferedReader(new InputStreamReader(lineFile,
					"UTF-8"));

			TbLineHandler tbLineHandler = new TbLineHandler(context);
			TbConfigHandler tbConfigHandler = new TbConfigHandler(context);
			while (true) {
				String lineString = lineReader.readLine();
				if (StringUtil.isEmpty(lineString)) {
					break;
				}
				String[] line = lineString.split("\\|");
				tbLineHandler.saveOrUpdate(line[0], line[1], line[2], line[3],
						line[4]);
			}
			tbConfigHandler.saveLineData();
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
			} catch (IOException e) {
				LogUtil.e(TAG, "close line file error", e);
			}
		}
	}
}
