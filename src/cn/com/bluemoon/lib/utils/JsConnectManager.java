package cn.com.bluemoon.lib.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.util.Log;
import android.webkit.WebView;

import com.loopj.android.http.Base64;

import cn.com.bluemoon.lib.callback.JsConnectCallBack;

public class JsConnectManager {
	
	public static final String URL_BM = "bm://moonMall";
	public static final String URL_ANGEL = "angel://moonMall";
	public static final String URL_SFA = "sfa://moonMall";
	private static final String KEY_METHOD = "method";
	private static final String KEY_URL = "url";
	private static final String KEY_TITLE = "title";
	private static final String KEY_CALLBACK = "callback";
	private static final String VALUE_WEBVIEW = "webview";
	private static final String VALUE_SCAN = "scan";
	private static final String VALUE_CLOSEWEBVIEW = "closeWebView";
	private static final String VALUE_SETTITLE = "setTitle";
	private static final String VALUE_SHOWCUSTOMERSERVICE = "showCustomerService";
	private static final String VALUE_SETAPPINFO = "setAppInfo";
	private static final String VALUE_GET_LOCATION = "getLocation";


	public static HashMap<String, String> getBMJSParams(String url){
		String arg = url.substring(url.indexOf("?")+1,url.length());
		String[] strs = arg.split("&");
		HashMap<String, String> map = new HashMap<String, String>();
		for(int x=0;x<strs.length;x++){
			if(strs[x].indexOf(":") > 0){
				String key = strs[x].substring(0,strs[x].indexOf(":"));
				String value = strs[x].substring(strs[x].indexOf(":")+1);
				if(!StringUtils.isEmpty(key)&&value!=null){
					value = new String(Base64.decode(value.getBytes(), Base64.DEFAULT));
					Log.d("jsConnect", "result ="+key+"="+value);
					map.put(key, value);
				}
			}
		}
		return map;
	}

	public static boolean jsConnect(String start,WebView view,String url,JsConnectCallBack callBack){
//		Log.d("jsConnect", url);
		if (url.startsWith(start)) {
			Map<String,String> map = getBMJSParams(url);
			if (VALUE_WEBVIEW.equals(map.get(KEY_METHOD))) {
				if(callBack!=null){
					callBack.webView(view,map.get(KEY_URL),map.get(KEY_TITLE),map.get(KEY_CALLBACK));
				}
			}else if(VALUE_SCAN.equals(map.get(KEY_METHOD))){
				if(callBack!=null){
					callBack.scan(view,map.get(KEY_TITLE),map.get(KEY_CALLBACK));
				}
			}else if(VALUE_CLOSEWEBVIEW.equals(map.get(KEY_METHOD))){
				if(callBack!=null){
					callBack.closeWebView(view,map.get(KEY_CALLBACK));
				}
			}else if(VALUE_SETTITLE.equals(map.get(KEY_METHOD))){
				if(callBack!=null){
					callBack.setTitle(view,map.get(KEY_TITLE));
				}
			}else if(VALUE_SHOWCUSTOMERSERVICE.equals(map.get(KEY_METHOD))){
				if(callBack!=null){
					callBack.showCustomerService();
				}
			}else if(VALUE_SETAPPINFO.equals(map.get(KEY_METHOD))){
				if(callBack!=null){
					loadJavascript(view,map.get(KEY_CALLBACK),callBack.getAppInfo());
				}
			} else if (VALUE_GET_LOCATION.equals(map.get(KEY_METHOD))) {
				if(callBack!=null){
					callBack.getLoaction(view,map.get(KEY_CALLBACK));
				}
			}
			return true;
		}
		return false;
	}
	
	public static void loadJavascript(WebView view,String method,String param){
		if(StringUtils.isEmpty(method)) return;
		if(StringUtils.isEmpty(param)){
			param = "";
		}else{
			param =  "'" + param + "'";
		}
		String url = "javascript:" + method + "(" + param + ")";
		view.loadUrl(url);
		Log.d("jsConnect", "result =" + url);
	}

	public static void loadJavascript(WebView view,String method){
		loadJavascript(view, method, null);
	}

	public static void keyBack(WebView view){
		loadJavascript(view,"app.util.isCloseWebView");
	}

}
