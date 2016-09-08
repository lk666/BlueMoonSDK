package cn.com.bluemoon.lib.qrcode.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import com.google.zxing.Result;
import com.google.zxing.WriterException;

import cn.com.bluemoon.lib.qrcode.CaptureActivity;
import cn.com.bluemoon.lib.qrcode.decoding.DecodeBimap;
import cn.com.bluemoon.lib.qrcode.decoding.EncodingHandler;

public class BarcodeUtil {

	/**
	 * 打开默认的扫描界面
	 * @param thisActivity
	 * @param fragment
	 * @param requestCode
	 */
	public static void openScan(Activity thisActivity, Fragment fragment,int requestCode) {
		Intent itOpen = new Intent(thisActivity, CaptureActivity.class);
		if(fragment!=null){
			fragment.startActivityForResult(itOpen, requestCode);
		}else {
			thisActivity.startActivityForResult(itOpen, requestCode);
		}
	}

	public static void openScan(Activity thisActivity, int requestCode) {
		openScan(thisActivity, null, requestCode);
	}

	/**
	 * 生成二维码图片
	 * @param str
	 * @param color
	 * @param isChange
	 * @param logo
	 * @param size
	 * @return
	 */
	public static Bitmap createQRCode(String str,int color,boolean isChange,Bitmap logo, int size) {
		Bitmap encodeBitmap = null;
		try {
			encodeBitmap = EncodingHandler.createQRCode(str,color,isChange,logo,size);
			return encodeBitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return encodeBitmap;
	}

	public static Bitmap createQRCode(String str,int color,boolean isChange,Bitmap logo) {
		return createQRCode(str,color,isChange,logo,0);
	}

	public static Bitmap createQRCode(String str,int color,boolean isChange) {
		return createQRCode(str,color,isChange,null);
	}

	public static Bitmap createQRCode(String str,Bitmap logo) {
		return createQRCode(str,0,false,logo);
	}

	public static Bitmap createQRCode(String str,int size) {
		return createQRCode(str,0,false,null,size);
	}

	public static Bitmap createQRCode(String str) {
		return createQRCode(str,null);
	}

	/**
	 * 生成条形码
	 * @param context
	 * @param contents
	 * @param displayCode
	 * @return
	 */
	public static Bitmap creatBarcode(Context context, String contents,boolean displayCode){
		Bitmap encodeBitmap = null;
		try {
			encodeBitmap = EncodingHandler.creatBarcode(context,contents,0,0,displayCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encodeBitmap;
	}

	public static Bitmap creatBarcode(String contents){
		return creatBarcode(null, contents,false);
	}

	/**
	 * 解析二维码图片
	 * @param path 图片路径
	 * @return
	 */
	public static Result decodeBitmap(String path) {
		return DecodeBimap.decodeFile(path);
	}


	/**
	 * 解析二维码图片
	 * @param bitmap
	 * @return
	 */
	public static Result decodeBitmap(Bitmap bitmap) {
		return DecodeBimap.decodeFile(bitmap);
	}

}
