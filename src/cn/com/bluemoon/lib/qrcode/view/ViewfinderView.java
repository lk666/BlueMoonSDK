/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.bluemoon.lib.qrcode.view;

import java.util.Collection;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.qrcode.camera.CameraManager;
import cn.com.bluemoon.lib.qrcode.utils.Configure;

import com.google.zxing.ResultPoint;


@SuppressLint("DrawAllocation")
public final class ViewfinderView extends View {


	private static final int[] SCANNER_ALPHA = Configure.SCANNER_ALPHA;
	private static final long ANIMATION_DELAY = Configure.ANIMATION_DELAY;
	private static final int TEXT_PADDING_BOTTOM = 30;
//	private static final int TEXT_PADDING_LEFT = 40;
	private static final int OPAQUE = 0xFF;
	private static float density;
	private static int ScreenRate;
	private static int CORNER_WIDTH;
	private final Paint paint;
	private static int middle = 0;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;


	public ViewfinderView(Context context) {
		super(context);

		paint = new Paint();
		maskColor = Configure.MASK_COLOR;
		resultColor = Configure.RESULT_COLOR;
		frameColor = Configure.FRAME_COLOR;
		laserColor = Configure.LASER_COLOR;
		resultPointColor = Configure.RESULT_POINT_COLOR;
		scannerAlpha = 0;
		middle = 0;
		possibleResultPoints = new HashSet<ResultPoint>(5);
		density = context.getResources().getDisplayMetrics().density;

		ScreenRate = (int) (Configure.SCREEN_RATE * density);
		CORNER_WIDTH = (int) (Configure.CORNER_WIDTH * density);
	}


	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		paint = new Paint();
		maskColor = Configure.MASK_COLOR;
		resultColor = Configure.RESULT_COLOR;
		frameColor = Configure.FRAME_COLOR;
		laserColor = Configure.LASER_COLOR;
		resultPointColor = Configure.RESULT_POINT_COLOR;
		scannerAlpha = 0;
		middle = 0;
		possibleResultPoints = new HashSet<ResultPoint>(5);
		density = context.getResources().getDisplayMetrics().density;

		ScreenRate = (int) (Configure.SCREEN_RATE * density);
		CORNER_WIDTH = (int) (Configure.CORNER_WIDTH * density);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if(CameraManager.get()==null){
			CameraManager.init(this.getContext());
			if(CameraManager.get()==null){
				return;
			}
		}
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();


		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);


		if (resultBitmap != null) {

			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			paint.setColor(frameColor);
			canvas.drawRect(frame.left, frame.top, frame.right + 1,
					frame.top + 2, paint);
			canvas.drawRect(frame.left, frame.top + 2, frame.left + 2,
					frame.bottom - 1, paint);
			canvas.drawRect(frame.right - 1, frame.top, frame.right + 1,
					frame.bottom - 1, paint);
			canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1,
					frame.bottom + 1, paint);

			paint.setColor(Color.GREEN);
			canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH,
					frame.top + ScreenRate, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right,
					frame.top + ScreenRate, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
					+ ScreenRate, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - ScreenRate, frame.left
					+ CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.bottom
					- CORNER_WIDTH, frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom
					- ScreenRate, frame.right, frame.bottom, paint);
			

			String content = getResources().getString(R.string.qrcode_content_txt);
			paint.setColor(Configure.BTN_TXT_CONTENT_COLOR);
			paint.setTextSize(Configure.CONTENT_TXT_SIZE * density);
//			paint.setAlpha(0xbb);
//			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
			float l = (frame.left+frame.right)/2 - ((Configure.CONTENT_TXT_SIZE*content.length()/2)*density); 
//			canvas.drawText(Configure.content_txt, (float)(frame.left+(float)TEXT_PADDING_LEFT *density), (float) (frame.top - (float)TEXT_PADDING_BOTTOM *density), paint);
			canvas.drawText(content, l, (float) (frame.top - (float)TEXT_PADDING_BOTTOM *density), paint);

			paint.setColor(laserColor);
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			// int middle = frame.height() / 2 + frame.top;
			if (middle == 0 || middle == frame.bottom-10) {
				middle = frame.top;
			}
			middle = middle + Configure.LINE_SIZE;
			canvas.drawRect(frame.left + Configure.MIDDLE_LINE_PADDING, middle
					- Configure.LINE_HEIGHT / 2, frame.right
					- Configure.MIDDLE_LINE_PADDING, middle
					+ Configure.LINE_HEIGHT / 2, paint);
//			Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.image_qr_scan_line1);
//			int left = frame.left + ((frame.right - frame.left)-bit.getWidth()) / 2;
//			canvas.drawBitmap(bit, left + Configure.MIDDLE_LINE_PADDING,middle - Configure.LINE_HEIGHT / 2, paint);

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 3.0f, paint);
				}
			}

			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}


	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
