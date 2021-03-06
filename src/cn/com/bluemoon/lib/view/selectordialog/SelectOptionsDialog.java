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
//    @Override
//    public String getShowText() {
//        return getDcode() + "-" + getDname();
//    }
//}
//SelectOptionsDialog s;

//    public void onClick() {
//
//        if (s == null) {
//            List<TextArea>[] iList = new ArrayList[3];
//            for (int i = 0; i < 3; i++) {
//                List<TextArea> l = new ArrayList<>();
//                for (int j = 0; j < 6; j++) {
//                    TextArea ja = new TextArea();
//                    ja.setDcode(i + "" + j);
//                    ja.setDname(j + "J");
//                    l.add(ja);
//                }
//                iList[i] = l;
//            }
//
//            s = new SelectOptionsDialog(this, 5, iList, new int[]{0, 1, 2}, new
//                    SelectOptionsDialog.ISelectOptionsDialog() {
//                        @Override
//                        public void OnSelectedChanged(List<ISecectedItem> selectedObj) {
//                            if (selectedObj != null) {
//                                String str = "";
//                                for (ISecectedItem obj : selectedObj) {
//                                    TextArea a = (TextArea) obj;
//                                    str += a.getDname();
//                                }
//                                PublicUtil.showToast("OnSelectedChanged：" + str);
//                            }
//                        }
//
//                        @Override
//                        public void onOutsideClick() {
//                            PublicUtil.showToast("点击外部区域");
//                        }
//
//                        @Override
//                        public void onOKButtonClick(List<ISecectedItem>
//                                                            selectedObj) {
//                            if (selectedObj != null) {
//                                String str = "";
//                                for (ISecectedItem obj : selectedObj) {
//                                    TextArea a = (TextArea) obj;
//                                    str += a.getDname();
//                                }
//                                PublicUtil.showToast("点击确定：" + str);
//                            }
//                        }
//
//                        @Override
//                        public void onClearButtonClick() {
//                            PublicUtil.showToast("点击取消");
//                        }
//                    });
//        } else {
//            //s.setCurrentSelectedIndex(new int[]{0, 2, 4, 0});
//            List<TextArea>[] ist = new ArrayList[4];
//            for (int i = 0; i < 4; i++) {
//                List<TextArea> l = new ArrayList<>();
//                for (int j = 0; j <  3; j++) {
//                    TextArea ja = new TextArea();
//                    ja.setDcode(i + "" + j);
//                    ja.setDname(j + "J");
//                    l.add(ja);
//                }
//                ist[i] = l;
//            }
//            s.setData(ist, new int[]{1, 50,3,4,5});
//        }
//
//        s.show();
//}


//// TODO: lk 2016/7/22 使用dialogfragment

/**
 * 多层条件选择弹窗（各选项独立，不级联）
 *
 * @author Luokai
 */
public class SelectOptionsDialog extends Dialog {
    public interface ISelectOptionsDialog extends OnDialogBtnClickListener {
        /**
         * 滑轮滚动时的监听
         *
         * @param selectedObj 选择项（从第一级开始）
         */
        void OnSelectedChanged(List<ISecectedItem> selectedObj);

        /**
         * 点击非选中区域，已dismiss掉dialog
         */
        void onOutsideClick();
    }

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
     * 回调
     */
    public ISelectOptionsDialog iSelectOptionsDialog;

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
     * @param context               上下文
     * @param row                   每个可选滚轮（层级）有多少行，奇数
     * @param iSelectOptionsDialog  回调
     * @param lists                 从左到右的列表
     * @param defaultSelectedIndexs 从左到右的列表默认选择项
     */
    public SelectOptionsDialog(Context context, int row, List[] lists, int[] defaultSelectedIndexs,
                               ISelectOptionsDialog iSelectOptionsDialog) {
        super(context, R.style.Dialog);

        this.row = row % 2 == 0 ? row - 1 : row;
        this.iSelectOptionsDialog = iSelectOptionsDialog;

        this.lists = new ArrayList<>();

        if (lists != null) {
            List<Integer> selected = getFormatInddexList(lists, defaultSelectedIndexs);
            this.lists.clear();

            for (int i = 0; i < lists.length; i++) {
                List<ISecectedItem> list = lists[i];
                SelectedItem node = new SelectedItem();
                node.lists = list;
                node.selectedIndex = selected.get(i);
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
                        if (iSelectOptionsDialog != null) {
                            SelectOptionsDialog.this.dismiss();
                            iSelectOptionsDialog.onOutsideClick();
                        }
                    }
                });

        findViewById(R.id.ll_content).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 避免点击内容空白处也dismiss
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
        llScroll.removeAllViews();
        for (SelectedItem item : lists) {
            addNewWheelView(item);
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
            if (iSelectOptionsDialog != null) {
                List<ISecectedItem> result = new ArrayList<>();

                for (SelectedItem item : lists) {
                    result.add(item.lists.get(item.selectedIndex));
                }

                iSelectOptionsDialog.OnSelectedChanged(result);
            }
        }
    };

    /**
     * 给控件加上事件
     */
    private void initListiner() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iSelectOptionsDialog != null) {
                    SelectOptionsDialog.this.dismiss();

                    List<ISecectedItem> result = new ArrayList<>();

                    for (SelectedItem item : lists) {
                        result.add(item.lists.get(item.selectedIndex));
                    }

                    iSelectOptionsDialog.onOKButtonClick(result);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iSelectOptionsDialog != null) {
                    SelectOptionsDialog.this.dismiss();
                    iSelectOptionsDialog.onClearButtonClick();
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

        /**
         * 设置数据
         */
        public void setData(SelectedItem item) {
            this.item = item;
            notifyDataChangedEvent();
        }


        @Override
        public int getItemsCount() {
            return item != null ? item.lists.size() : 0;
        }

        @Override
        protected CharSequence getItemText(int index) {
            String s = item != null ? (index >= item.lists.size() || index < 0 ?
                    null : item.lists.get(index).getShowText()) : null;
            if (s.startsWith("3")) {
                s += " ";
            }
            return s;
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

    /**
     * 设置选中项
     *
     * @param selectedIndexs 从左到右的列表默认选择项
     */
    public void setCurrentSelectedIndex(int[] selectedIndexs) {
        if (lists != null && selectedIndexs != null && lists.size() == selectedIndexs.length) {
            for (int i = 0; i < selectedIndexs.length; i++) {
                SelectedItem node = lists.get(i);
                int selected = selectedIndexs[i];
                selected = selected < 0 ? 0 :
                        (selected >= node.lists.size() ? node.lists.size() - 1 : selected);
                if (selected != node.selectedIndex) {
                    ((WheelView) llScroll.getChildAt(i)).setCurrentItem(selected);
                }
            }
        }
    }

    /**
     * 设置数据
     *
     * @param lists           从左到右的列表
     * @param selectedIndexes 从左到右的列表默认选择项
     */
    public void setData(List[] lists, int[] selectedIndexes) {
        if (lists == null) {
            llScroll.removeAllViews();
            return;
        }
        List<Integer> selected = getFormatInddexList(lists, selectedIndexes);
        this.lists.clear();

        for (int i = 0; i < lists.length; i++) {
            List<ISecectedItem> list = lists[i];
            SelectedItem node = new SelectedItem();
            node.lists = list;
            node.selectedIndex = selected.get(i);
            this.lists.add(node);
        }

        reSetSelectView();
    }

    private ArrayList<Integer> getFormatInddexList(List[] lists, int[] selectedIndexes) {
        ArrayList<Integer> l = new ArrayList<>();
        int listsLength = lists.length;
        int selectedIndexesLength = selectedIndexes == null ? 0 : selectedIndexes.length;

        // 忽略多出的部分
        if (selectedIndexesLength > listsLength) {
            for (int k = 0; k < listsLength; k++) {
                int selectedIndexValue = selectedIndexes[k];
                if (selectedIndexValue < 0) {
                    selectedIndexValue = 0;
                } else if (selectedIndexValue > lists[k].size() - 1) {
                    selectedIndexValue = lists[k].size() - 1;
                }
                l.add(selectedIndexValue);
            }
        }
        // 不足的部分默认为0
        else {
            int k = 0;
            for (; k < selectedIndexesLength; k++) {
                int selectedIndexValue = selectedIndexes[k];
                if (selectedIndexValue < 0) {
                    selectedIndexValue = 0;
                } else if (selectedIndexValue > lists[k].size() - 1) {
                    selectedIndexValue = lists[k].size() - 1;
                }
                l.add(selectedIndexValue);
            }

            // 默认选中为0
            while (k < listsLength) {
                l.add(0);
                k++;
            }
        }

        return l;
    }

    /**
     * 重置选择列表控件
     */
    private void reSetSelectView() {
        if (lists == null || lists.isEmpty()) {
            return;
        }

        int llChildCount = llScroll.getChildCount();
        int size = lists.size();

        // 新增新的数据滚轮
        if (size > llChildCount) {
            for (int i = llChildCount; i < size; i++) {
                addNewWheelView(lists.get(i - 1));
            }
        }

        // 删除多余的滚轮
        while (size < llChildCount) {
            llScroll.removeViewAt(size);
            llChildCount--;
        }

        // 复用旧滚轮
        for (int i = 0; i < llChildCount; i++) {
            SelectedItem item = lists.get(i);
            WheelView wheelView = (WheelView) llScroll.getChildAt(i);
            SelectListAdapter adapter = (SelectListAdapter) wheelView.getViewAdapter();
            adapter.setData(item);
            // 必须得重新设置，因为WheelView的notify并不能正确清除原来的全部数据
            wheelView.setViewAdapter(adapter);
            wheelView.setCurrentItem(item.selectedIndex);
        }
    }

    private void addNewWheelView(SelectedItem item) {
        WheelView wheelView = new WheelView(getContext());
        LinearLayout.LayoutParams wheelLp = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        wheelLp.weight = 1;
        wheelView.setLayoutParams(wheelLp);

        wheelView.setWheelBackground(R.color.transparent);
        wheelView.setWheelForeground(R.color.transparent);
        wheelView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
        wheelView.addChangingListener(onSelectedChangedListener);
        wheelView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        SelectListAdapter selectAdapter = new SelectListAdapter(getContext(), item);
        wheelView.setViewAdapter(selectAdapter);
        wheelView.setCurrentItem(item.selectedIndex);

        llScroll.addView(wheelView);
    }
}
