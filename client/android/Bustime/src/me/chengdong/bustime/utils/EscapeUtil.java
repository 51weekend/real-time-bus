package me.chengdong.bustime.utils;

/**
 * Escape工具类
 * @author wanghuaiqiang
 *
 */
public class EscapeUtil {
	public static String escape(String s) {
    	if (s == null) {
    		return "";
    	}
    	String ret = s;
    	ret = ret.replaceAll("\\%", "%25");
    	ret = ret.replaceAll("\r", "%20");
    	ret = ret.replaceAll("\n", "%20");
    	
    	ret = ret.replaceAll("\\+", "%2B");
    	ret = ret.replaceAll("\\ ", "%20");
    	ret = ret.replaceAll("\\/", "%2F");
    	ret = ret.replaceAll("\\?", "%3F");
    	ret = ret.replaceAll("\\=", "%3D");
    	ret = ret.replaceAll("\\&", "%26");
    	ret = ret.replaceAll("\\#", "%23");
    	return ret;
    }
}
