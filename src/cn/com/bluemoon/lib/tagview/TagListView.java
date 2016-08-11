/**
 * 
 */
package cn.com.bluemoon.lib.tagview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.utils.LibViewUtil;


public class TagListView extends FlowLayout implements OnClickListener {

	private Mode mode;
	private boolean mIsDeleteMode;
	private OnTagCheckedChangedListener mOnTagCheckedChangedListener;
	private OnTagClickListener mOnTagClickListener;
	private int backgroundResId = 0;
	private int backgroundCheckedResId = 0;
	private int txtColor = 0;
	private int txtColorChecked = 0;
	private int paddingTop = -1;
	private int paddingLeft = -1;
	private int paddingBottom = -1;
	private int paddingRight = -1;
	/**
	 * @param context
	 */
	public TagListView(Context context) {
		super(context);
		readStyleParameters(context, null);
	}

	private final List<Tag> mTags = new ArrayList<>();

	/**
	 * @param context
	 * @param attributeSet
	 */
	public TagListView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		readStyleParameters(context, attributeSet);
	}

	/**
	 * @param context
	 * @param attributeSet
	 * @param defStyle
	 */
	public TagListView(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);
		readStyleParameters(context, attributeSet);
	}

	@Override
	public void onClick(View v) {
		if ((v instanceof TagView)) {
			LibViewUtil.hideIM(v);
			Tag localTag = (Tag) v.getTag();
			if(Mode.single==mode){
				for(int i=0;i<mTags.size();i++){
					mTags.get(i).setChecked(mTags.get(i).getId()==localTag.getId());
				}
				List<Tag> list = new ArrayList<>();
				list.addAll(mTags);
				setTags(list);
			}

			if (this.mOnTagClickListener != null) {
				this.mOnTagClickListener.onTagClick((TagView) v, localTag);
			}
		}
	}

	private void readStyleParameters(Context context,AttributeSet attributeSet) {
		TypedArray a = context.obtainStyledAttributes(attributeSet,
				R.styleable.FlowLayout);
		try {
			backgroundResId = a.getResourceId(R.styleable.FlowLayout_bg,0);
			backgroundCheckedResId = a.getResourceId(R.styleable.FlowLayout_bgChecked, 0);
			txtColor = a.getColor(R.styleable.FlowLayout_txtColor, 0);
			txtColorChecked = a.getColor(R.styleable.FlowLayout_txtColorChecked, 0);
			paddingTop = a.getDimensionPixelSize(
					R.styleable.FlowLayout_paddingTop, -1);
			paddingLeft = a.getDimensionPixelSize(
					R.styleable.FlowLayout_paddingLeft,-1);
			paddingBottom = a.getDimensionPixelSize(
					R.styleable.FlowLayout_paddingBottom,-1);
			paddingRight = a.getDimensionPixelSize(
					R.styleable.FlowLayout_paddingRight,-1);
		} finally {
			a.recycle();
		}
	}

	private void inflateTagView(final Tag t, boolean b) {

		TagView localTagView = (TagView) View.inflate(getContext(),
				R.layout.tag, null);

		if(paddingLeft != -1||paddingRight!=-1||paddingTop!=-1||paddingBottom!=-1){
			if(paddingLeft==-1) paddingLeft = localTagView.getPaddingLeft();
			if(paddingRight==-1) paddingRight = localTagView.getPaddingRight();
			if(paddingTop==-1) paddingTop = localTagView.getPaddingTop();
			if(paddingBottom==-1) paddingBottom = localTagView.getPaddingBottom();
			localTagView.setPadding(paddingLeft,paddingBottom,paddingRight,paddingBottom);

		}

		localTagView.setText(t.getTitle());
		localTagView.setTag(t);
		
		refreshBgAndTxtColor(localTagView, t);

		if (mIsDeleteMode) {
			int k = (int) TypedValue.applyDimension(1, 5.0F, getContext()
					.getResources().getDisplayMetrics());
			localTagView.setPadding(localTagView.getPaddingLeft(),
					localTagView.getPaddingTop(), k,
					localTagView.getPaddingBottom());
			localTagView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.forum_tag_close, 0);
		}
		
		if ((t.getLeftDrawableResId() > 0) || (t.getRightDrawableResId() > 0)) {
			localTagView.setCompoundDrawablesWithIntrinsicBounds(
					t.getLeftDrawableResId(), 0, t.getRightDrawableResId(), 0);
		}
		localTagView.setCheckEnable(b);
		localTagView.setChecked(t.isChecked());
		localTagView.setOnClickListener(this);
		localTagView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton,
										 boolean paramAnonymousBoolean) {
				t.setChecked(paramAnonymousBoolean);

				refreshBgAndTxtColor((TagView) paramAnonymousCompoundButton, t);
				
				if (TagListView.this.mOnTagCheckedChangedListener != null) {
					TagListView.this.mOnTagCheckedChangedListener
							.onTagCheckedChanged((TagView) paramAnonymousCompoundButton, t);
				}
					}
				});
		addView(localTagView);
	}
	
	private void refreshBgAndTxtColor(TagView tagView,Tag t){
		
		if (txtColor == 0) {
			txtColor = getResources().getColor(R.color.black_light);
		}

		if(txtColorChecked == 0){
			txtColorChecked = getResources().getColor(R.color.orange_red);
		}

		if(backgroundResId == 0){
			backgroundResId = R.drawable.tag_bg_grep;
		}

		if(backgroundCheckedResId == 0){
			backgroundCheckedResId = R.drawable.tag_bg_red;
		}

		if(t.isChecked()){
			if (t.getBackgroundCheckedResId() != 0) {
				tagView.setBackgroundResource(t.getBackgroundCheckedResId());
			}else{
				tagView.setBackgroundResource(backgroundCheckedResId);
			}
			if(t.getTextColorChecked() != 0){
				tagView.setTextColor(t.getTextColorChecked());
			}else{
				tagView.setTextColor(txtColorChecked);
			}
		}else{
			if (t.getBackgroundResId() != 0) {
				tagView.setBackgroundResource(t.getBackgroundResId());
			}else{
				tagView.setBackgroundResource(backgroundResId);
			}
			if (t.getTextColor() != 0) {
				tagView.setTextColor(t.getTextColor());
			}else{
				tagView.setTextColor(txtColor);
			}
		}
	}

	public void addTag(int i, String s) {
		addTag(i, s, true);
	}

	public void addTag(int i, String s, boolean b) {
		addTag(new Tag(i, s), b);
	}

	public void addTag(Tag tag) {
		addTag(tag, true);
	}

	public void addTag(Tag tag, boolean b) {
		mTags.add(tag);
		inflateTagView(tag, b);
	}

	public void addTags(List<Tag> lists) {
		addTags(lists, true);
	}

	public void addTags(List<Tag> lists, boolean b) {
		for (int i = 0; i < lists.size(); i++) {
			addTag(lists.get(i), b);
		}
	}

	public List<Tag> getTags() {
		return mTags;
	}

	public List<Tag> getTagsChecked(){
		List<Tag> list = new ArrayList<>();
		for(int i=0;i<mTags.size();i++){
			if(mTags.get(i).isChecked()){
				list.add(mTags.get(i));
			}
		}
		return list;
	}


	public View getViewByTag(Tag tag) {
		return findViewWithTag(tag);
	}

	public void removeTag(Tag tag) {
		mTags.remove(tag);
		removeView(getViewByTag(tag));
	}

	public void setDeleteMode(boolean b) {
		mIsDeleteMode = b;
	}

	public void setOnTagCheckedChangedListener(
			OnTagCheckedChangedListener onTagCheckedChangedListener) {
		mOnTagCheckedChangedListener = onTagCheckedChangedListener;
	}

	public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
		mOnTagClickListener = onTagClickListener;
	}

	public void setBackgroundRes(int res) {
		backgroundResId = res;
	}

	public void setBackgroundCheckedRes(int res) {
		backgroundCheckedResId = res;
	}

	public void seTextColor(int res) {
		txtColor = res;
	}
	
	public void setTextColorChecked(int res) {
		txtColorChecked = res;
	}
	
	

	public void setTags(List<? extends Tag> lists) {
		setTags(lists, true);
	}

	public void setTags(List<? extends Tag> lists, boolean b) {
		removeAllViews();
		mTags.clear();
		for (int i = 0; i < lists.size(); i++) {
			addTag(lists.get(i), b);
		}
	}

	public static abstract interface OnTagCheckedChangedListener {
		public abstract void onTagCheckedChanged(TagView tagView, Tag tag);
	}

	public static abstract interface OnTagClickListener {
		public abstract void onTagClick(TagView tagView, Tag tag);
	}

	public void setMode(Mode mode){
		this.mode = mode;
	}
    
    public enum Mode{
		single,multiple
	}

}
