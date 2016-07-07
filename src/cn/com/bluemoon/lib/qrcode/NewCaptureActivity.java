package cn.com.bluemoon.lib.qrcode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import cn.com.bluemoon.lib.qrcode.callback.IScanMenuListener;
import cn.com.bluemoon.lib.qrcode.camera.CameraManager;
import cn.com.bluemoon.lib.qrcode.decoding.CaptureActivityHandler;
import cn.com.bluemoon.lib.qrcode.decoding.InactivityTimer;
import cn.com.bluemoon.lib.qrcode.utils.BarcodeUtil;
import cn.com.bluemoon.lib.qrcode.utils.Configure;
import cn.com.bluemoon.lib.qrcode.view.CameraView;
import cn.com.bluemoon.lib.qrcode.view.NewCameraView;
import cn.com.bluemoon.lib.qrcode.view.ViewfinderView;
import cn.com.bluemoon.lib.utils.LibConstants;
import cn.com.bluemoon.lib.utils.LibImageUtil;
import cn.com.bluemoon.lib.utils.LibPublicUtil;

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