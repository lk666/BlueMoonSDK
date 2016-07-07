package cn.com.bluemoon.lib.qrcode.utils;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import cn.com.bluemoon.lib.qrcode.CaptureActivity;
import cn.com.bluemoon.lib.qrcode.NewCaptureActivity;
import cn.com.bluemoon.lib.qrcode.decoding.DecodeBimap;
import cn.com.bluemoon.lib.qrcode.decoding.EncodingHandler;

import com.google.zxing.Result;
import com.google.zxing.WriterException;

public class BarcodeUtil {


	public static void openScan(Activity thisActivity, int requestCode,
			int resultCode) {
		Intent it_open = new Intent(thisActivity, CaptureActivity.class);
		it_open.putExtra("resultCode", resultCode);
		thisActivity.startActivityForResult(it_open, requestCode);
	}


	public static void openNewScan(Activity thisActivity, int requestCode,
								int resultCode) {
		Intent it_open = new Intent(thisActivity, NewCaptureActivity.class);
		it_open.putExtra("resultCode", resultCode);
		thisActivity.startActivityForResult(it_open, requestCode);
	}

	public static void openScan(Activity thisActivity, Fragment fragment,
			int requestCode, int resultCode) {
		Intent it_open = new Intent(thisActivity, CaptureActivity.class);
		it_open.putExtra("resultCode", resultCode);
		fragment.startActivityForResult(it_open, requestCode);
	}


	public static void openNewScan(Activity thisActivity, Fragment fragment,
								int requestCode, int resultCode) {
		Intent it_open = new Intent(thisActivity, NewCaptureActivity.class);
		it_open.putExtra("resultCode", resultCode);
		fragment.startActivityForResult(it_open, requestCode);
	}

	public static void openScan(Activity thisActivity, int requestCode) {
		Intent it_open = new Intent(thisActivity, CaptureActivity.class);
		thisActivity.startActivityForResult(it_open, requestCode);
	}

	public static void openNewScan(Activity thisActivity, int requestCode) {
		Intent it_open = new Intent(thisActivity, NewCaptureActivity.class);
		thisActivity.startActivityForResult(it_open, requestCode);
	}


	public static void openScan(Activity thisActivity, Fragment fragment,
			int requestCode) {
		Intent it_open = new Intent(thisActivity, CaptureActivity.class);
		fragment.startActivityForResult(it_open, requestCode);
	}


	public static void openNewScan(Activity thisActivity, Fragment fragment,
								int requestCode) {
		Intent it_open = new Intent(thisActivity, NewCaptureActivity.class);
		fragment.startActivityForResult(it_open, requestCode);
	}

	public static Bitmap createQRCode(String str, int QRCODE_SIZE) {
		Bitmap encodeBitmap = null;
		try {
			encodeBitmap = EncodingHandler.createQRCode(str, QRCODE_SIZE);
			return encodeBitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return encodeBitmap;
	}


	public static Bitmap createQRCode(String str) {
		Bitmap encodeBitmap = null;
		try {
			encodeBitmap = EncodingHandler.createQRCode(str);
			return encodeBitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return encodeBitmap;
	}


	public static Bitmap createLogoQRCode(String str, Bitmap logoBitmap) {
		Bitmap encodeBitmap = null;
		try {
			encodeBitmap = EncodingHandler
					.createQRCodeWithLogo(str, logoBitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return encodeBitmap;
	}


	public static Bitmap createQRCodeByColor(String str, int color,
			boolean isChange) {
		Bitmap encodeBitmap = null;
		try {
			encodeBitmap = EncodingHandler.createQRCodeByColor(str, color,
					isChange);
			return encodeBitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return encodeBitmap;
	}
	

	public static Bitmap createQRCodeByColor(String str, int color) {
		return createQRCodeByColor(str, color, false);
	}


	public static Bitmap createLogoQRCodeByColor(String str, int color,
			boolean isChange, Bitmap logoBitmap) {
		Bitmap encodeBitmap = null;
		try {
			encodeBitmap = EncodingHandler.createQRCodeWithLogoByColor(str,
					color, isChange, logoBitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return encodeBitmap;
	}
	

	public static Bitmap createLogoQRCodeByColor(String str, int color,
			 Bitmap logoBitmap) {
		Bitmap encodeBitmap = null;
		try {
			encodeBitmap = EncodingHandler.createQRCodeWithLogoByColor(str,
					color, false, logoBitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return encodeBitmap;
	}


	public static void sendSMS(Activity thisActivity, String str,
			String contactNum) {
		Uri uri = Uri.parse("smsto:" + contactNum);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", str);
		thisActivity.startActivity(intent);
	}


	public static void sendSMS(Activity thisActivity, String str) {
		Uri uri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", str);
		thisActivity.startActivity(intent);
	}


	public static void openUri(Activity thisActivity, String str)
			throws Exception {
		Uri uri = Uri.parse(str);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		thisActivity.startActivity(intent);
	}


	public static void searchFromGoogle(Activity thisActivity, String str)
			throws Exception {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_WEB_SEARCH);
		intent.putExtra(SearchManager.QUERY, str);
		thisActivity.startActivity(intent);
	}


	public static void callContact(Activity thisActivity, String contactNum) {
		Uri uri = Uri.parse("tel:" + contactNum);
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		thisActivity.startActivity(intent);
	}


	public static void sendEmail(Activity thisActivity, String str) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, str);
		intent.setType("text/plain");
		thisActivity.startActivity(Intent.createChooser(intent,
				"Choose Email Client"));
	}


	public static Result decodeBitmap(String path) {
		Result rawResult = null;
		rawResult = DecodeBimap.decodeFile(path);
		return rawResult;
	}


	public static Result decodeBitmap(Bitmap bitmap) {
		Result rawResult = null;
		rawResult = DecodeBimap.decodeFile(bitmap);
		return rawResult;
	}

}
