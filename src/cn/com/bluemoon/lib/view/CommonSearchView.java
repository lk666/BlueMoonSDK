package cn.com.bluemoon.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.bluemoon.lib.callback.CommonEditTextCallBack;
import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.utils.LibViewUtil;

/**
 * Created by bm on 2016/6/20.
 */
public class CommonSearchView extends LinearLayout {

    private Context context;
    //    private int width;
    private float density;
    private SearchViewListener listener;
    private CommonClearEditText etSearch;
    private TextView txtSearch;
    private ListView listView;
    private boolean isSearch;
    private String cancel;
    private String search;
    private String hint;
    private int textColor = 0;
    private CommonAdapter adapter;
    private View emptyView;
    private List<String> listHistory;
    private int maxSize = 5;


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
        screenDefault();

        if (typedArray != null) {
            textColor = typedArray.getInt(R.styleable.CommonSearchView_text_color, 0);
            search = typedArray.getString(R.styleable.CommonSearchView_text_ok);
            cancel = typedArray.getString(R.styleable.CommonSearchView_text_cancel);
            hint = typedArray.getString(R.styleable.CommonSearchView_text_hint);
        }

        etSearch = (CommonClearEditText) this.findViewById(R.id.et_search);
        txtSearch = (TextView) this.findViewById(R.id.txt_search);
        listView = (ListView) this.findViewById(R.id.listView_history);
        setHistoryEmptyView();
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

    public CommonClearEditText getSearchEdittext() {
        return etSearch;
    }

    public void setEmptyView(View view) {
        emptyView = view;
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
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

    public void search(){
        etSearch.clearFocus();
        String str = etSearch.getText().toString();
        if(listHistory==null){
            listHistory = new ArrayList<>();
        }
        addHistory(str);
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

    public void setHistorySize(int maxSize){
        this.maxSize = maxSize;
    }

    public List<String> addHistory(String str){
        if(listHistory==null){
            listHistory = new ArrayList<>();
        }
        listHistory.add(0,str);
        for(int i=1;i<listHistory.size();i++){
            if(i>=maxSize||listHistory.get(i).equals(str)){
                listHistory.remove(i);
                i--;
            }
        }
        return listHistory;
    }

    public void showHistoryView() {
        if (adapter == null) {
            adapter = new CommonAdapter(context);
        }
        if(listView.getAdapter()==null){
            listView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }

        if (listView.getVisibility() == View.GONE
                && listView.getEmptyView().getVisibility() == View.GONE){
            listView.setVisibility(View.VISIBLE);
        }
    }

    public void hideHistoryView() {
        if(listView.getEmptyView().getVisibility()==View.VISIBLE){
            listView.getEmptyView().setVisibility(View.GONE);
        }
        if (listView.getVisibility() == View.VISIBLE){
            listView.setVisibility(View.GONE);
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
                showHistoryView();
            } else {
                LibViewUtil.hideIM(v);
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

    class CommonAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public CommonAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (listHistory == null) {
                return 0;
            }
            return listHistory.size();
        }

        @Override
        public Object getItem(int position) {
            return listHistory.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if(viewHolder==null){
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_search_history, null);
                viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_content);
                viewHolder.imgDel = (ImageView) convertView.findViewById(R.id.img_delete);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            viewHolder.txtContent.setText(listHistory.get(position).toString());
            viewHolder.imgDel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listHistory.remove(position);
                    notifyDataSetChanged();
                }
            });
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setText(listHistory.get(position));
                    etSearch.setSelection(listHistory.get(position).length());
                    search();
                }
            });

            return convertView;
        }

        class ViewHolder{
            TextView txtContent;
            ImageView imgDel;
        }

    }

    private void setHistoryEmptyView(){
        TextView emptyView = new TextView(context);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setText(context.getString(R.string.history_no_data));
        emptyView.setBackgroundColor(0xfffefefe);
        emptyView.setTextColor(0xff999999);
        emptyView.setTextSize(14);
        emptyView.setClickable(true);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
    }


    private void screenDefault() {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();

        windowManager.getDefaultDisplay().getMetrics(dm);

        density = dm.density;
//        width = AppContext.getInstance().getDisplayWidth();
    }

    public int getPx(int dip) {
        return (int) (dip * density);
    }

    public void setSearchViewListener(final SearchViewListener listener) {
        this.listener = listener;
    }

    public interface SearchViewListener {

        void onSearch(String str);

        void onCancel();

    }
}
