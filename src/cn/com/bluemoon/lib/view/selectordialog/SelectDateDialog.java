package cn.com.bluemoon.lib.view.selectordialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.view.kankan.wheel.widget.OnWheelChangedListener;
import cn.com.bluemoon.lib.view.kankan.wheel.widget.WheelView;
import cn.com.bluemoon.lib.view.kankan.wheel.widget.adapters.NumericWheelAdapter;

//例子：
//Calendar calendar = Calendar.getInstance();
//        SelectDateDialog mSelectDateDialog = new SelectDateDialog(this,
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH) + 1,
//        calendar.get(Calendar.DAY_OF_MONTH),
//        // 点击确定时的回调
//        new SelectDateDialog.OnButtonClickListener() {
//
//@Override
//public void onOKButtonClick(int year, int month,
//        int day) {
//        Toast.makeText(ActivityDescActivity.this, year + "年" + month +
//        "月" + day + "日", Toast.LENGTH_SHORT).show();
//        }
//
//@Override
//public void onClearButtonClick() {
//        Toast.makeText(ActivityDescActivity.this, "清除", Toast
//        .LENGTH_SHORT).show();
//        }
//        });
//        mSelectDateDialog.show();

/**
 * 滚轮日期选择弹窗
 *
 * @author LK
 */
public class SelectDateDialog extends Dialog {
    /**
     * 点击按钮时的回调
     */
    public OnButtonClickListener onButtonClickListener;

    /**
     * 点击按钮时的回调接口
     */
    public interface OnButtonClickListener {
        /**
         * 点击确定时的回调，此方法能在初始化对话框定义事件时直接使用选择的日期字符串
         *
         * @param year  年
         * @param month 月
         * @param day   日
         */
         void onOKButtonClick(int year, int month, int day);

        /**
         * 点击清除按钮的回调
         */
         void onClearButtonClick();
    }

    /**
     * 年列表适配器
     */
    SelectListAdapter yearForSelectAdapter;
    /**
     * 月列表适配器
     */
    SelectListAdapter monthForSelectAdapter;
    /**
     * 日列表适配器
     */
    SelectListAdapter dayForSelectAdapter;

    /**
     * 默认年
     */
    private int defaultYear;
    /**
     * 默认月
     */
    private int defaultMonth;
    /**
     * 默认日
     */
    private int defaultDay;

    /**
     * 确定按钮
     */
    private Button okBtn;
    /**
     * 取消按钮
     */
    private Button cancelBtn;

    /**
     * 年滚动条
     */
    private WheelView yearForSelectView;
    /**
     * 月滚动条
     */
    private WheelView monthForSelectView;
    /**
     * 日滚动条
     */
    private WheelView dayForSelectView;

    /**
     * 构造函数
     *
     * @param context                 上下文
     * @param defaultYear             默认年(确保正确，不做检查)
     * @param defaultMonth            默认月(确保正确，不做检查)
     * @param defaultDay              默认日(确保正确，不做检查)
     * @param onOKButtonClickListener 点击按钮时的回调
     */
    public SelectDateDialog(Context context, int defaultYear, int defaultMonth,
                            int defaultDay, OnButtonClickListener onOKButtonClickListener) {
        super(context, R.style.Dialog);
        initView();
        initData(defaultYear, defaultMonth, defaultDay);

        this.onButtonClickListener = onOKButtonClickListener;
        initSelectView();
        initListiner();
    }

    /**
     * 初始化界面与控件
     */
    private void initView() {
        setContentView(R.layout.view_selector_date_dialog);
        // 保证全屏宽，因为默认宽高为WRAP_CONTENT
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);

        getWindow().setWindowAnimations(R.style.DialogAnimation);
        findViewById(R.id.rl_main).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        SelectDateDialog.this.dismiss();
                    }
                });

        okBtn = (Button) findViewById(R.id.btn_ok);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        yearForSelectView = (WheelView) findViewById(R.id.year_list_scroll);
        monthForSelectView = (WheelView) findViewById(R.id.month_list_scroll);
        dayForSelectView = (WheelView) findViewById(R.id.day_list_scroll);
    }

    /**
     * 初始化数据
     *
     * @param defaultYear  默认年
     * @param defaultMonth 默认月
     * @param defaultDay   默认日
     */
    private void initData(int defaultYear, int defaultMonth, int defaultDay) {
        this.defaultYear = defaultYear;
        this.defaultMonth = defaultMonth;
        this.defaultDay = defaultDay;
    }

    /**
     * 显示的年范围，为默认年的+-100年
     */
    private final static int YEAR_RANAGE = 100;
    /**
     * 日期改变监听器
     */
    OnWheelChangedListener onDateChangedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (wheel.getId() == dayForSelectView.getId()) {
                dayForSelectAdapter.changeCurrentItem(newValue);
            } else if (wheel.getId() == yearForSelectView.getId()) {
                yearForSelectAdapter.changeCurrentItem(newValue);
                int curDay = updateDays(false, newValue - YEAR_RANAGE
                        + defaultYear, monthForSelectView.getCurrentItem());

                dayForSelectAdapter.changeCurrentItem(curDay);
            } else if (wheel.getId() == monthForSelectView.getId()) {
                monthForSelectAdapter.changeCurrentItem(newValue);
                int curDay = updateDays(false,
                        yearForSelectView.getCurrentItem() - YEAR_RANAGE
                                + defaultYear, newValue);
                dayForSelectAdapter.changeCurrentItem(curDay);
            }

        }
    };

    /**
     * 初始化选择列表控件
     */
    private void initSelectView() {
        // 年
        yearForSelectView.setWheelBackground(R.color.transparent);
        yearForSelectView.setWheelForeground(R.color.transparent);
        yearForSelectView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
        yearForSelectAdapter = new SelectListAdapter(getContext(), defaultYear
                - YEAR_RANAGE, defaultYear + YEAR_RANAGE, YEAR_RANAGE);
        yearForSelectView.setViewAdapter(yearForSelectAdapter);
        yearForSelectView.setCurrentItem(YEAR_RANAGE);
        yearForSelectView.addChangingListener(onDateChangedListener);

        // 月
        monthForSelectView.setWheelBackground(R.color.transparent);
        monthForSelectView.setWheelForeground(R.color.transparent);
        monthForSelectView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
        monthForSelectAdapter = new SelectListAdapter(getContext(), 1, 12,
                defaultMonth - 1);
        monthForSelectView.setViewAdapter(monthForSelectAdapter);
        monthForSelectView.setCurrentItem(defaultMonth - 1);
        monthForSelectView.addChangingListener(onDateChangedListener);
        monthForSelectView.setCyclic(true);

        // 日
        dayForSelectView.setWheelBackground(R.color.transparent);
        dayForSelectView.setWheelForeground(R.color.transparent);
        dayForSelectView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
        updateDays(true, defaultYear, defaultMonth - 1);
        dayForSelectView.addChangingListener(onDateChangedListener);
        dayForSelectView.setCyclic(true);
    }

    /**
     * 刷新日期
     */
    private int updateDays(boolean isInit, int year, int month) {
        Calendar calendar = Calendar.getInstance();

        // 解决系统bug，当前日期为30或31时2月可能显示30、31号
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int ocurrentDay = defaultDay;
        if (!isInit && dayForSelectView != null)
            ocurrentDay = dayForSelectView.getCurrentItem() + 1;

        int currentDay = ocurrentDay;
        if (currentDay > maxDays)
            currentDay = currentDay % maxDays - 1;
        else
            --currentDay;
        dayForSelectAdapter = new SelectListAdapter(getContext(), 1, maxDays,
                currentDay);
        dayForSelectView.setViewAdapter(dayForSelectAdapter);
        dayForSelectView.setCurrentItem(currentDay);

        return currentDay;
    }

    /**
     * 给控件加上事件
     */
    private void initListiner() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    SelectDateDialog.this.dismiss();
                    onButtonClickListener.onOKButtonClick(defaultYear
                                    - YEAR_RANAGE + yearForSelectView.getCurrentItem(),
                            monthForSelectView.getCurrentItem() + 1,
                            dayForSelectView.getCurrentItem() + 1);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    SelectDateDialog.this.dismiss();
                    onButtonClickListener.onClearButtonClick();
                }
            }
        });
    }

    /**
     * 列表适配器
     */
    private class SelectListAdapter extends NumericWheelAdapter {
        /**
         * 当前选中的项序号
         */
        private int currentItem;

        /**
         * 最小值
         */
        private int minValue;
        /**
         * 最大值
         */
        private int maxValue;

        /**
         * 构造函数
         *
         * @param context      上下文
         * @param minValue     the kankan.widget.wheel min value
         * @param maxValue     the kankan.widget.wheel max value
         * @param defaultOrder 默认的项序号
         */
        public SelectListAdapter(Context context, int minValue, int maxValue,
                                 int defaultOrder) {
            super(context, minValue, maxValue,
                    R.layout.item_selector, NO_RESOURCE);
            setItemTextResource(R.id.tv);
            this.currentItem = defaultOrder;
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        // @Override
        // protected void configureTextView(TextView view) {
        // super.configureTextView(view);
        // if (currentItem == currentValue) {
        // view.setTextColor(0xFF0000F0);
        // }
        // view.setTypeface(Typeface.SANS_SERIF);
        // }

        /**
         * 选中项改变时，修改数据显示颜色
         */
        public void changeCurrentItem(int newValue) {
            this.currentItem = newValue;
            notifyDataChangedEvent();
        }

        @Override
        public int getItemsCount() {
            return maxValue - minValue + 1;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            if (index != currentItem) {
                tv.setTextColor(getContext().getResources().getColor(
                        R.color.selector_dialog_text_not_selected));
            } else {
                tv.setTextColor(getContext().getResources().getColor(
                        R.color.selector_dialog_text_selected));
            }
            return view;
        }
    }

    /**
     * 设置选中年
     *
     * @param year 年，不做检查，确保正确
     */
    public void setSelectedYear(int year) {
        if (year - defaultYear > YEAR_RANAGE)
            yearForSelectView.setCurrentItem(defaultYear + YEAR_RANAGE);
        else if (defaultYear - year > YEAR_RANAGE)
            yearForSelectView.setCurrentItem(defaultYear - YEAR_RANAGE);
        else
            yearForSelectView.setCurrentItem(year - defaultYear + YEAR_RANAGE);
    }

    /**
     * 设置选中月
     *
     * @param month 月，不做检查，确保正确
     */
    public void setSelectedMonth(int month) {
        if (month > 12)
            monthForSelectView.setCurrentItem(11);
        else if (month < 1)
            monthForSelectView.setCurrentItem(0);
        else
            monthForSelectView.setCurrentItem(month - 1);
    }

    /**
     * 设置选中日
     *
     * @param day 日，不做检查，确保正确（如同时设置年月日，必须最后设置日）
     */
    public void setSelectedDay(int day) {
        Calendar calendar = Calendar.getInstance();

        // 解决系统bug，当前日期为30或31时2月可能显示30、31号
        calendar.clear();
        calendar.set(Calendar.YEAR, yearForSelectView.getCurrentItem()
                - YEAR_RANAGE + defaultYear);
        calendar.set(Calendar.MONTH, monthForSelectView.getCurrentItem());
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int currentDay = day;
        if (currentDay < 1)
            currentDay = 1;
        else if (currentDay > maxDays)
            currentDay = currentDay % maxDays - 1;
        else
            --currentDay;

        dayForSelectView.setCurrentItem(currentDay);
    }
}
