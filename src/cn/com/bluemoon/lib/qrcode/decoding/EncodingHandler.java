package cn.com.bluemoon.lib.qrcode.decoding;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xffffffff;

	private static final int QRCODE_SIZE = 480;


	public static Bitmap createQRCode(String str, int widthAndHeight)
			throws WriterException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
	
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				} else {
					pixels[y * width + x] = WHITE;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}


	public static Bitmap createQRCode(String str) throws WriterException {
		return createQRCode(str, QRCODE_SIZE);
	}


	public static Bitmap createQRCodeByColor(String str, int color,
			boolean isChange) throws WriterException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					if (isChange) {
						pixels[y * width + x] = color - y / 5;
					} else {
						pixels[y * width + x] = color;
					}

				} else {
					pixels[y * width + x] = WHITE;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}


	public static Bitmap createQRCodeWithLogoByColor(String str, int color,
			boolean isChange, Bitmap logoBitmap) throws WriterException {
		int portraitsize = QRCODE_SIZE / 5;
		Bitmap encodeBitmap = createQRCodeByColor(str, color, isChange);
		createQRCodeBitmapWithLogo(encodeBitmap, logoBitmap, QRCODE_SIZE,
				portraitsize);
		return encodeBitmap;

	}


	public static Bitmap createQRCodeWithLogo(String str, Bitmap logoBitmap)
			throws WriterException {
		int portraitsize = QRCODE_SIZE / 5;
		Bitmap encodeBitmap = createQRCode(str, QRCODE_SIZE);
		createQRCodeBitmapWithLogo(encodeBitmap, logoBitmap, QRCODE_SIZE,
				portraitsize);
		return encodeBitmap;

	}


	private static void createQRCodeBitmapWithLogo(Bitmap qr,
			Bitmap logoBitmap, int widthAndHeight, int portraitsize) {


		Matrix mMatrix = new Matrix();
		float width = logoBitmap.getWidth();
		float height = logoBitmap.getHeight();
		mMatrix.setScale(portraitsize / width, portraitsize / height);
		Bitmap portrait = Bitmap.createBitmap(logoBitmap, 0, 0, (int) width,
				(int) height, mMatrix, true);


		portrait = getRoundedCornerBitmap(portrait);


		int portrait_W = portrait.getWidth();
		int portrait_H = portrait.getHeight();


		int left = (widthAndHeight - portrait_W) / 2;
		int top = (widthAndHeight - portrait_H) / 2;
		int right = left + portrait_W;
		int bottom = top + portrait_H;
		Rect rect1 = new Rect(left, top, right, bottom);


		Canvas canvas = new Canvas(qr);

		Rect rect2 = new Rect(0, 0, portrait_W, portrait_H);

		canvas.drawBitmap(portrait, rect2, rect1, null);
	}


	private static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int w;
		int deltaX = 0;
		int deltaY = 0;
		if (width <= height) {
			w = width;
			deltaY = height - w;
		} else {
			w = height;
			deltaX = width - w;
		}
		final Rect rect = new Rect(deltaX, deltaY, w, w);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
		canvas.drawRoundRect(rectF, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		paint.setColor(WHITE);
		paint.setStrokeWidth(3);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(w / 2, w / 2, w / 2 - 1, paint);
		return output;
	}

}
