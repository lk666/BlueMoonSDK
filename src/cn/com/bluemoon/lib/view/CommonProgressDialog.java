package cn.com.bluemoon.lib.view;

import android.app.ProgressDialog;
import android.content.Context;

import cn.com.bluemoon.lib.qrcode.R;

public class CommonProgressDialog extends ProgressDialog{

	public CommonProgressDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}
	
	public CommonProgressDialog(Context context) {
		super(context);
		init(context);
		
	}
	
	private void init(Context context){
		setMessage(context.getResources().getString(
				R.string.data_loading));
	}
	
	@Override
	public void show() {
		try {
			super.show();
			setContentView(R.layout.dialog_progress);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		try {
			if(isShowing()){
				super.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
