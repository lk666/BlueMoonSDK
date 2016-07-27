////package cn.com.bluemoon.lib.view.selectordialog;
////
////import android.app.DialogFragment;
////import android.content.Context;
////import android.os.Bundle;
////import android.support.annotation.Nullable;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.view.Window;
////import android.view.WindowManager;
////import android.widget.Button;
////import android.widget.DatePicker;
////import android.widget.TextView;
////
////import java.util.Calendar;
////
////import cn.com.bluemoon.lib.qrcode.R;
////import cn.com.bluemoon.lib.view.kankan.wheel.widget.OnWheelChangedListener;
////import cn.com.bluemoon.lib.view.kankan.wheel.widget.WheelView;
////import cn.com.bluemoon.lib.view.kankan.wheel.widget.adapters.NumericWheelAdapter;
////
/////**
//// * 滚轮日期选择弹窗
//// * Created by lk on 2016/7/20.
//// */
////public class SelectDateDialog extends DialogFragment {
////
////    /**
////     * 确定按钮
////     */
////    private Button okBtn;
////    /**
////     * 取消按钮
////     */
////    private Button cancleBtn;
////
////    /**
////     * 年滚动条
////     */
////    private WheelView yearForSelectView;
////    /**
////     * 月滚动条
////     */
////    private WheelView monthForSelectView;
////    /**
////     * 日滚动条
////     */
////    private WheelView dateForSelectView;
////
////    private int[] endDateSet;
////    private int[] startDateSet;
////    private int[] initDateSet;
////
////    /**
////     * 当前年月的开始日
////     */
////    private int curStartDate;
////
////    Calendar calendar = Calendar.getInstance();
////    /**
////     * 年列表适配器
////     */
////    SelectListAdapter yearForSelectAdapter;
////    /**
////     * 月列表适配器
////     */
////    SelectListAdapter monthForSelectAdapter;
////    /**
////     * 日列表适配器
////     */
////    SelectListAdapter dateForSelectAdapter;
////
////    /**
////     * 点击按钮时的回调
////     */
////    public OnButtonClickListener onButtonClickListener;
////
////    /**
////     * @param context                 上下文
////     * @param startTime               开始时间，确保正确，此处不检查
////     * @param endTime                 结束时间，确保正确，此处不检查
////     * @param currtntTime             当前时间，确保正确，此处不检查
////     * @param iSelectOptionsDialog 点击按钮时的回调
////     */
////    public SelectDateDialog(Context context, long startTime, long endTime, long currtntTime,
////                            OnButtonClickListener iSelectOptionsDialog) {
////        super();
////
////        initDate(startTime, endTime, currtntTime);
////        this.onButtonClickListener = iSelectOptionsDialog;
////    }
////
////    private void initDate(long startTime, long endTime, long currtntTime) {
////        if (startTime < 0) {
////            startTime = 0;
////        }
////        if (endTime < 0) {
////            endTime = 0;
////        }
////        if (startTime > endTime) {
////            startTime = endTime;
////        }
////
////        if (currtntTime < startTime) {
////            currtntTime = startTime;
////        }
////        if (currtntTime > endTime) {
////            currtntTime = endTime;
////        }
////
////        startDateSet = getDate(startTime);
////        endDateSet = getDate(endTime);
////        initDateSet = getDate(currtntTime);
////    }
////
////    /**
////     * 获取年月（月份1开始）日三元组
////     */
////    private int[] getDate(long time) {
////        // 解决系统bug，当前日期为30或31时2月可能显示30、31号
////        calendar.clear();
////        calendar.setTimeInMillis(time);
////        return new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get
////                (Calendar.DAY_OF_MONTH)};
////    }
////
////    @Nullable
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
////            savedInstanceState) {
////        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
////        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent2);
////
////        View view = inflater.inflate(R.layout.view_selector_date_dialog, container);
////        // 保证全屏宽，因为默认宽高为WRAP_CONTENT
////        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
////        lp.copyFrom(getDialog().getWindow().getAttributes());
////        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
////        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
////        getDialog().getWindow().setAttributes(lp);
////
////        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
////        view.setOnClickListener(
////                new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        SelectDateDialog.this.dismiss();
////                    }
////                });
////
////        okBtn = (Button) view.findViewById(R.id.btn_ok);
////        cancleBtn = (Button) view.findViewById(R.id.btn_cancel);
////        yearForSelectView = (WheelView) view.findViewById(R.id.year_list_scroll);
////        monthForSelectView = (WheelView) view.findViewById(R.id.month_list_scroll);
////        dateForSelectView = (WheelView) view.findViewById(R.id.day_list_scroll);
////
////        initSelectView();
////        initListiner();
////
////        return view;
////    }
////
////    @Override
////    public void onStart() {
////        super.onStart();
////    }
////
////    /**
////     * 初始化选择列表控件
////     */
////    private void initSelectView() {
////        // 年
////        yearForSelectView.setWheelBackground(R.color.transparent);
////        yearForSelectView.setWheelForeground(R.color.transparent);
////        yearForSelectView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
////        yearForSelectAdapter = new SelectListAdapter(getActivity(), startDateSet[0], endDateSet[0],
////                initDateSet[0] - startDateSet[0]);
////        yearForSelectView.setViewAdapter(yearForSelectAdapter);
////        yearForSelectView.setCurrentItem(initDateSet[0]);
////        yearForSelectView.addChangingListener(onDateChangedListener);
////
////        // 月
////        monthForSelectView.setWheelBackground(R.color.transparent);
////        monthForSelectView.setWheelForeground(R.color.transparent);
////        monthForSelectView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
////        monthForSelectAdapter = new SelectListAdapter(getActivity(), startDateSet[1], endDateSet[1],
////                initDateSet[1] - startDateSet[1]);
////        monthForSelectView.setViewAdapter(monthForSelectAdapter);
////        monthForSelectView.setCurrentItem(initDateSet[1]);
////        monthForSelectView.addChangingListener(onDateChangedListener);
////        monthForSelectView.setCyclic(true);
////
////        // 日
////        dateForSelectView.setWheelBackground(R.color.transparent);
////        dateForSelectView.setWheelForeground(R.color.transparent);
////        dateForSelectView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
////
////        curStartDate = 1;
////        if (startDateSet[0] == initDateSet[0] && startDateSet[1] == initDateSet[1]) {
////            curStartDate = startDateSet[2];
////        }
////
////        int endDate = getDaysOfMonth(initDateSet[0], initDateSet[1]);
////        if (endDateSet[0] == initDateSet[0] && endDateSet[1] == initDateSet[1]) {
////            endDate = endDateSet[2];
////        }
////
////        dateForSelectAdapter = new SelectListAdapter(getActivity(), curStartDate, endDate,
////                initDateSet[2] - curStartDate);
////        dateForSelectView.setViewAdapter(dateForSelectAdapter);
////        dateForSelectView.setCurrentItem(initDateSet[2]);
////
////        dateForSelectView.addChangingListener(onDateChangedListener);
////        dateForSelectView.setCyclic(true);
////    }
////
////    private int getDaysOfMonth(int year, int month) {
////        // 解决系统bug，当前日期为30或31时2月可能显示30、31号
////        calendar.clear();
////        calendar.set(Calendar.YEAR, year);
////        calendar.set(Calendar.MONTH, month);
////        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
////    }
////
////    /**
////     * 给控件加上事件
////     */
////    private void initListiner() {
////        okBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (onButtonClickListener != null) {
////                    SelectDateDialog.this.dismiss();
////                    onButtonClickListener.onOKButtonClick(startDateSet[0] + yearForSelectView
////                                    .getCurrentItem(),
////                            monthForSelectView.getCurrentItem() + startDateSet[1],
////                            dateForSelectView.getCurrentItem() + curStartDate);
////                }
////            }
////        });
////
////        cancleBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (onButtonClickListener != null) {
////                    SelectDateDialog.this.dismiss();
////                    onButtonClickListener.onCanclerButtonClick();
////                }
////            }
////        });
////    }
////
////
////
////
////    /**
////     * 日期改变监听器
////     */
////    OnWheelChangedListener onDateChangedListener = new OnWheelChangedListener() {
////        @Override
////        public void onChanged(WheelView wheel, int oldValue, int newValue) {
////            // 日
////            if (wheel.getId() == dateForSelectView.getId()) {
////                dateForSelectAdapter.setCurrentIndex(newValue);
////                calendar.clear();
////
////
////
////            }
////
////
////            // 年
////            else if (wheel.getId() == yearForSelectView.getId()) {
////                yearForSelectAdapter.setCurrentIndex(newValue);
////                int curDay = updateDays(false, newValue - YEAR_RANAGE
////                        + defaultYear, monthForSelectView.getCurrentItem());
////
////                dateForSelectAdapter.setCurrentIndex(curDay);
////            }
////
////            // 月
////            else if (wheel.getId() == monthForSelectView.getId()) {
////                monthForSelectAdapter.setCurrentIndex(newValue);
////                int curDay = updateDays(false, yearForSelectView.getCurrentItem() - YEAR_RANAGE
////                        + defaultYear, newValue);
////                dateForSelectAdapter.setCurrentIndex(curDay);
////            }
////
////        }
////    };
////
////    /**
////     * 刷新日期
////     */
////    private int updateDays(boolean isInit, int year, int month, int oriDate) {
////
////
////        int currentDay = ocurrentDay;
////        if (currentDay > maxDays)
////            currentDay = currentDay % maxDays - 1;
////        else
////            --currentDay;
////        dayForSelectAdapter = new SelectListAdapter(getContext(), 1, maxDays,
////                currentDay);
////        dayForSelectView.setViewAdapter(dayForSelectAdapter);
////        dayForSelectView.setCurrentItem(currentDay);
////
////        return currentDay;
////    }
////
////
////
////
////
////
////
////    @Override
////    public void onDestroyView() {
////        super.onDestroyView();
////        if (dateForSelectView != null) {
////            dateForSelectView.removeChangingListener(onDateChangedListener);
////        }
////        if (yearForSelectView != null) {
////            yearForSelectView.removeChangingListener(onDateChangedListener);
////        }
////        if (monthForSelectView != null) {
////            monthForSelectView.removeChangingListener(onDateChangedListener);
////        }
////    }
////
////    /**
////     * 点击按钮时的回调接口
////     */
////    public interface OnButtonClickListener {
////        /**
////         * 点击确定时的回调，此方法能在初始化对话框定义事件时直接使用选择的日期字符串
////         *
////         * @param year  年
////         * @param month 月
////         * @param date  日
////         */
////        void onOKButtonClick(int year, int month, int date);
////
////        /**
////         * 点击取消按钮的回调
////         */
////        void onCanclerButtonClick();
////    }
////
////    /**
////     * 列表适配器
////     */
////    private class SelectListAdapter extends NumericWheelAdapter {
////        /**
////         * 当前选中的项序号
////         */
////        private int currentIndex;
////
////        private int count;
////
////        /**
////         * 构造函数
////         *
////         * @param context      上下文
////         * @param minValue     the kankan.widget.wheel min value
////         * @param maxValue     the kankan.widget.wheel max value
////         * @param defaultOrder 默认的项序号
////         */
////        public SelectListAdapter(Context context, int minValue, int maxValue, int defaultOrder) {
////            // TODO: lk 2016/7/20 试试纯textview
////            super(context, minValue, maxValue, R.layout.item_selector, NO_RESOURCE);
////            setItemTextResource(R.id.tv);
////            this.currentIndex = defaultOrder;
////            this.count = maxValue - minValue + 1;
////        }
////
//////         @Override
//////         protected void configureTextView(TextView view) {
//////         super.configureTextView(view);
//////         if (currentIndex == currentValue) {
//////         view.setTextColor(0xFF0000F0);
//////         }
//////         view.setTypeface(Typeface.SANS_SERIF);
//////         }
////
////        /**
////         * 选中项改变时，修改数据显示颜色
////         */
////        public void setCurrentIndex(int newValue) {
////            this.currentIndex = newValue;
////            notifyDataChangedEvent();
////        }
////
////        @Override
////        public int getItemsCount() {
////            return count;
////        }
////
////        @Override
////        public View getItem(int index, View cachedView, ViewGroup parent) {
////            View view = super.getItem(index, cachedView, parent);
////            TextView tv = (TextView) view.findViewById(R.id.tv);
////            if (index != currentIndex) {
////                tv.setTextColor(getResources().getColor(R.color.selector_dialog_text_not_selected));
////            } else {
////                tv.setTextColor(getResources().getColor(R.color.selector_dialog_text_selected));
////            }
////            return view;
////        }
////    }
////}
//
//
//
//package cn.com.bluemoon.lib.view.selectordialog;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.TextView;
//import java.util.Calendar;
//
//import cn.com.bluemoon.lib.qrcode.R;
//import cn.com.bluemoon.lib.view.kankan.wheel.widget.OnWheelChangedListener;
//import cn.com.bluemoon.lib.view.kankan.wheel.widget.WheelView;
//import cn.com.bluemoon.lib.view.kankan.wheel.widget.adapters.NumericWheelAdapter;
//
///**
// * 滚轮日期选择弹窗
// *
// * @author LK
// */
//public class SelectDatesDialog extends Dialog {
//
//    /**
//     * 点击按钮时的回调
//     */
//    public OnButtonClickListener onButtonClickListener;
//
//    /**
//     * 点击按钮时的回调接口
//     */
//    public interface OnButtonClickListener {
//        /**
//         * 点击确定时的回调，此方法能在初始化对话框定义事件时直接使用选择的日期字符串
//         *
//         * @param year  年
//         * @param month 月
//         * @param day   日
//         */
//        void onOKButtonClick(int year, int month, int day);
//
//        /**
//         * 点击清除按钮的回调
//         */
//        void onClearButtonClick();
//    }
//
//    /**
//     * 年列表适配器
//     */
//    SelectListAdapter yearForSelectAdapter;
//    /**
//     * 月列表适配器
//     */
//    SelectListAdapter monthForSelectAdapter;
//    /**
//     * 日列表适配器
//     */
//    SelectListAdapter dayForSelectAdapter;
//
//    /**
//     * 当前时间
//     */
//    private Calendar curTime;
//
//    /**
//     * 开始时间
//     */
//    private Calendar startTime;
//
//    /**
//     * 结束时间
//     */
//    private Calendar endTime;
//
//    private Calendar tempDate;
//
//    /**
//     * 确定按钮
//     */
//    private Button okBtn;
//    /**
//     * 取消按钮
//     */
//    private Button cancelBtn;
//
//    /**
//     * 年滚动条
//     */
//    private WheelView yearForSelectView;
//    /**
//     * 月滚动条
//     */
//    private WheelView monthForSelectView;
//    /**
//     * 日滚动条
//     */
//    private WheelView dayForSelectView;
//
//    /**
//     * 构造函数
//     *
//     * @param context                 上下文
//     * @param defaultTIme 默认时间戳(确保正确，不做检查)
//     * @param iSelectOptionsDialog 点击按钮时的回调
//     */
//    public SelectDatesDialog(Context context, long defaultTIme,
//                             OnButtonClickListener iSelectOptionsDialog) {
//        super(context, R.style.Dialog);
//
//        this.curTime = Calendar.getInstance();
//        this.curTime.clear();
//        this.curTime.setTimeInMillis(defaultTIme);
//
//        tempDate = Calendar.getInstance();
//
//        // TODO: lk 2016/7/23 最大、最小时间
//        this.startTime = Calendar.getInstance();
//        this.startTime.clear();
//        this.startTime.setTimeInMillis(0);
//
//        this.endTime = Calendar.getInstance();
//        this.endTime.clear();
//        this.endTime.setTimeInMillis(defaultTIme);
//        this.endTime.add(Calendar.YEAR, 100);
//
//        this.onButtonClickListener = iSelectOptionsDialog;
//
//        initView();
//        initSelectView();
//        initListiner();
//    }
//
//    /**
//     * 初始化界面与控件
//     */
//    private void initView() {
//        setContentView(R.layout.view_selector_date_dialog);
//        // 保证全屏宽，因为默认宽高为WRAP_CONTENT
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        getWindow().setAttributes(lp);
//
//        getWindow().setWindowAnimations(R.style.DialogAnimation);
//        findViewById(R.id.rl_main).setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SelectDatesDialog.this.dismiss();
//                    }
//                });
//
//        okBtn = (Button) findViewById(R.id.btn_ok);
//        cancelBtn = (Button) findViewById(R.id.btn_cancel);
//        yearForSelectView = (WheelView) findViewById(R.id.year_list_scroll);
//        monthForSelectView = (WheelView) findViewById(R.id.month_list_scroll);
//        dayForSelectView = (WheelView) findViewById(R.id.day_list_scroll);
//    }
//
//    /**
//     * 日期改变监听器
//     */
//    OnWheelChangedListener onDateChangedListener = new OnWheelChangedListener() {
//        @Override
//        public void onChanged(WheelView wheel, int oldVal, int newVal) {
//            tempDate.clear();
//            tempDate.setTimeInMillis(curTime.getTimeInMillis());
//            // take care of wrapping of days and months to update greater fields
//            if (wheel== dayForSelectView) {
//                int maxDayOfMonth = tempDate.getActualMaximum(Calendar.DAY_OF_MONTH);
//                if (oldVal == maxDayOfMonth && newVal == 1) {
//                    tempDate.add(Calendar.DAY_OF_MONTH, 1);
//                } else if (oldVal == 1 && newVal == maxDayOfMonth) {
//                    tempDate.add(Calendar.DAY_OF_MONTH, -1);
//                } else {
//                    tempDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
//                }
//            } else if (wheel == monthForSelectView) {
//                if (oldVal == 11 && newVal == 0) {
//                    tempDate.add(Calendar.MONTH, 1);
//                } else if (oldVal == 0 && newVal == 11) {
//                    tempDate.add(Calendar.MONTH, -1);
//                } else {
//                    tempDate.add(Calendar.MONTH, newVal - oldVal);
//                }
//            }  else if (wheel== yearForSelectView) {
//                tempDate.set(Calendar.YEAR, newVal);
//            } else {
//                throw new IllegalArgumentException();
//            }
//
//            if (tempDate.before(startTime)) {
//                tempDate.setTimeInMillis(startTime.getTimeInMillis());
//            } else if (tempDate.after(endTime)) {
//                tempDate.setTimeInMillis(endTime.getTimeInMillis());
//            }
//
//            setCurrentView(tempDate);
//        }
//    };
//
//    /**
//     * 设置时间
//     */
//    private void setCurrentView(Calendar tempDate) {
//        if (tempDate.get(Calendar.YEAR) != curTime.get(Calendar.YEAR)) {
//            int index =   tempDate.get(Calendar.YEAR) - startTime.get(Calendar.YEAR);
//            yearForSelectAdapter.changeCurrentItem(index);
//            yearForSelectView.setCurrentItem(index);
//        }
//        if (tempDate.get(Calendar.MONTH) != curTime.get(Calendar.MONTH)) {
//            int index =   tempDate.get(Calendar.MONTH);
//            monthForSelectAdapter.changeCurrentItem(index);
//            monthForSelectView.setCurrentItem(index);
//        }
//
//        if (tempDate.getActualMaximum(Calendar.DAY_OF_MONTH)
//                != curTime.getActualMaximum(Calendar.DAY_OF_MONTH)) {
//            dayForSelectAdapter = new SelectListAdapter(getContext(), 1,
//                    tempDate.getActualMaximum(Calendar.DAY_OF_MONTH),
//                    tempDate.get(Calendar.DAY_OF_MONTH) - 1);
//        }
//
//        curTime.setTimeInMillis(tempDate.getTimeInMillis());
//
//        if (tempDate.get(Calendar.MONTH) != curTime.get(Calendar.MONTH)) {
//            int index =   tempDate.get(Calendar.MONTH) - startTime.get(Calendar.MONTH);
//            monthForSelectAdapter.changeCurrentItem(index);
//            monthForSelectView.setCurrentItem(index);
//        }
//    }
//
//    /**
//     * 初始化选择列表控件
//     */
//    private void initSelectView() {
//        // 年
//        yearForSelectView.setWheelBackground(R.color.transparent);
//        yearForSelectView.setWheelForeground(R.color.transparent);
//        yearForSelectView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
//        yearForSelectAdapter = new SelectListAdapter(getContext(),
//                startTime.get(Calendar.YEAR),
//                endTime.get(Calendar.YEAR),
//                curTime.get(Calendar.YEAR) - startTime.get(Calendar.YEAR));
//        yearForSelectView.setViewAdapter(yearForSelectAdapter);
//        yearForSelectView.setCurrentItem(curTime.get(Calendar.YEAR) - startTime.get(Calendar.YEAR));
//        yearForSelectView.addChangingListener(onDateChangedListener);
//
//        // 月
//        monthForSelectView.setWheelBackground(R.color.transparent);
//        monthForSelectView.setWheelForeground(R.color.transparent);
//        monthForSelectView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
//        monthForSelectAdapter = new SelectListAdapter(getContext(), 1, 12,
//                curTime.get(Calendar.MONTH));
//        monthForSelectView.setViewAdapter(monthForSelectAdapter);
//        monthForSelectView.setCurrentItem(curTime.get(Calendar.MONTH));
//        monthForSelectView.addChangingListener(onDateChangedListener);
//        monthForSelectView.setCyclic(true);
//
//        // 日
//        dayForSelectView.setWheelBackground(R.color.transparent);
//        dayForSelectView.setWheelForeground(R.color.transparent);
//        dayForSelectView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
//        dayForSelectAdapter = new SelectListAdapter(getContext(), 1,
//                curTime.getActualMaximum(Calendar.DAY_OF_MONTH),
//                curTime.get(Calendar.DAY_OF_MONTH) - 1);
//        dayForSelectView.setViewAdapter(dayForSelectAdapter);
//        dayForSelectView.setCurrentItem( curTime.get(Calendar.DAY_OF_MONTH) - 1);
//        dayForSelectView.addChangingListener(onDateChangedListener);
//        dayForSelectView.setCyclic(true);
//    }
//
//    /**
//     * 刷新日期
//     */
//    private int updateDays(boolean isInit, int year, int month) {
//        Calendar calendar = Calendar.getInstance();
//
//        // 解决系统bug，当前日期为30或31时2月可能显示30、31号
//        calendar.clear();
//        calendar.set(Calendar.YEAR, year);
//        calendar.set(Calendar.MONTH, month);
//        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//
//        int ocurrentDay = defaultDay;
//        if (!isInit && dayForSelectView != null)
//            ocurrentDay = dayForSelectView.getCurrentItem() + 1;
//
//        int currentDay = ocurrentDay;
//        if (currentDay > maxDays)
//            currentDay = currentDay % maxDays - 1;
//        else
//            --currentDay;
//        dayForSelectAdapter = new SelectListAdapter(getContext(), 1, maxDays,
//                currentDay);
//        dayForSelectView.setViewAdapter(dayForSelectAdapter);
//        dayForSelectView.setCurrentItem(currentDay);
//
//        return currentDay;
//    }
//
//    /**
//     * 给控件加上事件
//     */
//    private void initListiner() {
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onButtonClickListener != null) {
//                    SelectDatesDialog.this.dismiss();
//                    onButtonClickListener.onOKButtonClick(
//                            curTime.get(Calendar.YEAR),
//                            curTime.get(Calendar.MONTH) + 1,
//                            curTime.get(Calendar.DAY_OF_MONTH));
//                }
//            }
//        });
//
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onButtonClickListener != null) {
//                    SelectDatesDialog.this.dismiss();
//                    onButtonClickListener.onClearButtonClick();
//                }
//            }
//        });
//    }
//
//    /**
//     * 列表适配器
//     */
//    private class SelectListAdapter extends NumericWheelAdapter {
//        /**
//         * 当前选中的项序号
//         */
//        private int currentItem;
//
//        /**
//         * 最小值
//         */
//        private int minValue;
//        /**
//         * 最大值
//         */
//        private int maxValue;
//
//        /**
//         * 构造函数
//         *
//         * @param context      上下文
//         * @param minValue     the kankan.widget.wheel min value
//         * @param maxValue     the kankan.widget.wheel max value
//         * @param defaultOrder 默认的项序号
//         */
//        public SelectListAdapter(Context context, int minValue, int maxValue,
//                                 int defaultOrder) {
//            super(context, minValue, maxValue,
//                    R.layout.item_selector, NO_RESOURCE);
//            setItemTextResource(R.id.tv);
//            this.currentItem = defaultOrder;
//            this.minValue = minValue;
//            this.maxValue = maxValue;
//        }
//
//        /**
//         * 选中项改变时，修改数据显示颜色
//         */
//        public void changeCurrentItem(int newValue) {
//            this.currentItem = newValue;
//            notifyDataChangedEvent();
//        }
//
//        @Override
//        public int getItemsCount() {
//            return maxValue - minValue + 1;
//        }
//
//        @Override
//        public View getItem(int index, View cachedView, ViewGroup parent) {
//            View view = super.getItem(index, cachedView, parent);
//            TextView tv = (TextView) view.findViewById(R.id.tv);
//            if (index != currentItem) {
//                tv.setTextColor(getContext().getResources().getColor(
//                        R.color.selector_dialog_text_not_selected));
//            } else {
//                tv.setTextColor(getContext().getResources().getColor(
//                        R.color.selector_dialog_text_selected));
//            }
//            return view;
//        }
//    }
//}
