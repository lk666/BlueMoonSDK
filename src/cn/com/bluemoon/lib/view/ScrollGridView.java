package cn.com.bluemoon.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * TODO 解决GridView在SrollView里面显示不全
 *
 * @author wangshanhai
 * @version 3.1.0
 * @time 2016年3月28日 上午10:16:00
 */
public class ScrollGridView extends GridView {
    private boolean mIsScrollable = false;

    public ScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public ScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ScrollGridView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void setScrollaable(boolean isScrollable) {
        this.mIsScrollable = isScrollable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        if (mIsScrollable) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

}
