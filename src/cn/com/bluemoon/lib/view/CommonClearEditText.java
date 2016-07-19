package cn.com.bluemoon.lib.view;

import android.content.Context;
import android.util.AttributeSet;

import cn.com.bluemoon.lib.qrcode.R;


public class CommonClearEditText extends CommonEditText{
	
	private Context mContext;
	private boolean isChangeRightPlace = true;

	public CommonClearEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		setMode();
	}
	
	public CommonClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		setMode();
	}
	
	public CommonClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		setMode();
	}
	

	private void setMode()
	{
		updateRightDrawable();
		isCleanable(true);
		updateCleanable(length(),isFocused());
	}
	

	private void updateRightDrawable()
	{
		if(isChangeRightPlace)
		{
			setRightDrawable(mContext.getResources().getDrawable(R.drawable.login_delete_normal));
		}
		else
		{
			setRightDrawable(mContext.getResources().getDrawable(R.drawable.login_delete_normal),
					mContext.getResources().getDrawable(R.drawable.login_delete_null));
		}
	}
	

	public void isChangeRightPlace(boolean isChangeRightPlace)
	{
		this.isChangeRightPlace = isChangeRightPlace;
		updateRightDrawable();
	}

}
