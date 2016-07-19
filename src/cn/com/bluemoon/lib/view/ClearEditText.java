package cn.com.bluemoon.lib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import cn.com.bluemoon.lib.callback.CommonEditTextCallBack;
import cn.com.bluemoon.lib.qrcode.R;

public class ClearEditText extends EditText {
	

	private CommonEditTextCallBack mCallback;

	private Drawable mDrawable;

	private Drawable mDrawable2;
	private Context mContext;

	private int maxLength = 1000;


	public void setCallBack(CommonEditTextCallBack mCallback) {
		this.mCallback = mCallback;
	}

	public ClearEditText(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public void init() {
		mDrawable = mContext.getResources().getDrawable(R.drawable.login_delete_normal);
		TextWatcher textWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > maxLength) {
					if (mCallback != null) {
						mCallback.outOfLengthShow(mContext, maxLength);
					}
					setText(s.toString().substring(0, maxLength));
					setSelection(maxLength);
					invalidate();
				}

				updateCleanable(length(), true);

				if (mCallback != null) {
					mCallback.afterTextChanged(s);
				}
					
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if(mCallback!=null) 
					mCallback.beforeTextChanged(s, start, count, after);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(mCallback!=null) 
					mCallback.onTextChanged(s, start, before, count);
			}
		};
		this.addTextChangedListener(textWatcher);
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				updateCleanable(length(), hasFocus);
			}
		});
		
	}


	public void updateCleanable(int length, boolean hasFocus){
		if(length() > 0 && hasFocus)
		{				
			setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], null, mDrawable, null);
		}
		else
		{
			setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], null, mDrawable2, null);
		}	
	}	
	

	public void isChangeRightPlace(boolean isChangeRightPlace)
	{
		mDrawable = mContext.getResources().getDrawable(R.drawable.login_delete_normal);
		if(isChangeRightPlace)
		{
			mDrawable2 = null;
		}
		else
		{
			mDrawable2 = mContext.getResources().getDrawable(R.drawable.login_delete_null);
		}
		updateCleanable(length(), isFocused());
	}	

	public void setMaxLength(int maxLength)
	{
		this.maxLength = maxLength;
		this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength+1)});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int DRAWABLE_RIGHT = 2;

		Drawable rightIcon = getCompoundDrawables()[DRAWABLE_RIGHT];
		if (rightIcon != null && event.getAction() == MotionEvent.ACTION_UP) {

			int leftEdgeOfRightDrawable = getRight() - getPaddingRight()
					- rightIcon.getBounds().width();
			if (event.getRawX() >= leftEdgeOfRightDrawable) {
				
				if(mCallback!=null)
				{
					mCallback.onDrawableRightClick(this,true);
				}else{
					setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		mDrawable2 = null;
		super.finalize();
	}
	
}

