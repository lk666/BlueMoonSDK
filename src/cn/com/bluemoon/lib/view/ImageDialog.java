package cn.com.bluemoon.lib.view;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import cn.com.bluemoon.lib.callback.ImageDialogCallback;
import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.utils.ImageLoaderUtil;

@SuppressLint("NewApi")
public class ImageDialog extends DialogFragment {

    private View view;
    private String codeUrl;
    private Bitmap bm;
    private ImageView imgView;
    private ImageDialogCallback cb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.dialog_image, container, false);
        initView();
        initData();
        return view;
    }

    public void setCallback(ImageDialogCallback cb) {
        this.cb = cb;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
        this.bm = null;
    }


    public void setBitmap(Bitmap bm) {
        this.bm = bm;
        this.codeUrl = null;
    }

    OnClickListener onclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    View.OnLongClickListener longClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (cb != null) {
                return cb.longClick(v,bm);
            }
            return false;
        }
    };

    /**
     * 初始化图片界面
     */
    private void initView(){
        imgView = (ImageView) view.findViewById(R.id.img_pic);
        view.setOnClickListener(onclick);
        imgView.setOnClickListener(onclick);
        imgView.setOnLongClickListener(longClick);
    }

    /**
     * 设置图片
     */
    private void initData(){
        if (TextUtils.isEmpty(codeUrl)) {
            imgView.setImageBitmap(bm);
        } else {
            ImageLoaderUtil.displayImage(codeUrl, imgView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    bm = bitmap;
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        if (cb != null) {
            cb.showResult();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        clear();
        if (cb != null) {
            cb.dismissResult();
        }
    }

    private void clear() {
        bm = null;
        codeUrl = null;
    }

}
