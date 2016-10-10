package cn.com.bluemoon.lib.view;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import cn.com.bluemoon.lib.callback.CodeDialogCallback;
import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.qrcode.utils.BarcodeUtil;
import cn.com.bluemoon.lib.utils.LibViewUtil;

public class QRCodeDialog extends DialogFragment {

    private View view;
    private String codeTitle;
    private String codeString;
    private String codeContent;
    private String code;
    private Bitmap bm;
    private ImageView imgView;
    private CodeDialogCallback cb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.dialog_code, container, false);
        initView();
        initData();
        return view;
    }

    public void setCallback(CodeDialogCallback cb) {
        this.cb = cb;
    }

    public void setTitle(String codeTitle) {
        this.codeTitle = codeTitle;
    }

    public void setString(String codeString) {
        this.codeString = codeString;
    }

    public void setContent(String codeContent) {
        this.codeContent = codeContent;
    }

    public  void setCode(String code){
        this.code = code;
        this.bm = null;
    }

    public void setBitmap(Bitmap bm) {
        this.bm = bm;
        code = null;
    }

    OnClickListener onclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == view || v == imgView) {
                dismiss();
            }
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

    private void initView(){
        imgView = (ImageView) view.findViewById(R.id.img_code);
        view.setOnClickListener(onclick);
        imgView.setOnClickListener(onclick);
        imgView.setOnLongClickListener(longClick);
    }

    public void initData(){
        setCodeImage(code);
        setTitleTxt(codeTitle);
        setStringTxt(codeString);
        setContentTxt(codeContent);
    }

    private void setCodeImage(String code){
        if(code !=null){
            bm = BarcodeUtil.createQRCode(code);
        }
        imgView.setImageBitmap(bm);
    }

    private void setTitleTxt(String codeTitle) {
        if (!StringUtils.isEmpty(codeTitle)) {
            TextView txt = (TextView) view.findViewById(R.id.title_code);
            txt.setText(codeTitle);
            LibViewUtil.setViewVisibility(txt, View.VISIBLE);
        }
    }

    private void setStringTxt(String codeString) {
        if (!StringUtils.isEmpty(codeString)) {
            TextView txt = (TextView) view.findViewById(R.id.txt_code);
            txt.setText(codeString);
            LibViewUtil.setViewVisibility(txt, View.VISIBLE);
        }
    }


    private void setContentTxt(String codeContent) {
        if (!StringUtils.isEmpty(codeContent)) {
            TextView txt = (TextView) view.findViewById(R.id.txt_content);
            txt.setText(codeContent);
            LibViewUtil.setViewVisibility(txt, View.VISIBLE);
            view.findViewById(R.id.line).setVisibility(View.VISIBLE);
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
        codeTitle = null;
        codeString = null;
        codeContent = null;
        code = null;
    }

}
