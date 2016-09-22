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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;

import java.util.Collection;
import java.util.HashSet;

import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.qrcode.camera.CameraManager;
import cn.com.bluemoon.lib.qrcode.utils.Configure;


@SuppressLint("DrawAllocation")
public final class ViewfinderView extends View {


    private final float density;
    private final Paint paint;
    private float cornerHeight;
    private float cornerWidth;
    private int cornerColor;
    private int contentColor;
    private String contentText;
    private float contentPaddingBottom;
    private boolean isHideContent;
    private int maskColor;
    private int frameColor;
    private int laserColor;
    private int laserSpace;
    private int laserColorLight;
//    private int scannerAlpha;
    private float top;
    private float width;
    private float height;
    private int middle;
    private Bitmap resultBitmap;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;


    public ViewfinderView(Context context) {
        super(context);
        density = context.getResources().getDisplayMetrics().density;
        paint = new Paint();
        init(context);
        initData();
    }

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        paint = new Paint();
        init(context);
        readStyleParameters(context, attrs);
    }

    private void init(Context context) {
        if (CameraManager.get() == null) {
            CameraManager.init(context);
        }
        CameraManager.get().clearFramimgRect();
        middle = 0;
//        scannerAlpha = 0;
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    private void initData() {
        maskColor = Configure.MASK_COLOR;
        frameColor = Configure.FRAME_COLOR;
        laserColor = Configure.LASER_COLOR;
        laserSpace = Configure.LASER_SPACE;
        laserColorLight = Configure.LASER_COLOR_LIGHT;
        contentColor = Configure.CONTENT_COLOR;
        contentPaddingBottom = Configure.CONTENT_PADDING_BOTTOM * density;
        cornerColor = Configure.CORNER_COLOR;
        cornerHeight = Configure.CORNER_HEIGHT * density;
        cornerWidth = Configure.CORNER_WIDTH * density;
    }

    private void readStyleParameters(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) return;
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet,
                R.styleable.ViewfinderView);
        try {
            if (typedArray != null) {
                top = typedArray.getDimensionPixelOffset(R.styleable.ViewfinderView_marginTop, 0);
                width = typedArray.getDimensionPixelOffset(R.styleable.ViewfinderView_rectWidth, 0);
                height = typedArray.getDimensionPixelOffset(R.styleable.ViewfinderView_rectHeight, 0);
                maskColor = typedArray.getColor(R.styleable.ViewfinderView_mask_color, Configure.MASK_COLOR);
                frameColor = typedArray.getColor(R.styleable.ViewfinderView_frame_color, Configure.FRAME_COLOR);
                laserColor = typedArray.getColor(R.styleable.ViewfinderView_laser_color, Configure.LASER_COLOR);
                cornerColor = typedArray.getColor(R.styleable.ViewfinderView_corner_color, Configure.CORNER_COLOR);
                laserSpace = typedArray.getDimensionPixelOffset(R.styleable.ViewfinderView_laser_space,
                        (int) (Configure.LASER_SPACE * density));
                laserColorLight = typedArray.getColor(R.styleable.ViewfinderView_laser_color_light, Configure.LASER_COLOR_LIGHT);
                cornerWidth = typedArray.getDimensionPixelOffset(R.styleable.ViewfinderView_corner_width,
                        (int) (Configure.CORNER_WIDTH * density));
                cornerHeight = typedArray.getDimensionPixelOffset(R.styleable.ViewfinderView_corner_height,
                        (int) (Configure.CORNER_HEIGHT * density));
                isHideContent = typedArray.getBoolean(R.styleable.ViewfinderView_isHideContent, false);
                if(!isHideContent){
                    contentText = typedArray.getString(R.styleable.ViewfinderView_content_text);
                    contentColor = typedArray.getColor(R.styleable.ViewfinderView_content_color, Configure.CONTENT_COLOR);
                    contentPaddingBottom = typedArray.getColor(R.styleable.ViewfinderView_content_padding_bottom,
                            (int) (Configure.CONTENT_PADDING_BOTTOM * density));
                }
            }
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (CameraManager.get() == null) {
            CameraManager.init(this.getContext());
        }
        Rect frame = CameraManager.get().getFramingRect((int) top, (int) width, (int) height);
        if (frame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        /*绘制扫描框外部的半透明部分*/
        paint.setColor(resultBitmap != null ? Configure.RESULT_COLOR : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
                paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);


        if (resultBitmap != null) {
            /*绘制返回结果的图片*/
            paint.setAlpha(Configure.OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            /*绘制扫描框*/
            paint.setColor(frameColor);
            canvas.drawRect(frame.left, frame.top, frame.right + 1,
                    frame.top + 2, paint);
            canvas.drawRect(frame.left, frame.top + 2, frame.left + 2,
                    frame.bottom - 1, paint);
            canvas.drawRect(frame.right - 1, frame.top, frame.right + 1,
                    frame.bottom - 1, paint);
            canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1,
                    frame.bottom + 1, paint);

            /*绘制4个边角*/
            paint.setColor(cornerColor);
            canvas.drawRect(frame.left, frame.top, frame.left + cornerHeight,
                    frame.top + cornerWidth, paint);
            canvas.drawRect(frame.left, frame.top, frame.left + cornerWidth,
                    frame.top + cornerHeight, paint);
            canvas.drawRect(frame.right - cornerHeight, frame.top, frame.right,
                    frame.top + cornerWidth, paint);
            canvas.drawRect(frame.right - cornerWidth, frame.top, frame.right,
                    frame.top + cornerHeight, paint);
            canvas.drawRect(frame.left, frame.bottom - cornerWidth, frame.left
                    + cornerHeight, frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - cornerHeight, frame.left
                    + cornerWidth, frame.bottom, paint);
            canvas.drawRect(frame.right - cornerHeight, frame.bottom
                    - cornerWidth, frame.right, frame.bottom, paint);
            canvas.drawRect(frame.right - cornerWidth, frame.bottom
                    - cornerHeight, frame.right, frame.bottom, paint);

            /*绘制黄色闪点*/
            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(Configure.OPAQUE);
                paint.setColor(Configure.RESULT_POINT_COLOR);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(Configure.OPAQUE / 2);
                paint.setColor(Configure.RESULT_POINT_COLOR);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }

            /*绘画描述文字*/
            if (!isHideContent) {
                if(TextUtils.isEmpty(contentText)){
                    contentText = getResources().getString(R.string.qrcode_content_txt);
                }
                paint.setColor(contentColor);
                paint.setTextSize(Configure.CONTENT_SIZE * density);
                float l = (frame.left + frame.right) / 2 - (Configure.CONTENT_SIZE * density * contentText.length() / 2);
                canvas.drawText(contentText, l, (frame.top - contentPaddingBottom), paint);
            }

            /*绘画激光扫描线*/
            Paint paintLine = new Paint();
            LinearGradient linearGradient = new LinearGradient(frame.left ,0, frame.right,0,
                    new int[]{Color.TRANSPARENT, laserColorLight,laserColor, laserColorLight,Color.TRANSPARENT},
                    new float[]{0f,0.25f,0.5f,0.75f,1f}, Shader.TileMode.CLAMP);
            paintLine.setShader(linearGradient);

            if (middle == 0 || middle >= frame.bottom - 10) {
                middle = frame.top;
            }
            middle = middle + laserSpace;
            canvas.drawRect(frame.left, middle
                    - Configure.LASER_HEIGHT / 2, frame.right, middle
                    + Configure.LASER_HEIGHT / 2, paintLine);

            postInvalidateDelayed(Configure.ANIMATION_DELAY, frame.left, frame.top,
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
