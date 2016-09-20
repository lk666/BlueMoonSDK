package cn.com.bluemoon.lib.qrcode;

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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import cn.com.bluemoon.lib.qrcode.camera.CameraManager;
import cn.com.bluemoon.lib.qrcode.decoding.CaptureActivityHandler;
import cn.com.bluemoon.lib.qrcode.decoding.InactivityTimer;
import cn.com.bluemoon.lib.qrcode.utils.BarcodeUtil;
import cn.com.bluemoon.lib.qrcode.view.ViewfinderView;
import cn.com.bluemoon.lib.utils.LibConstants;
import cn.com.bluemoon.lib.utils.LibImageUtil;
import cn.com.bluemoon.lib.utils.LibViewUtil;

public abstract class BaseCaptureActivity extends Activity implements Callback {

    protected CaptureActivityHandler handler;
    protected boolean hasSurface;
    protected boolean isLight;
    protected Vector<BarcodeFormat> decodeFormats;
    protected String characterSet;
    protected InactivityTimer inactivityTimer;
    protected MediaPlayer mediaPlayer;
    protected boolean playBeep;
    private static final float BEEP_VOLUME = 0.50f;
    private static final int CHOSE_PIC_RESULT = 10;
    private static final long VIBRATE_DURATION = 200L;
    protected boolean vibrate;
    protected String title;
    private boolean isPause;


    public static void actStart(Activity aty, Fragment fragment, Class clazz,String title, int requestCode) {
        Intent intent = new Intent(aty, clazz);
        intent.putExtra("title", title);
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            aty.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeSetContentLayout();
        initContentView();
        CameraManager.init(this);
        hasSurface = false;
        initView();
        initData();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        playBeepSoundAndVibrate();
        pauseScan();
        if (result == null) {
            onResult(null, null, barcode);
            return;
        }
        ParsedResult resultType = ResultParser.parseResult(result);
        String type = resultType.getType().toString();
        String resultString = result.getText();
        onResult(resultString, type, barcode);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = true;
        resumeScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseScan();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    protected SurfaceHolder getSurfaceHolder() {
        return ((SurfaceView) findViewById(getSurfaceViewId())).getHolder();
    }

    final public ViewfinderView getViewfinderView() {
        return (ViewfinderView) findViewById(getViewfinderViewId());
    }

    final public Handler getHandler() {
        return handler;
    }

    final public void drawViewfinder() {
        getViewfinderView().drawViewfinder();
    }

    //播放声音和震动

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file;
            try {
                file = getResources().openRawResourceFd(R.raw.beep);
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOSE_PIC_RESULT:
                    decodeImage(data);
                    break;
            }
        }
    }

    //////////必须实现的方法/////////////
    protected abstract int getLayoutId();

    /**
     * surfaceView一般为全屏界面
     *
     * @return
     */
    protected abstract int getSurfaceViewId();

    /**
     * ViewfinderView一般为全屏界面
     *
     * @return
     */
    protected abstract int getViewfinderViewId();

    /**
     * 接收返回的扫描数据的方法
     *
     * @param str
     * @param type
     * @param barcode
     */
    protected abstract void onResult(String str, String type, Bitmap barcode);

    ///////////////////可重写方法///////////////

    /**
     * 填充布局之前的方法
     */
    protected void onBeforeSetContentLayout() {
        if(getIntent()!=null){
            title = getIntent().getStringExtra("title");
        }
        if(TextUtils.isEmpty(title)){
            title = getString(R.string.qrcode_title_txt);
        }
    }

    /**
     * 填充界面布局的方法
     */
    protected void initContentView() {
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
    }

    protected void initView() {

    }

    protected void initData() {
    }

    ////////////////公共方法////////////////

    /**
     * 选图识别二维码
     */
    final protected void pickImage() {
        LibImageUtil.pickPhoto(this, CHOSE_PIC_RESULT);
    }

    /**
     * 打开闪光灯
     */
    final protected void openLight() {
        if (CameraManager.get() != null&&!isLight) {
            CameraManager.get().openLight();
        }
        isLight = true;
    }

    /**
     * 关闭闪光灯
     */
    final protected void closeLight() {
        if (CameraManager.get() != null&&isLight) {
            CameraManager.get().closeLight();
        }
        isLight = false;
    }

    /**
     * 暂停扫描
     */
    final protected void pauseScan() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (CameraManager.get() != null) {
            CameraManager.get().closeDriver();
        }
        isPause = true;
    }

    /**
     * 启动扫描
     */
    final protected void resumeScan() {
        if(!isPause) return;
        inactivityTimer = new InactivityTimer(this);
        if (CameraManager.get() == null) {
            CameraManager.init(this);
        }
        SurfaceHolder surfaceHolder = getSurfaceHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    /**
     * 解析本地二维码图片
     *
     * @param path
     */
    final protected void decodeImage(String path) {
        try {
            if (path != null) {
                String dirpath = Environment.getExternalStorageDirectory() + "/Temp";
                File file = new File(dirpath);
                if (!file.exists()) file.mkdirs();
                String newPath = LibImageUtil.ClipImage(path, dirpath, 800);
                Result rawresult = BarcodeUtil.decodeBitmap(newPath);
                handleDecode(rawresult, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LibViewUtil.toast(this, getString(R.string.get_photo_too_big));
        }
    }

    final protected void decodeImage(Intent data) {
        String returnPath = LibImageUtil.returnPickPhotoPath(data, this);
        decodeImage(returnPath);
    }

    /**
     * 带返回关闭Activity
     *
     * @param resultString
     * @param type
     */
    final protected void finishWithData(String resultString, String type) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(LibConstants.SCAN_RESULT, resultString);
        bundle.putString(LibConstants.SCAN_TYPE, TextUtils.isEmpty(type)?"TEXT":type);
        intent.putExtras(bundle);
        this.setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 绘制返回结果的图片
     * @param barcode
     */
    protected void drawResultBitmap(Bitmap barcode){
        getViewfinderView().drawResultBitmap(barcode);
    }


}