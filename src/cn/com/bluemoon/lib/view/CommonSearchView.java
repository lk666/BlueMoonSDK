package cn.com.bluemoon.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.bluemoon.lib.callback.CommonEditTextCallBack;
import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.utils.LibPublicUtil;
import cn.com.bluemoon.lib.utils.LibViewUtil;

/**
 * Created by bm on 2016/6/20.
 */
public class CommonSearchView extends LinearLayout implements ListPopView.OnSelectListener {

    private Context context;
    private SearchViewListener listener;
    private CommonClearEditText etSearch;
    private TextView txtSearch;
    private ListView listView;
    private boolean isSearch;
    private String cancel;
    private String search;
    private String hint;
    private int textColor = 0;
    private View emptyView;
    private List<String> listHistory;
    private int maxSize = 5;
    private boolean isSearchEmpty = true;
    private ListPopView popupWindow;
    private LinearLayout layoutTitle;


    public CommonSearchView(Context context) {
        super(context);
        init(context, null);
    }

    public CommonSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CommonSearchView);
        init(context, typedArray);
    }

    private void init(Context context, TypedArray typedArray) {
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layoutInflater.inflate(R.layout.layout_search, this);

        if (typedArray != null) {
            textColor = typedArray.getInt(R.styleable.CommonSearchView_text_color, 0);
            search = typedArray.getString(R.styleable.CommonSearchView_text_ok);
            cancel = typedArray.getString(R.styleable.CommonSearchView_text_cancel);
            hint = typedArray.getString(R.styleable.CommonSearchView_text_hint);
            isSearchEmpty = typedArray.getBoolean(R.styleable.CommonSearchView_search_empty, true);
        }
        etSearch = (CommonClearEditText) this.findViewById(R.id.et_search);
        txtSearch = (TextView) this.findViewById(R.id.txt_search);
        listView = (ListView) this.findViewById(R.id.listView_history);
        etSearch.setOnKeyListener(onKeyListener);
        etSearch.setCallBack(editTextCallBack);
        if (StringUtils.isEmpty(search)) {
            search = context.getString(R.string.btn_ok_space);
        }
        if (StringUtils.isEmpty(cancel)) {
            cancel = context.getString(R.string.btn_cancel_space);
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
        if (popupWindow == null) {
            popupWindow = new ListPopView(context, this);
        }

    }

    public void setEmptyView(View view) {
        emptyView = view;
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
    }

    public String getText() {
        return etSearch.getText().toString().trim();
    }

    public void setText(String text) {
        etSearch.setText(text);
    }

    public void setHint(String hint) {
        etSearch.setHint(hint);
    }

    public List<String> getListHistory() {
        return listHistory;
    }

    public void setListHistory(List<String> listHistory) {
        this.listHistory = listHistory;
    }

    public void search() {
        String str = etSearch.getText().toString().trim();
        if (!isSearchEmpty && StringUtils.isEmpty(str)) {
            LibPublicUtil.showToast(context, context.getString(R.string.search_cannot_empty));
            return;
        }
        etSearch.clearFocus();
        if (listHistory == null) {
            listHistory = new ArrayList<String>();
        }
        addHistory(str);
        if (listener != null) {
            listener.onSearch(this, str);
        }
    }

    public void cancel() {
        etSearch.clearFocus();
        if (listener != null) {
            listener.onCancel(this);
        }
    }

    public void setFocus(boolean isFocus) {
        if (isFocus) {
            etSearch.requestFocus();
        } else {
            etSearch.clearFocus();
        }
    }

    public CommonClearEditText getSearchEdittext() {
        return etSearch;
    }

    public void setSearchEmpty(boolean isSearchEmpty) {
        this.isSearchEmpty = isSearchEmpty;
    }

    public void setHistorySize(int maxSize) {
        this.maxSize = maxSize;
    }

    public List<String> addHistory(String str) {
        if (listHistory == null) {
            listHistory = new ArrayList<String>();
        }
        if (!StringUtils.isEmpty(str.trim())) {
            listHistory.add(0, str);
            for (int i = 1; i < listHistory.size(); i++) {
                if (i >= maxSize || listHistory.get(i).equals(str)) {
                    listHistory.remove(i);
                    i--;
                }
            }
        }
        return listHistory;
    }

    public void showHistoryView() {
        if (popupWindow != null) {
            popupWindow.showPopwindow(this, listHistory);
        }
    }

    public void hideHistoryView() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }

    }

    public View getHistoryView() {
        return listView;
    }

    private void checkSearchState() {
        if (etSearch.getText().toString().length() > 0) {
            if (!txtSearch.getText().toString().equals(search)) {
                txtSearch.setText(search);
            }
            isSearch = true;
        } else {
            if (!txtSearch.getText().toString().equals(cancel)) {
                txtSearch.setText(cancel);
            }
            isSearch = false;
        }
    }

    CommonEditTextCallBack editTextCallBack = new CommonEditTextCallBack() {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            if (etSearch.isFocused()) {
                checkSearchState();
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
                checkSearchState();
                showHistoryView();
            } else {
                LibViewUtil.hideIM(v);
                hideHistoryView();
                if (txtSearch.getVisibility() == View.VISIBLE) {
                    txtSearch.setVisibility(View.GONE);
                }
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

    public void setSearchViewListener(final SearchViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void select(String str) {
        setText(str);
        etSearch.setSelection(str.length());
        search();
    }

    @Override
    public void delete(List<String> list) {
        listHistory = list;
    }

    public interface SearchViewListener {

        void onSearch(CommonSearchView view, String str);

        void onCancel(CommonSearchView view);

    }
}
