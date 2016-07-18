package cn.com.bluemoon.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import cn.com.bluemoon.lib.qrcode.R;

public class RedpointTextView extends TextView {

	public RedpointTextView(Context context) {
		super(context);
		init();
	}
	
	public RedpointTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		
		setGravity(Gravity.CENTER);
		
		this.addTextChangedListener(new TextWatcher() {
			
			@SuppressLint("NewApi")
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String num = s.toString();
				LayoutParams params = getLayoutParams();
				GradientDrawable grad = null ;
				try {
					grad = (GradientDrawable)getBackground();
				} catch (Exception e) {
					try {
						ColorDrawable color = (ColorDrawable)getBackground();
						grad = new GradientDrawable();
						grad.setColor(color.getColor());
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				if(grad==null){
					grad = new GradientDrawable();
					grad.setColor(0xfffc5500);
				}
				if(num.length()<3){
					params.width = params.height;
					setPadding(0, 0,0, 0);
					grad.setShape(GradientDrawable.OVAL);
				}else{
					params.width = LayoutParams.WRAP_CONTENT;
					setMinWidth(params.height);
					setPadding(getResources().getDimensionPixelOffset(R.dimen.point_padding_left), 0, 
							getResources().getDimensionPixelOffset(R.dimen.point_padding_right), 0);
					grad.setShape(GradientDrawable.RECTANGLE);
					grad.setCornerRadius((float)params.height/(float)2);
				}
				int sdk = android.os.Build.VERSION.SDK_INT;
				if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					setBackgroundDrawable(grad);
				} else {
					setBackground(grad);
				}
				setLayoutParams(params);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
