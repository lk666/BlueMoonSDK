package cn.com.bluemoon.lib.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.com.bluemoon.lib.qrcode.R;

/**
 * 实现动画可拖动item的GridView
 *
 * @author chenchongsen
 * @version 0.0.1
 */
@SuppressLint("WrongCall")
public class DragGridView extends ViewGroup implements View.OnTouchListener,
        View.OnClickListener, View.OnLongClickListener {
    protected static int COL_COUNT = 2;
    private static int animT = 250;
    private int bgResId = 0;
    private int bgCheckResId = 0;
    private boolean scrollable;
    protected int colCount, childSize, padding, dpi, scroll = 0;
    protected float lastDelta = 0;
    protected Handler handler = new Handler();
    // dragging vars
    protected int dragged = -1, lastX = -1, lastY = -1, lastTarget = -1;
    protected boolean enabled = true, touching = false;

    protected ArrayList<Integer> newPositions = new ArrayList<>();
    // listeners
    protected OnRearrangeListener onRearrangeListener;
    protected OnClickListener secondaryOnClickListener;
    private OnItemClickListener onItemClickListener;
    private Context context;

    /**
     * 拖动item的接口
     */
    public interface OnRearrangeListener {

        public abstract void onRearrange(int oldIndex, int newIndex);
    }

    public DragGridView(Context context) {
        super(context);
        init(context, null);
    }

    // CONSTRUCTOR AND HELPERS
    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        readStyleParameters(context, attrs);
        this.context = context;
        setListeners();
        handler.removeCallbacks(updateTask);
        handler.postAtTime(updateTask, SystemClock.uptimeMillis() + 500);
        setChildrenDrawingOrderEnabled(true);

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        dpi = metrics.densityDpi;
    }

    protected void setListeners() {
        setOnTouchListener(this);
        super.setOnClickListener(this);
        setOnLongClickListener(this);
    }

    private void readStyleParameters(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet,
                R.styleable.DragGridView);
        try {
            COL_COUNT = a.getInt(R.styleable.DragGridView_dragCol, 3);
            if (COL_COUNT < 1) COL_COUNT = 1;
            animT = a.getInt(R.styleable.DragGridView_dragAnimTime, 200);
            bgResId = a.getColor(R.styleable.DragGridView_dragBg, 0);
            bgCheckResId = a.getColor(R.styleable.DragGridView_dragCheckBg, 0);
            scrollable = a.getBoolean(R.styleable.DragGridView_dragScrollable, false);
        } finally {
            a.recycle();
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        secondaryOnClickListener = l;
    }

    protected Runnable updateTask = new Runnable() {
        public void run() {
            if (dragged != -1) {
                if (lastY < padding * 3 && scroll > 0)
                    scroll -= 20;
                else if (lastY > getBottom() - getTop() - (padding * 3)
                        && scroll < getMaxScroll())
                    scroll += 20;
            } else if (lastDelta != 0 && !touching) {
                scroll += lastDelta;
                lastDelta *= .9;
                if (Math.abs(lastDelta) < .25)
                    lastDelta = 0;
            }
            clampScroll();
            onLayout(true, getLeft(), getTop(), getRight(), getBottom());

            handler.postDelayed(this, 25);
        }
    };

    @Override
    public void addView(View child) {
        super.addView(child);
        newPositions.add(-1);
    }

    ;

    public void replaceView(int index, View child) {
        if (getChildAt(index) != null) {
            removeViewAt(index);
            addView(view2ImgView(child), index);
            if (newPositions.size() > index) {
                newPositions.add(index, -1);
            } else {
                newPositions.add(-1);
            }
        }
    }

    public void setViews(List<View> list) {
        removeAllViews();
        newPositions.clear();
        if (list == null) return;
        for (int i = 0; i < list.size(); i++) {
            addView(view2ImgView(list.get(i)));
        }
    }

    public View view2ImgView(View view) {
        ImageView imgView = new ImageView(context);
        if (bgResId != 0) imgView.setBackgroundColor(bgResId);
        int padding = 0;
        if (colCount <= 2) {
            padding = getResources().getDimensionPixelOffset(R.dimen.space_10);
        } else {
            padding = getResources().getDimensionPixelOffset(R.dimen.space_5);
        }
        imgView.setPadding(padding, padding, padding, padding);

        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bm = Bitmap.createBitmap(view.getDrawingCache());
        view.destroyDrawingCache();
        imgView.setImageBitmap(bm);
        return imgView;
    }

    public void setViews(Map<Integer, View> map) {
        removeAllViews();
        if (map == null) return;
        List<View> list = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            View view = map.get(i);
            if (view != null) {
                list.add(map.get(i));
            }
        }
        setViews(list);
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        newPositions.remove(index);
    }

    ;

    // LAYOUT
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // compute width of view, in dp
        float w = (r - l) / (dpi / 160f);

        // determine number of columns, at least 2
        colCount = COL_COUNT - 1;
        int sub = 240;
        w -= 280;
        while (w > 0) {
            colCount++;
            w -= sub;
            sub += 40;
        }

        // determine childSize and padding, in px
        childSize = (r - l) / colCount;
        float childRatio = .9f;
        childSize = Math.round(childSize * childRatio);
        padding = ((r - l) - (childSize * colCount)) / (colCount + 1);

        for (int i = 0; i < getChildCount(); i++)
            if (i != dragged) {
                Point xy = getCoorFromIndex(i);
                getChildAt(i).layout(xy.x, xy.y, xy.x + childSize,
                        xy.y + childSize);
            }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (dragged == -1)
            return i;
        else if (i == childCount - 1)
            return dragged;
        else if (i >= dragged)
            return i + 1;
        return i;
    }

    public int getIndexFromCoor(int x, int y) {
        int col = getColOrRowFromCoor(x), row = getColOrRowFromCoor(y + scroll);
        if (col == -1 || row == -1) // touch is between columns or rows
            return -1;
        int index = row * colCount + col;
        if (index >= getChildCount())
            return -1;
        return index;
    }

    protected int getColOrRowFromCoor(int coor) {
        coor -= padding;
        for (int i = 0; coor > 0; i++) {
            if (coor < childSize)
                return i;
            coor -= (childSize + padding);
        }
        return -1;
    }

    protected int getTargetFromCoor(int x, int y) {
        if (getColOrRowFromCoor(y + scroll) == -1) // touch is between rows
            return -1;
        // if (getIndexFromCoor(x, y) != -1) //touch on top of another visual
        // return -1;

        int leftPos = getIndexFromCoor(x - (childSize / 4), y);
        int rightPos = getIndexFromCoor(x + (childSize / 4), y);
        if (leftPos == -1 && rightPos == -1) // touch is in the middle of
            // nowhere
            return -1;
        if (leftPos == rightPos) // touch is in the middle of a visual
            return -1;

        int target = -1;
        if (rightPos > -1)
            target = rightPos;
        else if (leftPos > -1)
            target = leftPos + 1;
        if (dragged < target)
            return target - 1;

        // Toast.makeText(getContext(), "Target: " + target + ".",
        // Toast.LENGTH_SHORT).show();
        return target;
    }

    protected Point getCoorFromIndex(int index) {
        int col = index % colCount;
        int row = index / colCount;
        return new Point(padding + (childSize + padding) * col, padding
                + (childSize + padding) * row - scroll);
    }

    public int getIndexOf(View child) {
        for (int i = 0; i < getChildCount(); i++)
            if (getChildAt(i) == child)
                return i;
        return -1;
    }

    // EVENT HANDLERS
    public void onClick(View view) {
        if (enabled) {
            if (secondaryOnClickListener != null)
                secondaryOnClickListener.onClick(view);
            if (onItemClickListener != null && getLastIndex() != -1)
                onItemClickListener.onItemClick(null,
                        getChildAt(getLastIndex()), getLastIndex(),
                        getLastIndex() / colCount);
        }
    }

    public void vibrate() {
        Vibrator mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(1000L);
    }

    public boolean onLongClick(View view) {
        if (!enabled)
            return false;
        int index = getLastIndex();
        if (index != -1) {
            dragged = index;
            animateDragged();
            vibrate();
            return true;
        }
        return false;
    }

    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                enabled = true;
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                refreshBackground(getLastIndex());
                touching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int delta = lastY - (int) event.getY();
                if (dragged != -1) {
                    // change draw location of dragged visual
                    int x = (int) event.getX(), y = (int) event.getY();
                    int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
                    getChildAt(dragged).layout(l, t, l + (childSize * 3 / 2),
                            t + (childSize * 3 / 2));

                    // check for new target hover
                    int target = getTargetFromCoor(x, y);
                    if (lastTarget != target) {
                        if (target != -1) {
                            animateGap(target);
                            lastTarget = target;
                        }
                    }
                } else if (scrollable) {
                    scroll += delta / 2;
                    clampScroll();
                    if (Math.abs(delta) > 5) {
                        refreshBackground(-1);
                        enabled = false;
                    }
                    onLayout(true, getLeft(), getTop(), getRight(), getBottom());
                } else {
                    if (Math.abs(delta) > 5)
                        enabled = false;
                }
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                if (scrollable)
                    lastDelta = delta;
                break;
            case MotionEvent.ACTION_UP:
                refreshBackground(-1);
                if (dragged != -1) {
                    View v = getChildAt(dragged);
                    if (lastTarget != -1)
                        reorderChildren();
                    else {
                        Point xy = getCoorFromIndex(dragged);
                        v.layout(xy.x, xy.y, xy.x + childSize, xy.y + childSize);
                    }
                    v.clearAnimation();
                    if (v instanceof ImageView)
                        ((ImageView) v).setAlpha(255);
                    lastTarget = -1;
                    dragged = -1;
                }
                touching = false;
                break;
        }
        if (dragged != -1)
            return true;
        return false;
    }

    protected void refreshBackground(int index) {
        if (bgCheckResId == 0) return;

        for (int i = 0; i < getChildCount(); i++) {
            if (index == -1) {
                if (bgResId != 0) {
                    getChildAt(i).setBackgroundColor(bgResId);
                } else {
                    getChildAt(i).setBackgroundDrawable(null);
                }
            } else if (i == index) {
                getChildAt(i).setBackgroundColor(bgCheckResId);
                break;
            }
        }
    }

    // EVENT HELPERS
    protected void animateDragged() {
        View v = getChildAt(dragged);
        int x = getCoorFromIndex(dragged).x + childSize / 2, y = getCoorFromIndex(dragged).y
                + childSize / 2;
        int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
        v.layout(l, t, l + (childSize * 3 / 2), t + (childSize * 3 / 2));
        AnimationSet animSet = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(.667f, .7f, .667f, .7f,
                childSize * 3 / 4, childSize * 3 / 4);
        scale.setDuration(animT);
        AlphaAnimation alpha = new AlphaAnimation(1, .6f);
        alpha.setDuration(animT);

        animSet.addAnimation(scale);
        animSet.addAnimation(alpha);
        animSet.setFillEnabled(true);
        animSet.setFillAfter(true);

        v.clearAnimation();
        v.startAnimation(animSet);
    }

    protected void animateGap(int target) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (i == dragged)
                continue;
            int newPos = i;
            if (dragged < target && i >= dragged + 1 && i <= target)
                newPos--;
            else if (target < dragged && i >= target && i < dragged)
                newPos++;

            // animate
            int oldPos = i;
            if (newPositions.get(i) != -1)
                oldPos = newPositions.get(i);
            if (oldPos == newPos)
                continue;

            Point oldXY = getCoorFromIndex(oldPos);
            Point newXY = getCoorFromIndex(newPos);
            Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y
                    - v.getTop());
            Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y
                    - v.getTop());

            TranslateAnimation translate = new TranslateAnimation(
                    Animation.ABSOLUTE, oldOffset.x, Animation.ABSOLUTE,
                    newOffset.x, Animation.ABSOLUTE, oldOffset.y,
                    Animation.ABSOLUTE, newOffset.y);
            translate.setDuration(animT);
            translate.setFillEnabled(true);
            translate.setFillAfter(true);
            v.clearAnimation();
            v.startAnimation(translate);

            newPositions.set(i, newPos);
        }
    }

    protected void reorderChildren() {
        // figure out how to reorder children without removing them all and
        // reconstructing the list!!!
        if (onRearrangeListener != null)
            onRearrangeListener.onRearrange(dragged, lastTarget);
        ArrayList<View> children = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
            children.add(getChildAt(i));
        }
        removeAllViews();
        while (dragged != lastTarget)
            if (lastTarget == children.size()) // dragged and dropped to the
            // right of the last element
            {
                children.add(children.remove(dragged));
                dragged = lastTarget;
            } else if (dragged < lastTarget) // shift to the right
            {
                Collections.swap(children, dragged, dragged + 1);
                dragged++;
            } else if (dragged > lastTarget) // shift to the left
            {
                Collections.swap(children, dragged, dragged - 1);
                dragged--;
            }
        for (int i = 0; i < children.size(); i++) {
            newPositions.set(i, -1);
            addView(children.get(i));
        }
        onLayout(true, getLeft(), getTop(), getRight(), getBottom());
    }

    public void scrollToTop() {
        scroll = 0;
    }

    public void scrollToBottom() {
        scroll = Integer.MAX_VALUE;
        clampScroll();
    }

    protected void clampScroll() {
        int stretch = 4, overreach = getHeight() / 2;
        int max = getMaxScroll();
        max = Math.max(max, 0);

        if (scroll < -overreach) {
            scroll = -overreach;
            lastDelta = 0;
        } else if (scroll > max + overreach) {
            scroll = max + overreach;
            lastDelta = 0;
        } else if (scroll < 0) {
            if (scroll >= -stretch)
                scroll = 0;
            else if (!touching)
                scroll -= scroll / stretch;
        } else if (scroll > max) {
            if (scroll <= max + stretch)
                scroll = max;
            else if (!touching)
                scroll += (max - scroll) / stretch;
        }
    }

    protected int getMaxScroll() {
        int rowCount = (int) Math.ceil((double) getChildCount() / colCount), max = rowCount
                * childSize + (rowCount + 1) * padding - getHeight();
        return max;
    }

    public int getLastIndex() {
        return getIndexFromCoor(lastX, lastY);
    }

    // OTHER METHODS
    public void setOnRearrangeListener(OnRearrangeListener l) {
        this.onRearrangeListener = l;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.onItemClickListener = l;
    }
}