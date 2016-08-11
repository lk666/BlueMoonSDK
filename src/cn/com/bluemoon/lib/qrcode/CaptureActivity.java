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
import cn.com.bluemoon.lib.qrcode.view.ViewfinderView;
import cn.com.bluemoon.lib.utils.LibConstants;
import cn.com.bluemoon.lib.utils.LibImageUtil;
import cn.com.bluemoon.lib.utils.LibPublicUtil;

@SuppressLint("NewApi")
public class CaptureActivity extends Activity implements Callback {

	protected CaptureActivity main;
	protected CaptureActivityHandler handler;
	protected boolean hasSurface;
	protected Vector<BarcodeFormat> decodeFormats;
	protected String characterSet;
	protected InactivityTimer inactivityTimer;
	protected MediaPlayer mediaPlayer;
	protected boolean playBeep;
	protected static final float BEEP_VOLUME = 0.50f;
	protected static final int CHOSE_PIC_RESULT = 10;
	protected boolean vibrate;
	protected CameraView cameraview;
	public static boolean KEY = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		cameraview = new CameraView(this,listener);
		
		setContentView(cameraview);
		main = this;
		CameraManager.init(main);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(CameraManager.get()==null){
			CameraManager.init(main);
		}
		SurfaceHolder surfaceHolder = cameraview.surfaceView.getHolder();
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

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		if(CameraManager.get()==null){
			CameraManager.init(main);
		}
		if(CameraManager.get()!=null){
			CameraManager.get().closeDriver();
		}
		KEY = false;
	}
	
	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		Configure.IS_KEEP_OPEN = false;
		super.onDestroy();
	}
	
	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
			ParsedResult result_type = ResultParser.parseResult(result);

			String type = result_type.getType().toString();

			String resultString = result.getText().toString();
			if ("".equals(resultString)) {
				LibPublicUtil.showToast(main, getString(R.string.scan_failed));
			}else {
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString(LibConstants.SCAN_RESULT, resultString);
				bundle.putString(LibConstants.SCAN_TYPE, type);
				resultIntent.putExtras(bundle);
				this.setResult(RESULT_OK, resultIntent);
			}
			CaptureActivity.this.finish();
//		}
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

	public ViewfinderView getViewfinderView() {
		return cameraview.finderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		cameraview.finderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file;
			try {
				file =  getResources().openRawResourceFd(
						R.raw.beep);
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

	private static final long VIBRATE_DURATION = 200L;


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
	
	IScanMenuListener listener = new IScanMenuListener() {
		
		@Override
		public void openPick(ImageButton btn) {
			// TODO Auto-generated method stub
			LibImageUtil.pickPhoto(main, CHOSE_PIC_RESULT);
		}
		
		@Override
		public void openLight(ImageButton btn) {
			// TODO Auto-generated method stub
			KEY = !KEY;
			if(KEY){
				if(CameraManager.get()!=null){
					CameraManager.get().openLight();
				}
				btn.setImageResource(R.drawable.scan_code_light_press);
			}else{
				if(CameraManager.get()!=null){
					CameraManager.get().closeLight();
				}
				btn.setImageResource(R.drawable.scan_code_light);
			}
		}
		
		@Override
		public void closeScan() {
			// TODO Auto-generated method stub
			finish();
			KEY = false;
		}

		@Override
		public void onClick(Button btn) {
			// TODO Auto-generated method stub
			if(getIntent()!=null){
				int resultCode = getIntent().getIntExtra("resultCode", 0);
				setResult(resultCode);
			}
			finish();
			KEY = false;	
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CANCELED){
			return;
		}
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case CHOSE_PIC_RESULT:
				String return_path = LibImageUtil.returnPickPhotoPath(data, main);
				if(return_path == null){
					LibPublicUtil.showToast(main, getString(R.string.get_qrcode_fail));
					return;
				}
				Result rawresult;
			    try {
			    	String dirpath = Environment.getExternalStorageDirectory() + "/Temp";
			    	File file = new File(dirpath);
			    	if(!file.exists()) file.mkdirs();
			    	String path = LibImageUtil.ClipImage(return_path, dirpath, 800);
			    	rawresult = BarcodeUtil.decodeBitmap(path);
			    	if(rawresult != null){
				    	handleDecode(rawresult, null);
				    }else{
				    	LibPublicUtil.showToast(main, getString(R.string.get_qrcode_fail));
				    }
				} catch (Exception e) {
					e.printStackTrace();
					LibPublicUtil.showToast(main, getString(R.string.get_photo_too_big));
				}
				break;

			default:
				break;
			}
		}
	}

}