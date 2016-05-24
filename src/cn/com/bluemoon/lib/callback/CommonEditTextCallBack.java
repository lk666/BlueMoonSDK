package cn.com.bluemoon.lib.callback;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public abstract class CommonEditTextCallBack {


	public void afterTextChanged(Editable s){};

	public void onTextChanged(CharSequence s, int start, int before,
			int count){};
	

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after){};			
			

	public void onDrawableRightClick(EditText et,boolean isCleanable){
		et.setText("");
	};
	

	public void outOfLengthShow(Context context,int maxLength){}

	public void onFocusChange(View v, boolean hasFocus){};
}
