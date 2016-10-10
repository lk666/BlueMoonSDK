package cn.com.bluemoon.lib.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;

import cn.com.bluemoon.lib.callback.CodeDialogCallback;
import cn.com.bluemoon.lib.callback.ImageDialogCallback;
import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.qrcode.utils.BarcodeUtil;
import cn.com.bluemoon.lib.view.ImageDialog;
import cn.com.bluemoon.lib.view.QRCodeDialog;

/**
 * Created by bm on 2016/7/22.
 */
public class LibDialogUtil {

    /***
     * 获取一个dialog
     *
     * @param context context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

    /***
     * 获取一个耗时等待对话框
     *
     * @param context context
     * @return
     */
    public static ProgressDialog getWaitDialog(Context context) {
        return new ProgressDialog(context);
    }

    /***
     * 获取一个信息对话框，注意需要自己手动调用show方法显示
     *
     * @param context
     * @param message
     * @param onClickListener
     * @return
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String message,
                                                       DialogInterface.OnClickListener
                                                               onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.btn_ok_space),
                onClickListener);
        return builder;
    }

    public static AlertDialog.Builder getMessageDialog(Context context, String message) {
        return getMessageDialog(context, message, null);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message,
                                                       DialogInterface.OnClickListener
                                                               onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton(context.getResources().getString(R.string.btn_ok_space),
                onClickListener);
        builder.setNegativeButton(context.getResources().getString(R.string.btn_cancel_space),
                null);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message,
                                                       DialogInterface.OnClickListener
                                                               onOkClickListener, DialogInterface
                                                               .OnClickListener
                                                               onCancelClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.btn_ok_space),
                onOkClickListener);
        builder.setNegativeButton(context.getResources().getString(R.string.btn_cancel_space),
                onCancelClickListener);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context,
                                                       String message,
                                                       String okString,
                                                       String cancelString,
                                                       DialogInterface.OnClickListener
                                                               onOkClickListener,
                                                       DialogInterface.OnClickListener
                                                               onCancelClickListener) {
        return getConfirmDialog(context, "", message, okString, cancelString, onOkClickListener,
                onCancelClickListener);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context,
                                                       String title,
                                                       String message,
                                                       String okString,
                                                       String cancelString,
                                                       DialogInterface.OnClickListener
                                                               onOkClickListener,
                                                       DialogInterface.OnClickListener
                                                               onCancelClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setPositiveButton(okString, onOkClickListener);
        builder.setNegativeButton(cancelString, onCancelClickListener);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[]
            arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton(context.getResources().getString(R.string.btn_cancel_space),
                null);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays,
                                                      DialogInterface.OnClickListener
                                                              onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title,
                                                            String[] arrays, int selectIndex,
                                                            DialogInterface.OnClickListener
                                                                    onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setNegativeButton(context.getResources().getString(R.string.btn_cancel_space),
                null);
        return builder;
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays, int
            selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, "", arrays, selectIndex, onClickListener);
    }
    /**
     * 旧方法
     */
    public static QRCodeDialog showCodeDialog(Activity context,String title,
                                              String codeUrl,String code,String str,String tips,String path,CodeDialogCallback cb)
    {
        QRCodeDialog codeDialog = new QRCodeDialog();
        if(!codeDialog.isVisible())
        {
            if(title!=null) codeDialog.setTitle(title);
            if(str!= null) codeDialog.setString(str);
            if(code!=null) codeDialog.setCode(code);
            if(tips!=null) codeDialog.setContent(tips);
            if(cb!=null) codeDialog.setCallback(cb);
            codeDialog.show(context.getFragmentManager(), "dialog");
        }
        return codeDialog;
    }

    /**
     * 旧方法
     */
    public static ImageDialog showPictureDialog(Activity context,Bitmap bm,String imgUrl,String path,ImageDialogCallback cb)
    {
        ImageDialog picDialog = new ImageDialog();
        if (!picDialog.isVisible()) {
            if (imgUrl != null) {
                picDialog.setCodeUrl(imgUrl);
            } else {
                picDialog.setBitmap(bm);
            }
            picDialog.setCallback(cb);
            picDialog.show(context.getFragmentManager(), "dialog");
        }
        return picDialog;
    }

    /**
     * 展示二维码
     * @param context
     * @param code
     * @param bit
     * @param title
     * @param str
     * @param tips
     * @param cb
     * @return
     */
    public static QRCodeDialog showCodeDialog(Activity context, String code, Bitmap bit, String
            title, String str, String tips, CodeDialogCallback cb) {
        QRCodeDialog codeDialog = new QRCodeDialog();
        if (!codeDialog.isVisible()) {
            if (code != null) {
                codeDialog.setCode(code);
            } else {
                codeDialog.setBitmap(bit);
            }
            codeDialog.setTitle(title);
            codeDialog.setString(str);
            codeDialog.setContent(tips);
            codeDialog.setCallback(cb);
            codeDialog.show(context.getFragmentManager(), "dialog");
        }
        return codeDialog;
    }

    /**
     * 展示图片
     * @param context
     * @param bm
     * @param imgUrl
     * @param cb
     * @return
     */
    public static ImageDialog showPictureDialog(Activity context, Bitmap bm, String imgUrl,ImageDialogCallback cb) {
        ImageDialog picDialog = new ImageDialog();
        if (!picDialog.isVisible()) {
            if (imgUrl != null) {
                picDialog.setCodeUrl(imgUrl);
            } else {
                picDialog.setBitmap(bm);
            }
            picDialog.setCallback(cb);
            picDialog.show(context.getFragmentManager(), "dialog");
        }
        return picDialog;
    }
}
