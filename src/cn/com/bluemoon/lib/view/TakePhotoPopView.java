package cn.com.bluemoon.lib.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;

import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.utils.LibImageUtil;
import cn.com.bluemoon.lib.utils.LibPublicUtil;

public class TakePhotoPopView {

    private LinearLayout ll_popup;
    private PopupWindow pop;
    private Context context;
    private Fragment fragment;
    private int TAKE_PIC_RESULT;
    private int CHOSE_PIC_RESULT;
    private DismissListener listener;
    private boolean isCancel;

    public TakePhotoPopView(Context context, int TAKE_PIC_RESULT, int CHOSE_PIC_RESULT) {
        super();
        this.context = context;
        this.TAKE_PIC_RESULT = TAKE_PIC_RESULT;
        this.CHOSE_PIC_RESULT = CHOSE_PIC_RESULT;
        popupInit();
    }

    public TakePhotoPopView(Context context, int TAKE_PIC_RESULT, int CHOSE_PIC_RESULT, DismissListener listener) {
        super();
        this.context = context;
        this.TAKE_PIC_RESULT = TAKE_PIC_RESULT;
        this.CHOSE_PIC_RESULT = CHOSE_PIC_RESULT;
        this.listener = listener;
        popupInit();
    }

    public TakePhotoPopView(Context context, Fragment fragment, int TAKE_PIC_RESULT, int CHOSE_PIC_RESULT) {
        super();
        this.context = context;
        this.fragment = fragment;
        this.TAKE_PIC_RESULT = TAKE_PIC_RESULT;
        this.CHOSE_PIC_RESULT = CHOSE_PIC_RESULT;
        popupInit();
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void getPic(View v) {
        isCancel = true;
        ll_popup.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.activity_translate_up_in));
        pop.showAtLocation(v, Gravity.BOTTOM, 0, 1);
    }


    @SuppressLint("NewApi")
    public void popupInit() {

        pop = new PopupWindow(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_popupwindows, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.MATCH_PARENT);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        pop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_transparent));
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (listener != null && isCancel) {
                    listener.cancelReceiveValue();
                }
            }
        });
        parent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (fragment != null) {
                    takePhoto(fragment);
                } else {
                    takePhoto((Activity) context);
                }
                isCancel = false;
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (fragment != null) {
                    pickPhoto(fragment);
                } else {
                    pickPhoto((Activity) context);
                }
                isCancel = false;
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.cancelReceiveValue();
                }
                isCancel = false;
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }

    private File out;

    public void takePhoto(Activity act) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        out = new File(LibPublicUtil.getCameraPath());
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        act.startActivityForResult(intent, TAKE_PIC_RESULT);
    }

    public void pickPhoto(Activity act) {
        LibImageUtil.pickPhoto(act, CHOSE_PIC_RESULT);
    }

    public void takePhoto(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        out = new File(LibPublicUtil.getCameraPath());
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        fragment.startActivityForResult(intent, TAKE_PIC_RESULT);
    }

    public void pickPhoto(Fragment fragment) {
        LibImageUtil.pickPhoto(context, fragment, CHOSE_PIC_RESULT);
    }

    public File getTakeImageFile() {
        return out;
    }

    public Uri getTakeImageUri() {
        return Uri.fromFile(out);
    }

    public Uri getPickImageUri(Intent data) {
        if (data == null)
            return null;
        return data.getData();
    }

    public String getPickPhotoPath(Intent data) {
        return LibImageUtil.returnPickPhotoPath(data, context);
    }


    public Bitmap getTakeImageBitmap(int size) {
        if (out == null || !out.exists()) {
            Toast.makeText(context, context.getResources().getString(R.string.error_take_photo), Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        return LibImageUtil.getImgScale(out.getAbsolutePath(), size, false);

    }

    public Bitmap getPickImageBitmap(Intent data, int size) {
        String path_change = getPickPhotoPath(data);
        if (path_change == null) {
            Toast.makeText(context, context.getResources().getString(R.string.error_take_photo), Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        return LibImageUtil.getImgScale(path_change, size, false);
    }

    public Bitmap getTakeImageBitmap() {
        return getTakeImageBitmap(300);
    }

    public Bitmap getPickImageBitmap(Intent data) {
        return getPickImageBitmap(data, 300);
    }

    public Drawable getTakeImageDrawable(int size) {
        return new BitmapDrawable(getTakeImageBitmap(size));
    }

    public Drawable getPickImageDrawable(Intent data, int size) {
        return new BitmapDrawable(getPickImageBitmap(data, size));
    }

    public Drawable getTakeImageDrawable() {
        return new BitmapDrawable(getTakeImageBitmap());
    }

    public Drawable getPickImageDrawable(Intent data) {
        return new BitmapDrawable(getPickImageBitmap(data));
    }

    public interface DismissListener {
        public void cancelReceiveValue();

    }

}
