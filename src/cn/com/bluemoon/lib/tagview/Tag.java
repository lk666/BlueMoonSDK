package cn.com.bluemoon.lib.tagview;

import java.io.Serializable;

public class Tag implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2684657309332033242L;
	
	private int backgroundResId ;
	private int backgroundCheckedResId ;
	private int textColor ;
	private int textColorChecked ;
	private int id;
	private boolean isChecked;
	private int leftDrawableResId ;
	private int rightDrawableResId ;
	private String key;
	private String title;

	public Tag() {
		
	}

	public Tag(int keyInt, String paramString) {
		this.id = keyInt;
		this.title = paramString;
	}
	
	public Tag(String keyString, String paramString) {
		this.key = keyString;
		this.title = paramString;
	}

	public int getBackgroundCheckedResId() {
		return backgroundCheckedResId;
	}

	public void setBackgroundCheckedResId(int backgroundCheckedResId) {
		this.backgroundCheckedResId = backgroundCheckedResId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getBackgroundResId() {
		return this.backgroundResId;
	}

	public int getId() {
		return this.id;
	}

	public int getLeftDrawableResId() {
		return this.leftDrawableResId;
	}

	public int getRightDrawableResId() {
		return this.rightDrawableResId;
	}

	public String getTitle() {
		return this.title;
	}

	public boolean isChecked() {
		return this.isChecked;
	}

	public void setBackgroundResId(int paramInt) {
		this.backgroundResId = paramInt;
	}

	public void setChecked(boolean paramBoolean) {
		this.isChecked = paramBoolean;
	}

	public void setId(int paramInt) {
		this.id = paramInt;
	}

	public void setLeftDrawableResId(int paramInt) {
		this.leftDrawableResId = paramInt;
	}

	public void setRightDrawableResId(int paramInt) {
		this.rightDrawableResId = paramInt;
	}

	public void setTitle(String paramString) {
		this.title = paramString;
	}

	public int getTextColorChecked() {
		return textColorChecked;
	}

	public void setTextColorChecked(int textColorChecked) {
		this.textColorChecked = textColorChecked;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
}
