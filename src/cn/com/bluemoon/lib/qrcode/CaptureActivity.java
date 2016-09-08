package cn.com.bluemoon.lib.qrcode;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CaptureActivity extends BaseCaptureActivity implements View.OnClickListener{

    private ImageView btnBack;
    private ImageView btnPick;
    private ImageView btnLight;
    private TextView txtTitle;
    private boolean isLight;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_capture;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        btnBack = (ImageView)findViewById(R.id.btn_back);
        btnPick = (ImageView)findViewById(R.id.btn_pick);
        btnLight = (ImageView)findViewById(R.id.btn_light);
        txtTitle = (TextView)findViewById(R.id.txt_title);
        btnBack.setOnClickListener(this);
        btnPick.setOnClickListener(this);
        btnLight.setOnClickListener(this);
        if(!TextUtils.isEmpty(title)){
            txtTitle.setText(title);
        }
    }

    @Override
    protected int getSurfaceViewId() {
        return R.id.preview_view;
    }

    @Override
    protected int getViewfinderViewId() {
        return R.id.viewfinder_view;
    }

    @Override
    protected void onResult(String str, String type, Bitmap barcode) {
        finishWithData(str,type);
    }

    @Override
    public void onClick(View v) {
        if(v== btnBack){
            finish();
        }else if(v == btnLight){
            if(isLight){
                closeLight();
                btnLight.setImageResource(R.drawable.scan_code_light);
            }else{
                openLight();
                btnLight.setImageResource(R.drawable.scan_code_light_press);
            }
            isLight = !isLight;
        }else if(v == btnPick){
            pickImage();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
