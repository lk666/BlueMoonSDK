package cn.com.bluemoon.lib.view.selectordialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.view.kankan.wheel.widget.OnWheelChangedListener;
import cn.com.bluemoon.lib.view.kankan.wheel.widget.WheelView;
import cn.com.bluemoon.lib.view.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

//例子：
//class TextArea extends Area implements ISecectedItem {
//
//    @Override public String getShowText() {
//        return getDcode() + "-" + getDname();
//    }
//}
//
//SelectOptionsDialog s;
//        List<TextArea>[] iList = new ArrayList[4];
//        for (int i = 0; i < 4; i++) {
//        List<TextArea> l = new ArrayList<>();
//        for (int j = 0; j < 2 * i + 1; j++) {
//        TextArea ja = new TextArea();
//        ja.setDcode(i + "" + j);
//        ja.setDname(j + "J");
//        l.add(ja);
//        }
//        iList[i] = l;
//        }
//
//        s = new SelectOptionsDialog(this,5, iList, new int[]{0,1,2,3}, new
//        OnOKButtonClickListener() {
//@Override public void onOKButtonClick(List<ISecectedItem>
//selectedObj) {
//        if (selectedObj !=null) {
//        String str = "";
//        for (ISecectedItem obj : selectedObj) {
//        TextArea a = (TextArea) obj;
//        str += a.getDname();
//        }
//        PublicUtil.showToast("返回：" + str);
//        }
//        }
//@Override public void onClearButtonClick() {
//        PublicUtil.showToast("点击取消");
//        }
//        });
//
//        s.show();

//// TODO: lk 2016/7/22 使用dialogfragment

/**
 * 多层条件选择弹窗（各选项独立，不级联）
 *
 * @author Luokai
 */
public class SelectOptionsDialog extends Dialog {
    /**
     * 确定按钮
     */
    private Button okBtn;
    /**
     * 取消按钮
     */
    private Button cancelBtn;

    /**
     * 滚动区域
     */
    private LinearLayout llScroll;

    /**
     * 点击确定时的回调
     */
    public OnOKButtonClickListener onOKButtonClickListener;

    /**
     * 列表
     */
    private List<SelectedItem> lists;

    private int row;

    private class SelectedItem {
        public int selectedIndex;
        public List<ISecectedItem> lists;
    }

    /**
     * 构造函数
     *
     * @param context                 上下文
     * @param row                     每个可选滚轮（层级）有多少行，奇数
     * @param onOKButtonClickListener 点击确定时的回调
     * @param lists                   从左到右的列表
     * @param defaultSelectedIndexs   从左到右的列表默认选择项
     */
    public SelectOptionsDialog(Context context, int row,
                               List[] lists, int[] defaultSelectedIndexs,
                               OnOKButtonClickListener onOKButtonClickListener) {
        super(context, R.style.Dialog);

        this.row = row % 2 == 0 ? row - 1 : row;
        this.onOKButtonClickListener = onOKButtonClickListener;

        this.lists = new ArrayList<>();
        if (lists != null && defaultSelectedIndexs != null
                && lists.length == defaultSelectedIndexs.length) {
            for (int i = 0; i < lists.length; i++) {
                List<ISecectedItem> list = lists[i];
                SelectedItem node = new SelectedItem();
                node.lists = list;
                node.selectedIndex = defaultSelectedIndexs[i];
                this.lists.add(node);
            }
        }
        initView();
    }

    /**
     * 初始化界面与控件
     */
    private void initView() {
        setContentView(R.layout.dialog_select_option);
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
                        SelectOptionsDialog.this.dismiss();
                    }
                });
        okBtn = (Button) findViewById(R.id.btn_ok);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        llScroll = (LinearLayout) findViewById(R.id.ll_scroll);

        int height = row * getContext().getResources().getDimensionPixelSize(R.dimen
                .selector_item_height);
        LinearLayout.LayoutParams llLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, height);
        llScroll.setLayoutParams(llLp);

        initSelectView();
        initListiner();
    }

    /**
     * 初始化选择列表控件
     */
    private void initSelectView() {
        if (lists == null || lists.isEmpty()) {
            return;
        }

        for (SelectedItem item : lists) {
            WheelView wheelView = new WheelView(getContext());
            LinearLayout.LayoutParams wheelLp = new LinearLayout.LayoutParams(0, ViewGroup
                    .LayoutParams.MATCH_PARENT);
            wheelLp.weight = 1;
            wheelView.setLayoutParams(wheelLp);

            wheelView.setWheelBackground(R.color.transparent);
            wheelView.setWheelForeground(R.color.transparent);
            wheelView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
            wheelView.addChangingListener(onSelectedChangedListener);

            SelectListAdapter selectAdapter = new SelectListAdapter(getContext(), item);
            wheelView.setViewAdapter(selectAdapter);
            wheelView.setCurrentItem(item.selectedIndex);

            llScroll.addView(wheelView);
        }
    }

    /**
     * 选项改变监听器
     */
    private OnWheelChangedListener onSelectedChangedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            SelectListAdapter adapter = (SelectListAdapter) wheel.getViewAdapter();
            adapter.changeCurrentItem(newValue);
        }
    };

    /**
     * 给控件加上事件
     */
    private void initListiner() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOKButtonClickListener != null) {
                    SelectOptionsDialog.this.dismiss();

                    List<ISecectedItem> result = new ArrayList<>();

                    for (SelectedItem item : lists) {
                        result.add(item.lists.get(item.selectedIndex));
                    }

                    onOKButtonClickListener.onOKButtonClick(result);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOKButtonClickListener != null) {
                    SelectOptionsDialog.this.dismiss();
                    onOKButtonClickListener.onClearButtonClick();
                }
            }
        });
    }

    /**
     * 列表适配器
     */
    private class SelectListAdapter extends AbstractWheelTextAdapter {
        /**
         * 数据
         */
        private SelectedItem item;

        /**
         * 构造函数
         *
         * @param context 上下文
         * @param item    列表数据
         */
        public SelectListAdapter(Context context, SelectedItem item) {
            super(context, R.layout.item_selector, NO_RESOURCE);

            setItemTextResource(R.id.tv);
            this.item = item;
        }

        /**
         * 选中项改变时的回调，修改数据显示
         */
        public void changeCurrentItem(int newValue) {
            if (item != null) {
                item.selectedIndex = newValue;
            }
            notifyDataChangedEvent();
        }

        @Override
        public int getItemsCount() {
            return item != null ? item.lists.size() : 0;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return item != null ? (index >= item.lists.size() || index < 0 ?
                    null : item.lists.get(index).getShowText())
                    : null;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            if (index != item.selectedIndex) {
                tv.setTextColor(getContext().getResources().getColor(
                        R.color.selector_dialog_text_not_selected));
            } else {
                tv.setTextColor(getContext().getResources().getColor(
                        R.color.selector_dialog_text_selected));
            }
            return view;
        }
    }
}
