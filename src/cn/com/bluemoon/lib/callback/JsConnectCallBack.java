package cn.com.bluemoon.lib.callback;

import android.webkit.WebView;

/**
 * Created by bm on 2016/4/26.
 */
public abstract class JsConnectCallBack {
    public void webView(WebView view,String url,String title,String callbackName){}
    public void scan(WebView view,String title,String callbackName){}
    public void closeWebView(WebView view,String callbackName){}
    public void setTitle(WebView view,String title){}
    public void showCustomerService(){}
    public abstract String getAppInfo();
    public void getLoaction(WebView view,String callbackName){}
    public String getCacheSize(WebView view){return "";}
    public void cleanCache(WebView view){}
    public void cleanCache(WebView view, String version){}
    public void logout(WebView view){}
    public void share(WebView view,String topic,String content,String picUrl,String url){}
}
