package cn.com.bluemoon.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.bluemoon.lib.qrcode.R;

public class LibViewUtil {


    public static void setListViewHeight2(ListView listView) {
        if (listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(
                listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public static void setListViewHeight(ListView listView) {
        if (listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public static void hideIM(View v) {
        try {
            InputMethodManager im = (InputMethodManager) v.getContext().getSystemService(Activity
                    .INPUT_METHOD_SERVICE);
            IBinder windowToken = v.getWindowToken();
            if (windowToken != null) {
                im.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception e) {

        }
    }

    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }


    /**
     * 设置控件可见性
     */
    public static void setViewVisibility(View view, int visibility) {
        if (view != null && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    /**
     * 获取控件的可见性，空则不可见
     */
    public static int getViewVisibility(View view) {
        if (view != null) {
            return view.getVisibility();
        }
        return View.GONE;
    }

    /**
     * 递归设置子控件的enable
     */
    public static void setChildEnableRecursion(ViewGroup layout, boolean isEnable) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                setChildEnableRecursion((ViewGroup) child, isEnable);
            } else {
                if (isEnable) {
                    Boolean enable = (Boolean) child.getTag(R.id.tag_ori_enable);
                    child.setEnabled(enable != null ? enable : isEnable);
                } else {
                    child.setTag(R.id.tag_ori_enable, child.isEnabled());
                    child.setEnabled(false);
                }
            }
        }
    }

    public static void longToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static void toast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 解决TextView多行时ellipsize end在字数比满maxLines+1后不起作用的情况
     *
     * @param maxLines 要限制的最大行数
     * @param content  指TextView中要显示的内容
     */
    public static void setMaxEcplise(final TextView mTextView, final int maxLines, final String
            content) {

        ViewTreeObserver observer = mTextView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTextView.setText(content);
                if (mTextView.getLineCount() > maxLines) {
                    int lineEndIndex = mTextView.getLayout().getLineEnd(maxLines - 1);
                    //下面这句代码中：我在项目中用数字3发现效果不好，改成1了
                    String text = content.subSequence(0, lineEndIndex - 1) + "...";
                    mTextView.setText(text);
                } else {
                    removeGlobalOnLayoutListener(mTextView.getViewTreeObserver(), this);
                }
            }
        });
    }

    private static void removeGlobalOnLayoutListener(ViewTreeObserver obs, ViewTreeObserver
            .OnGlobalLayoutListener listener) {
        if (obs == null)
            return;
        if (Build.VERSION.SDK_INT < 16) {
            obs.removeGlobalOnLayoutListener(listener);
        } else {
            obs.removeOnGlobalLayoutListener(listener);
        }
    }
}
