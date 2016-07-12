package cn.com.bluemoon.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import cn.com.bluemoon.lib.qrcode.R;

/**
 * Created by bm on 2016/7/12.
 */
public class CommonEmptyView extends RelativeLayout {

    private TextView txtRefresh;
    private TextView txtContent;
    private ImageView imgEmpty;
    private EmptyListener listener;

    public CommonEmptyView(Context context) {
        super(context);
        init(context, null);
    }

    public CommonEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layoutInflater.inflate(R.layout.layout_empty, this);
        txtContent = (TextView) this.findViewById(R.id.txt_content);
        txtRefresh = (TextView) this.findViewById(R.id.txt_refresh);
        imgEmpty = (ImageView) this.findViewById(R.id.img_empty);

        readStyleParameters(context,attrs);

        txtRefresh.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtRefresh.getPaint().setAntiAlias(true);
        txtRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRefresh();
                }
            }
        });
    }

    private void readStyleParameters(Context context,AttributeSet attributeSet) {
        if(attributeSet==null) return;
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet,
                R.styleable.CommonEmptyView);
        try {
            if (typedArray != null) {
                String text = typedArray.getString(R.styleable.CommonEmptyView_text_content);
                int icon = typedArray.getResourceId(R.styleable.CommonEmptyView_img_empty, 0);
                if(StringUtils.isEmpty(text)){
                    text = String.format(context.getString(R.string.empty_hint),"");
                }
                txtContent.setText(text);
                if(icon != 0){
                    imgEmpty.setImageResource(icon);
                }
            }
        }finally {
            typedArray.recycle();
        }
    }

    public CommonEmptyView setContentText(String text){
        txtContent.setText(text);
        return this;
    }

    public CommonEmptyView setRefreshText(String text){
        txtRefresh.setText(text);
        return this;
    }

    public CommonEmptyView setEmptyImage(int imgResid){
        imgEmpty.setImageResource(imgResid);
        return this;
    }

    public CommonEmptyView setEmptyListener(EmptyListener listener){
        this.listener = listener;
        return this;
    }

    public interface EmptyListener{
        void onRefresh();
    }
}
