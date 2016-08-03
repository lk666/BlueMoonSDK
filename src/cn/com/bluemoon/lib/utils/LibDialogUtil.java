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
     * @param context context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

    /***
     * 获取一个耗时等待对话框
     * @param context context
     * @return
     */
    public static ProgressDialog getWaitDialog(Context context) {
        return new ProgressDialog(context);
    }

    /***
     * 获取一个信息对话框，注意需要自己手动调用show方法显示
     * @param context
     * @param message
     * @param onClickListener
     * @return
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.btn_ok_space), onClickListener);
        return builder;
    }

    public static AlertDialog.Builder getMessageDialog(Context context, String message) {
        return getMessageDialog(context, message, null);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton(context.getResources().getString(R.string.btn_ok_space), onClickListener);
        builder.setNegativeButton(context.getResources().getString(R.string.btn_cancel_space), null);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancelClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.btn_ok_space), onOkClickListener);
        builder.setNegativeButton(context.getResources().getString(R.string.btn_cancel_space), onCancelClickListener);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context,
                                                       String message,
                                                       String okString,
                                                       String cancelString,
                                                       DialogInterface.OnClickListener onOkClickListener,
                                                       DialogInterface.OnClickListener onCancelClickListener) {
        return getConfirmDialog(context, "", message, okString, cancelString, onOkClickListener, onCancelClickListener);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context,
                                                       String title,
                                                       String message,
                                                       String okString,
                                                       String cancelString,
                                                       DialogInterface.OnClickListener onOkClickListener,
                                                       DialogInterface.OnClickListener onCancelClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setPositiveButton(okString, onOkClickListener);
        builder.setNegativeButton(cancelString, onCancelClickListener);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton(context.getResources().getString(R.string.btn_cancel_space), null);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setNegativeButton(context.getResources().getString(R.string.btn_cancel_space), null);
        return builder;
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, "", arrays, selectIndex, onClickListener);
    }

    public static QRCodeDialog showCodeDialog(Activity context,String title,
                                              String codeUrl,String code,String str,String tips,String path,CodeDialogCallback cb)
    {
        QRCodeDialog codeDialog = new QRCodeDialog();
        if(!codeDialog.isVisible())
        {
            codeDialog.setLoadString(context.getString(R.string.data_loading));
            if(title!=null) codeDialog.setTitle(title);
            if(codeUrl!=null)codeDialog.setCodeUrl(codeUrl);
            if(str!= null) codeDialog.setString(str);
            if(code!=null) codeDialog.setBitmap(BarcodeUtil.createQRCode(code));
            if(tips!=null) codeDialog.setContent(tips);
            if(cb!=null) codeDialog.setCallback(cb);
            if(path == null) codeDialog.setSavePath(path);
            codeDialog.show(context.getFragmentManager(), "dialog");
        }
        return codeDialog;
    }

    public static ImageDialog showPictureDialog(Activity context,Bitmap bm,String imgUrl,String path,ImageDialogCallback cb)
    {
        ImageDialog picDialog = new ImageDialog();
        picDialog.setCallback(cb);
        if (!picDialog.isVisible()) {
            picDialog.setLoadString(context.getString(R.string.data_loading));
            if (bm != null) {
                picDialog.setBitmap(bm);
            } else {
                if (imgUrl != null)
                    picDialog.setCodeUrl(imgUrl);
            }
            picDialog.setSavePath(path);
            picDialog.show(context.getFragmentManager(), "dialog");
        }
        return picDialog;
    }
}
