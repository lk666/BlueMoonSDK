package cn.com.bluemoon.lib.qrcode.decoding;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public final class EncodingHandler {

	private static final int QRCODE_SIZE = 480;
	private static final int BARCODE_HEIGHT = 150;
	private static final int BARCODE_WIDTH = 400;

	public static Bitmap createQRCode(String str, int color,
			boolean isChange,Bitmap logoBitmap,int size) throws WriterException {
		if(size<=0){
			size = QRCODE_SIZE;
		}
		if(color == -1){
			color = Color.BLACK;
		}
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, size, size, hints);
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
					pixels[y * width + x] = Color.WHITE;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		if(logoBitmap!=null){
			int portraitsize = size / 5;
			createQRCodeBitmapWithLogo(bitmap, logoBitmap, size,
					portraitsize);
		}
		return bitmap;
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
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(3);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(w / 2, w / 2, w / 2 - 1, paint);
		return output;
	}

	/**
	 * 生成条形码
	 *
	 * @param context
	 * @param contents
	 *            需要生成的内容
	 * @param desiredWidth
	 *            生成条形码的宽带
	 * @param desiredHeight
	 *            生成条形码的高度
	 * @param displayCode
	 *            是否在条形码下方显示内容
	 * @return
	 */
	public static Bitmap creatBarcode(Context context, String contents,
									  int desiredWidth, int desiredHeight, boolean displayCode) throws WriterException {
		Bitmap ruseltBitmap ;

		if(desiredHeight <= 0||desiredWidth <=0){
			desiredHeight = BARCODE_HEIGHT;
			desiredWidth = BARCODE_WIDTH;
		}

		if (displayCode) {
			Bitmap barcodeBitmap = encodeAsBitmap(contents,desiredWidth, desiredHeight);
			Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth, context);
			ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(
					0, desiredHeight));
		} else {
			ruseltBitmap = encodeAsBitmap(contents,desiredWidth, desiredHeight);
		}

		return ruseltBitmap;
	}

	/**
	 * 生成条形码的Bitmap
	 *
	 * @param contents
	 *            需要生成的内容
	 * @param desiredWidth
	 * @param desiredHeight
	 * @return
	 * @throws WriterException
	 */
	private static Bitmap encodeAsBitmap(String contents,int desiredWidth, int desiredHeight) throws WriterException {
		BarcodeFormat format = BarcodeFormat.CODE_128;
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = writer.encode(contents, format, desiredWidth,
					desiredHeight, null);

		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 生成显示编码的Bitmap
	 *
	 * @param contents
	 * @param width
	 * @param context
	 * @return
	 */
	private static Bitmap creatCodeBitmap(String contents, int width,Context context) {
		TextView tv = new TextView(context);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(layoutParams);
		tv.setText(contents);
		tv.setBackgroundColor(Color.WHITE);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setWidth(width);
		tv.setDrawingCacheEnabled(true);
		tv.setTextColor(Color.BLACK);
		tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

		tv.buildDrawingCache();
		Bitmap bitmapCode = tv.getDrawingCache();
		return bitmapCode;
	}

	/**
	 * 将两个Bitmap合并成一个
	 *
	 * @param first
	 * @param second
	 * @param fromPoint
	 *            第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
	 * @return
	 */
	private static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
										  PointF fromPoint) {
		if (first == null || second == null || fromPoint == null) {
			return null;
		}

		Bitmap newBitmap = Bitmap.createBitmap(
				first.getWidth() + second.getWidth(),
				first.getHeight() + second.getHeight(), Config.ARGB_4444);
		Canvas cv = new Canvas(newBitmap);
		cv.drawBitmap(first, 0, 0, null);
		cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();

		return newBitmap;
	}

}
