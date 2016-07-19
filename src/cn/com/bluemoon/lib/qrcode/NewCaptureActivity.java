package cn.com.bluemoon.lib.qrcode;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;

import cn.com.bluemoon.lib.qrcode.camera.CameraManager;
import cn.com.bluemoon.lib.qrcode.decoding.InactivityTimer;
import cn.com.bluemoon.lib.qrcode.view.NewCameraView;

@SuppressLint("NewApi")
public class NewCaptureActivity extends CaptureActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        cameraview = new NewCameraView(this,listener);

        setContentView(cameraview);

        main = this;
        CameraManager.init(main);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }
}