package cn.com.bluemoon.lib.callback;

import android.graphics.Bitmap;
import android.view.View;

public abstract class ImageDialogCallback {

    public void showResult() {
    }

    public void dismissResult() {
    }

    public boolean longClick(View view,Bitmap bit) {
        return false;
    }
}
