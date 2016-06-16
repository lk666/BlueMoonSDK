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
import cn.com.bluemoon.lib.utils.LibLogUtils;
import cn.com.bluemoon.lib.utils.LibPublicUtil;
import cn.com.bluemoon.lib.utils.LibStringUtil;

public class CommonEditText extends EditText {
	
	private String TAG ="CommonEditText";

	private int cursorPos;

	private String tmp;

	private boolean resetText;

	private boolean isFirstSetText = true;

	private int limitMode = -1;

	private CommonEditTextCallBack mCallback;

	private Drawable mDrawable;

	private Drawable mDrawable2;
	private Context mContext;

	private boolean isCleanable = false;

	private int maxLength = 500;

	public final static int MODE_NAME = 0;

	public final static int MODE_EMOJI = 1;


	public void setCallBack(CommonEditTextCallBack mCallback) {
		this.mCallback = mCallback;
	}

	public CommonEditText(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public CommonEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	public CommonEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public void init() {

		TextWatcher textWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()>maxLength)
				{
					if(mCallback != null)
					{
						mCallback.outOfLengthShow(mContext, maxLength);
					}else{
						LibPublicUtil.showToast(mContext,String.format(mContext.getString(R.string.error_max_limit),maxLength));
					}
					resetText = true;
					 setText(tmp);
					 setSelection(cursorPos);
					 invalidate();
				}

				updateCleanable(length(), true);

				if(mCallback != null)
				{
					mCallback.afterTextChanged(s);
				}
					
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

				if (!resetText) {
					cursorPos = getSelectionEnd();
					tmp = s.toString();
				}else{
					resetText = false;
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if(limitMode!=-1)
				{
					if (!resetText && s.toString().length() - tmp.length() > 0) {
						 String str = s.toString().substring(cursorPos, 
								 cursorPos+ s.toString().length() - tmp.length());
						 LibLogUtils.i(TAG, "str:"+str);

						 if(MODE_NAME == limitMode)
						 {
							 if (str.contains("-") || str.contains("_") 
									 || str.contains(mContext.getResources().getString(R.string.limit_char_1))|| str.contains(mContext.getResources().getString(R.string.limit_char_2))) {
								 str = str.replaceAll("-", "");
								 str = str.replaceAll("_", "");
								 str = str.replaceAll(mContext.getResources().getString(R.string.limit_char_1), "");
								 str = str.replaceAll(mContext.getResources().getString(R.string.limit_char_2), "");
							 }
							 str.replaceAll(" ", "?");
							 char[] chars = str.toCharArray();
							 for (int i = 0; i < chars.length; i++) {
								String ss = String.valueOf(chars[i]);
								 if(!LibStringUtil.isChinese(ss) && !LibStringUtil.isNumeric(ss)
										 && !LibStringUtil.isLetter(ss)) {
									 LibPublicUtil.showToast(mContext, mContext.getString(R.string.content_no_name));
									 resetText = true;
									 setText(tmp);
									 setSelection(cursorPos);
									 invalidate();
									 break;
								 } 
							 }
							  
								 
						 }

						 else if(MODE_EMOJI == limitMode)
						 {
							 if(LibStringUtil.containsEmoji(str))
							 {
								 LibPublicUtil.showToast(mContext, mContext.getString(R.string.content_no_emoji));
								 resetText = true;
								 setText(tmp);
								 setSelection(cursorPos);
								 invalidate();
							 }
						 }
						 
					} else {
						resetText = false;
					}
				}
			}
		};
		this.addTextChangedListener(textWatcher);
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				updateCleanable(length(), hasFocus);

				if(mCallback!=null){
					mCallback.onFocusChange(v,hasFocus);
				}
			}
		});
		
	}


	public void updateCleanable(int length, boolean hasFocus){
		if(isCleanable)
		{
			if(length() > 0 && hasFocus)
			{				
				setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], null, mDrawable, null);
			}
			else
			{
				setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], null, mDrawable2, null);
			}
		}
	}	

	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		if(text==null)
		{
			return;
		}

		if(isFirstSetText)
		{
			if(limitMode == MODE_NAME)
			{
				
				text = getStringForName(text.toString());
			}
			else if(limitMode == MODE_EMOJI)
			{
				text = LibStringUtil.getStringNotEmoji(text.toString());
			}
			isFirstSetText = false;
		}
		super.setText(text, type);
	}
	

	public void setRightDrawable(Drawable d)
	{
		setRightDrawable(d, null);
	}
	

	public void setRightDrawable(Drawable d,Drawable d2)
	{
		this.mDrawable = d;
		this.mDrawable2 = d2;
		if(isCleanable)
		{
			updateCleanable(length(),isFocused());
		}
		else
		{
			setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], null, d, null);
		}
	}

	public void isCleanable(boolean isCleanable)
	{
		this.isCleanable = isCleanable;
	}

	public void setLimitMode(int limitMode)
	{
		this.limitMode = limitMode;
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
					mCallback.onDrawableRightClick(this,isCleanable);
				}else{
					if(isCleanable)setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		mDrawable = null;
		mDrawable2 = null;
		super.finalize();
	}
	
	
	  String getStringForName(String source) {
        String str = "";
        source = source.replaceAll(" ", "");
        char[] chars = source.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String codePoint = String.valueOf(chars[i]);

            if (LibStringUtil.isChinese(codePoint) || LibStringUtil.isNumeric(codePoint) || LibStringUtil.isLetter(codePoint)
                    || "-".equalsIgnoreCase(codePoint) || "_".equalsIgnoreCase(codePoint)
                    ||getResources().getString(R.string.limit_char_1).equalsIgnoreCase(codePoint) 
                    || getResources().getString(R.string.limit_char_2).equalsIgnoreCase(codePoint)) {
                str += codePoint;
            } else {
                str += "";
            }
        }
        return str;
    }
}

