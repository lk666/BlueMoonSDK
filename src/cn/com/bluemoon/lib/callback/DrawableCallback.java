package cn.com.bluemoon.lib.callback;

import android.graphics.drawable.Drawable;
public abstract class DrawableCallback {

	public DrawableCallback(){}
	public void onPreExec(){}
	public void onPostExec(){}
	public void onFailure(String errMsg){}
	public void onSuccess(Drawable drawable){}
}
