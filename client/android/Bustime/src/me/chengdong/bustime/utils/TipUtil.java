package me.chengdong.bustime.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 提示工具类
 * 
 * @author daiwei
 *
 */
public class TipUtil {
	public static void tipDescription(Context context, String description) {
		Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
	}

}
