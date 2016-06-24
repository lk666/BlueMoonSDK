package cn.com.bluemoon.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import cn.com.bluemoon.lib.callback.CommonEditTextCallBack;
import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.utils.LibViewUtil;

/**
 * Created by bm on 2016/6/20.
 */
public class CommonSearchView1 extends LinearLayout {

    private Context context;
//    private float density;
    private SearchViewListener listener;
    private CommonClearEditText etSearch;
    private TextView txtSearch;
    private boolean isSearch;
    private String cancel;
    private String search;
    private String hint;
    private int textColor = 0;


    public CommonSearchView1(Context context) {
        super(context);
        init(context, null);
    }

    public CommonSearchView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CommonSearchView);
        init(context, typedArray);
    }

    private void init(Context context, TypedArray typedArray) {
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layoutInflater.inflate(R.layout.layout_search1, this);
//        screenDefault();

        if (typedArray != null) {
            textColor = typedArray.getInt(R.styleable.CommonSearchView_text_color, 0);
            search = typedArray.getString(R.styleable.CommonSearchView_text_ok);
            cancel = typedArray.getString(R.styleable.CommonSearchView_text_cancel);
            hint = typedArray.getString(R.styleable.CommonSearchView_text_hint);
        }

        etSearch = (CommonClearEditText) this.findViewById(R.id.et_search);
        txtSearch = (TextView) this.findViewById(R.id.txt_search);
        etSearch.setOnKeyListener(onKeyListener);
        etSearch.setCallBack(editTextCallBack);
        if (StringUtils.isEmpty(search)) {
            search = context.getString(R.string.btn_ok);
        }
        if (StringUtils.isEmpty(cancel)) {
            cancel = context.getString(R.string.btn_cancel);
        }
        if (!StringUtils.isEmpty(hint)) {
            etSearch.setHint(hint);
        }
        if (textColor != 0) {
            txtSearch.setTextColor(textColor);
        }
        txtSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearch) {
                    search();
                } else {
                    cancel();
                }
            }
        });

    }

    public void setText(String text) {
        etSearch.setText(text);
    }

    public void setHint(String hint) {
        etSearch.setHint(hint);
    }

    public void search(){
        etSearch.clearFocus();
        String str = etSearch.getText().toString();
        if(listener!=null) listener.onSearch(str);
    }

    public void cancel(){
        etSearch.clearFocus();
        if(listener!=null) listener.onCancel();
    }

    public void setFocus(boolean isFocus){
        if(isFocus){
            etSearch.requestFocus();
        }else{
            etSearch.clearFocus();
        }
    }

    CommonEditTextCallBack editTextCallBack = new CommonEditTextCallBack() {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            if (etSearch.isFocused() && etSearch.getText().toString().length() > 0) {
                if (!txtSearch.getText().toString().equals(search)) {
                    txtSearch.setText(search);
                }
                isSearch = true;
            } else if (etSearch.isFocused() && etSearch.getText().toString().length() == 0) {
                if (!txtSearch.getText().toString().equals(cancel)) {
                    txtSearch.setText(cancel);
                }
                isSearch = false;
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            super.onFocusChange(v, hasFocus);
            if (hasFocus) {
                if (txtSearch.getVisibility() == View.GONE) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.activity_translate_right);
                    txtSearch.setAnimation(animation);
                    txtSearch.setVisibility(View.VISIBLE);
                }
            } else {
                LibViewUtil.hideIM(v);
                if (txtSearch.getVisibility() == View.VISIBLE) {
                    txtSearch.setVisibility(View.GONE);
                }
            }
            if(listener!=null){
                listener.onFocusChange(v,hasFocus);
            }
        }
    };

    OnKeyListener onKeyListener = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                search();
                return true;
            }
            return false;
        }
    };


    /*private void screenDefault() {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();

        windowManager.getDefaultDisplay().getMetrics(dm);

        density = dm.density;
    }

    public int getPx(int dip) {
        return (int) (dip * density);
    }*/

    public void setSearchViewListener(final SearchViewListener listener) {
        this.listener = listener;
    }

    public interface SearchViewListener {

        void onSearch(String str);

        void onCancel();

        void onFocusChange(View v, boolean hasFocus);
    }
}
