
package me.chengdong.bustime.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class DeviceUtil {
	private static final String TAG = "DeviceUtil";
	
	private static String deviceId = "";
	
    /**
     * 16位id
     * 
     * @param ctx
     * @return
     */
    public static final String getDeviceId(Context ctx) {
        try{
            if(!StringUtil.isBlank(deviceId)){
                return deviceId;
            }
            deviceId =  Secure.getString(
                    ctx.getContentResolver(), Secure.ANDROID_ID);
            
            if(deviceId==null)
                return "";
            
            return deviceId;
        }catch(Exception e){
            return "";
        }
    }
    
    /**
	 * 获取imei
	 * @param ctx
	 * @return
	 */
	public static String getImei(Context ctx) {
		try {
	    	TelephonyManager telephonyManager=(TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
	    	String imei = telephonyManager.getDeviceId();
	    	return imei;
		} catch (Exception e) {
	        LogUtil.e(TAG, "从TelephonyManager中获取IMEI出错: ", e);
	    }
		return null;
	}
	
	/**
	 * 直接拨号
	 * 
	 * @param ctx
	 * @param phoneNum
	 */
	public static void dialCallDirect(Context ctx, String phoneNum) {
	    if (StringUtil.isEmpty(phoneNum)) {
	        return;
	    }
	    
	    Uri uri = Uri.parse("tel:" + phoneNum);
	    Intent intent = new Intent(Intent.ACTION_CALL, uri);
	    ctx.startActivity(intent);
	}
}
