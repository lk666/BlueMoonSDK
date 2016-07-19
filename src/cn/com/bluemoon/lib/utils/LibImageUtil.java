package cn.com.bluemoon.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LibImageUtil {

    /************************* 图片处理 ****************************************************/
    /**
     * 获得圆角图片
     *
     * @param bitmap    bitmap资源
     * @param width
     * @param height
     * @param roundSize 圆角大小
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int width,
                                                int height, int roundSize) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // 若读取图片的宽度或高度小于ImageView的宽度或高度，则对图片进行放大
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / (float) w, (float) height / (float) h); // 长和宽放大缩小的比例
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        // 创建一个新的bitmap，然后在bitmap里创建一个圆角画布，将之前的图片画在里面。
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundSize, roundSize, paint);// 圆角平滑度
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获得圆角图片，圆角大小默认为10
     *
     * @param bitmap 资源
     * @param width
     * @param height
     * @param
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int width, int height) {
        return getRoundedCornerBitmap(bitmap, width, height, 10);
    }

    /**
     * 绘制带有边框的文字
     *
     * @param strMsg   文字内容
     * @param textSize 文字大小
     * @param SW       画布宽度
     * @param SH       画布高度
     * @param setx     字体x轴坐标
     * @param sety     字体x轴坐标
     * @param fg       前景色
     * @param bg       背景色
     * @return
     */

    public static Bitmap drawText(String strMsg, int textSize, int SW, int SH,
                                  int setx, int sety, int fg, int bg) {
        Bitmap mBitmap = Bitmap.createBitmap(SW, SH, Bitmap.Config.ARGB_8888);
        Canvas g = new Canvas(mBitmap);
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(bg);
        g.drawText(strMsg, setx + 1, sety, paint);
        g.drawText(strMsg, setx, sety - 1, paint);
        g.drawText(strMsg, setx, sety + 1, paint);
        g.drawText(strMsg, setx - 1, sety, paint);
        paint.setColor(fg);
        g.drawText(strMsg, setx, sety, paint);
        // g.restore();

        return mBitmap;
    }

    /**
     * 自适应大小边框字体
     *
     * @param strMsg
     * @param textSize
     * @param fg
     * @param bg
     * @return
     */
    public static Bitmap drawText(String strMsg, int textSize, int fg, int bg) {
        int txtW = (int) GetTextWidth(strMsg, textSize);
        return drawText(strMsg, textSize, txtW + 4, textSize + textSize / 5, 0,
                textSize - textSize / 10, fg, bg);
    }

    /***
     * 图片平均分割方法，将大图平均分割为N行N列
     *
     * @param g      画布
     * @param paint  画笔
     * @param imgBit 图片
     * @param x      X轴起点坐标
     * @param y      Y轴起点坐标
     * @param w      单一图片的宽度
     * @param h      单一图片的高度
     * @param line   第几列
     * @param row    第几行
     */
    public static void cuteImage(Canvas g, Paint paint, Bitmap imgBit, int x,
                                 int y, int w, int h, int line, int row) {
        g.clipRect(x, y, x + w, h + y);
        g.drawBitmap(imgBit, x - line * w, y - row * h, paint);
        g.restore();
    }

    /**
     * 计算字符串的宽度
     *
     * @param text
     * @param Size
     * @return
     */
    public static float GetTextWidth(String text, float Size) {
        TextPaint FontPaint = new TextPaint();
        FontPaint.setTextSize(Size);
        return FontPaint.measureText(text);
    }

    /**
     * 通过url下载图片，并转化为drawable
     *
     * @param imgurl
     * @return
     */
    public static Drawable returnDrawable(String imgurl) {
        if (imgurl != null) {
            InputStream inputStream;
            try {
                inputStream = (InputStream) new URL(imgurl).getContent();
                return Drawable.createFromStream(inputStream, "headsrc");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }

        }
        return null;
    }

    /**
     * 通过url下载图片，并转化为bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 自定义宽高，压缩图片
     *
     * @param filepath
     * @param width
     * @param height
     * @param isThumb
     * @return
     */
    public static Bitmap getImgScale(String filepath, int width, int height,
                                     boolean isThumb) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, opts);
        final int scale1 = Math.round((float) opts.outWidth / (float) width);
        final int scale2 = Math.round((float) opts.outHeight / (float) height);
        opts.inSampleSize = scale1 < scale2 ? scale1 : scale2;
        if (opts.inSampleSize < 1) {
            opts.inSampleSize = 1;
        }
        opts.inJustDecodeBounds = false;

        Bitmap mImg = BitmapFactory.decodeFile(filepath, opts);

        int mRotate = getJpgRotation(filepath);
        if (mRotate != 0) {
            Matrix m = new Matrix();
            m.setRotate(mRotate);
            mImg = Bitmap.createBitmap(mImg, 0, 0, mImg.getWidth(),
                    mImg.getHeight(), m, false);
        }
        if (isThumb) {
            mImg = ThumbnailUtils.extractThumbnail(mImg, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return mImg;
    }

    public static Bitmap getImgScale(String filepath, int size, boolean isThumb) {
        return getImgScale(filepath, size, size, isThumb);
    }

    /**
     * 获取图片的系统旋转角度
     *
     * @param img
     * @return
     */
    public static int getJpgRotation(String img) {
        if (img == null)
            return 0;
        if (img.endsWith(".jpg") == false && img.endsWith(".JPG") == false
                && img.endsWith(".dat") == false
                && img.endsWith(".dat") == false)
            return 0;
        ExifInterface exif;
        try {
            exif = new ExifInterface(img);
            String r = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            if (r != null && r.length() > 0) {
                int ori = Integer.parseInt(r);
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        return 90;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        return 180;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        return 270;
                }
            }
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 获取带后缀的文件名
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        if (path != null) {
            int pos = path.lastIndexOf("/");
            if (pos != -1) {
                String name = path.substring(pos + 1);
                return name;
            }
            return path;
        }

        return null;
    }

    /**
     * 获取不带后缀的文件名
     *
     * @param path
     * @return
     */
    public static String getFileName2(String path) {
        if (path != null) {
            String name = path;
            int pos = path.lastIndexOf("/");
            if (pos != -1) {
                name = path.substring(pos + 1);
            }
            int pos2 = name.lastIndexOf(".");
            if (pos2 != -1) {
                name = name.substring(0, pos2);
            }
            return name;
        }

        return null;
    }

    /**
     * 保存bitmap到内存卡
     *
     * @param bitmap
     * @param filepath
     * @return
     */
    public static boolean savaBitmap(Bitmap bitmap, String filepath) {
        File f = new File(filepath);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        bitmap.compress(CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 调用系统图库更新
     *
     * @param context
     * @param path
     */
    public static void refreshImg(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null,
                null);
    }

    /**
     * 调用系统图库更新
     *
     * @param context
     * @param paths   图片地址数组String[]
     */
    public static void refreshImg(Context context, String[] paths) {
        MediaScannerConnection.scanFile(context, paths, null, null);
    }

    /**
     * 调用系统选图功能
     *
     * @param act
     * @param requestCode
     */
    public static void pickPhoto(Activity act, int requestCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            act.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            LibPublicUtil.showToastErrorData(act);
        }

    }

    /**
     * 调用系统选图功能
     */
    public static void pickPhoto(Context context, Fragment frag, int requestCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            frag.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            LibPublicUtil.showToastErrorData(context);
        }

    }

    /**
     * 返回系统选图的路径
     *
     * @param data
     * @param context
     * @return
     */
    public static String returnPickPhotoPath(Intent data, Context context) {
        if (data == null)
            return null;
        Uri uri = data.getData();
        if (uri == null) {
            return null;
        }
        String path = uri.getPath();
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        } catch (Exception e) {
            return path;
        }
    }

    /**
     * 按尺寸裁剪图片
     *
     * @param Path    图片路径
     * @param savedir 保存的文件夹路径
     * @param size    裁剪大小
     * @return
     */
    public static String ClipImage(String Path, String savedir, int size) {
        int rotation = getJpgRotation(Path) % 360;
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Path, localOptions);
        if (localOptions.outWidth > size || localOptions.outHeight > size
                || rotation != 0) {
            System.out.println("clip");
            // Bitmap bmp = IcyUtils.decodeFile(Path, 2048);
            Bitmap bmp = getImgScale(Path, size - 5, false);
            if (bmp != null) {
                String cacheDir = savedir;
                File dir = new File(cacheDir);
                if (dir.exists() == false) {
                    dir.mkdirs();
                }
                bmp = scaleBitmap(bmp, size - 5);
                if (rotation != 0) {
                    Matrix m = new Matrix();
                    m.setRotate(rotation);
                    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                            bmp.getHeight(), m, false);
                }
                System.gc();
                String strTempFile = savedir
                        + Path.substring(Path.lastIndexOf('/'));
                System.out.println(strTempFile);
                try {
                    FileOutputStream fos = new FileOutputStream(strTempFile);
                    bmp.compress(CompressFormat.JPEG, 85, fos);
                    fos.close();
                    return strTempFile;
                } catch (Exception e) {
                }
            }
        }
        return Path;
    }

    public static Bitmap scaleBitmap(Bitmap bmp, int size) {
        Config config = bmp.getConfig();
        if (config == null) {
            config = Config.ARGB_8888;
        }
        return scaleBitmap(bmp, size, config, false);
    }

    public static Bitmap scaleBitmap(Bitmap bmp, int size, boolean keepQuality) {
        Config config = bmp.getConfig();
        if (config == null) {
            config = Config.ARGB_8888;
        }
        return scaleBitmap(bmp, size, config, keepQuality);
    }

    public static Bitmap scaleBitmap(Bitmap bmp, int size, Config config,
                                     boolean keepQuality) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        if (keepQuality == true && w < size && h < size) {
            return bmp;
        }
        int dw = 0;
        int dh = 0;
        float r1 = (float) w / (float) h;
        if (r1 < 1) {
            dh = size;
            dw = (int) (size * r1);
        } else {
            dw = size;
            dh = (int) (size / r1);
        }
        Bitmap bitmap = scaleBitmap(bmp, dw, dh, config);
        return bitmap;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int w, int h, Config config) {
        if (w < 1)
            w = 1;
        if (h < 1)
            h = 1;
        Bitmap bmp = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bmp);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, w, h), null);
        return bmp;
    }

    /*************************调用系统剪裁图片功能*********************************/
    /**
     * 调用系统图片剪裁功能
     *
     * @param act
     * @param fragment
     * @param uri         图片资源Uri
     * @param aspectX     x相对y的比例
     * @param aspectY     y比例x的比例
     * @param outputX     输入图片的宽
     * @param outputY     输入图片的高
     * @param requestCode 请求code,在result中接收
     */
    private static void cropPhoto(Activity act, Fragment fragment, Uri uri,
                                  int aspectX, int aspectY, int outputX, int outputY, int
                                          requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else if (act != null) {
            act.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * Activity调用系统图片剪裁功能
     *
     * @param act
     * @param uri
     * @param aspectX     x比例
     * @param aspectY     y比例
     * @param outputX     宽
     * @param outputY     高
     * @param requestCode 请求code,在result中接收
     */
    public static void cropPhoto(Activity act, Uri uri, int aspectX, int aspectY, int outputX,
                                 int outputY, int requestCode) {
        cropPhoto(act, null, uri, aspectX, aspectY, outputX, outputY, requestCode);
    }

    /**
     * Fragment调用系统图片剪裁功能
     *
     * @param fragment
     * @param uri
     * @param aspectX     x比例
     * @param aspectY     y比例
     * @param outputX     宽
     * @param outputY     高
     * @param requestCode 请求code,在result中接收
     */
    public static void cropPhoto(Fragment fragment, Uri uri, int aspectX, int aspectY, int
            outputX, int outputY, int requestCode) {
        cropPhoto(null, fragment, uri, aspectX, aspectY, outputX, outputY, requestCode);
    }

    /**
     * 根据宽高大小剪裁图片
     */
    public static void cropPhoto(Activity act, Uri uri, int outputX, int outputY, int requestCode) {
        cropPhoto(act, null, uri, 1, outputY / outputX, outputX, outputY, requestCode);
    }

    /**
     * 根据宽高大小剪裁图片
     */
    public static void cropPhoto(Fragment fragment, Uri uri, int outputX, int outputY, int
            requestCode) {
        cropPhoto(null, fragment, uri, 1, outputY / outputX, outputX, outputY, requestCode);
    }

    /**
     * 根据宽高比例和宽度剪裁图片
     */
    public static void cropPhoto(Activity act, Uri uri, int aspectX, int aspectY, int outputX,
                                 int requestCode) {
        cropPhoto(act, null, uri, aspectX, aspectY, outputX, aspectY * outputX / aspectX,
                requestCode);
    }

    /**
     * 根据宽高比例和宽度剪裁图片
     */
    public static void cropPhoto(Fragment fragment, Uri uri, int aspectX, int aspectY, int size,
                                 int requestCode) {
        cropPhoto(null, fragment, uri, 1, 1, size, size, requestCode);
    }

    /**
     * 剪裁固定边长的正方形图片
     */
    public static void cropPhoto(Activity act, Uri uri, int size, int requestCode) {
        cropPhoto(act, null, uri, 1, 1, size, size, requestCode);
    }

    /**
     * 剪裁固定边长的正方形图片
     */
    public static void cropPhoto(Fragment fragment, Uri uri, int size, int requestCode) {
        cropPhoto(null, fragment, uri, 1, 1, size, size, requestCode);
    }

    /**
     * 获取总大小不超过maxImgLength的压缩bitmap
     *
     * @param absolutePath 图片地址
     * @param maxImgLength 图片最大长 * 宽
     * @return
     */
    public static byte[] getImgScale(String absolutePath, int maxImgLength) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(absolutePath, opts);

//        Log.e("ORI_IMG", opts.outWidth + " * " + opts.outHeight);
        // 首次缩放后加入内存
        int inSampleSize = opts.outWidth * opts.outHeight / maxImgLength;
        inSampleSize = (int) Math.sqrt(inSampleSize);
        if (inSampleSize < 1) {
            inSampleSize = 1;
        }

        opts.inSampleSize = inSampleSize;
        opts.inJustDecodeBounds = false;
        Bitmap mImg = BitmapFactory.decodeFile(absolutePath, opts);

        // 旋转
        int mRotate = getJpgRotation(absolutePath);
        if (mRotate != 0) {
            Matrix m = new Matrix();
            m.setRotate(mRotate);
            mImg = Bitmap.createBitmap(mImg, 0, 0, mImg.getWidth(),
                    mImg.getHeight(), m, false);
        }

        if (inSampleSize <= 1) {
            byte[] result = getBytes(mImg);
            mImg.recycle();
            return result;
        }

        // 最终缩放到尺寸总大小800*600以内
        float rate = (float) maxImgLength / (mImg.getWidth() * mImg.getHeight());
        float scaleRate = (float) Math.sqrt(rate);

        Matrix matrix = new Matrix();
        matrix.postScale(scaleRate, scaleRate); //用于产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(mImg, 0, 0, mImg.getWidth(), mImg.getHeight(),
                matrix, false);
        mImg.recycle();

        byte[] result = getBytes(resizeBitmap);
        resizeBitmap.recycle();
        return result;
    }

    public static byte[] getBytes(Bitmap bit) {
//        Log.e("GET_IMG", "(" + bit.getWidth() + ", " + bit.getHeight() + ") * 4");
//        Log.e("START_TIME", System.currentTimeMillis() + "");
        byte[] buffer = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bit.compress(CompressFormat.JPEG, 90, out);
            buffer = out.toByteArray();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.e("END_TIME", System.currentTimeMillis() + ",");
//        Log.e("COMPRESSED", buffer.length + "\n\n");
        return buffer;
    }
}
