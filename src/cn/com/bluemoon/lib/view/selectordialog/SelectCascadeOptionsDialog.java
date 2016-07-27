package cn.com.bluemoon.lib.view.selectordialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
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

// 例子：
//SelectCascadeOptionsDialog s;
//class TextArea extends Area implements ISecectedItem {
//
//    @Override public String getShowText() {
//        return getDcode() + "-" + getDname();
//    }
//}
//if (s == null) {
//        SelectTreeNode<TextArea> root = new SelectTreeNode<>();
//        root.setSelectedChildIndex(2);
//        List<SelectTreeNode<TextArea>> iList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//        TextArea ia = new TextArea();
//        ia.setDcode(i + "");
//        ia.setDname(i + "I");
//        SelectTreeNode<TextArea> is = new SelectTreeNode<>();
//        is.setObj(ia);
//        List<SelectTreeNode<TextArea>> jList = new ArrayList<>();
//        for (int j = 0; j < 7; j++) {
//        TextArea ja = new TextArea();
//        ja.setDcode(i + "" + j);
//        ja.setDname(j + "J");
//        SelectTreeNode<TextArea> js = new SelectTreeNode<>();
//        js.setObj(ja);
//        List<SelectTreeNode<TextArea>> kList = new ArrayList<>();
//        for (int k = 0; k < 8; k++) {
//        TextArea ka = new TextArea();
//        ka.setDcode(i + "" + j + "" + k);
//        ka.setDname(k + "K");
//        SelectTreeNode<TextArea> ks = new SelectTreeNode<>();
//        ks.setObj(ka);
//        kList.add(ks);
//        }
//        js.setChildList(kList);
//        jList.add(js);
//        }
//        if (i != 3) {
//        is.setChildList(jList);
//        }
//        iList.add(is);
//        }
//        root.setChildList(iList);

//        s = new SelectCascadeOptionsDialog(this, root, 3, 5, new SelectCascadeOptionsDialog
//        .OnDialogBtnClickListener() {
//@Override public void onOKButtonClick(List<ISecectedItem>
//        selectedObj) {
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
//        }
//        s.show();

/**
 * 多层级递进级联条件选择弹窗（如省市区）
 *
 * @author Luokai
 */
public class SelectCascadeOptionsDialog extends Dialog {
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
    public OnDialogBtnClickListener onOKButtonClickListener;

    /**
     * 父节点
     */
    private SelectTreeNode<ISecectedItem> parent;

    private int depth;
    private int row;

    /**
     * 避免级联变更数据时，setCurrentItem引发的onSelectedChangedListener，以及
     */
    private boolean isScrollFInished = true;

    /**
     * 构造函数
     *
     * @param context                 上下文
     * @param parent                  包含第一层数据的空节点
     * @param depth                   有多少个可选滚轮（层级）
     * @param row                     每个可选滚轮（层级）有多少行，奇数
     * @param onOKButtonClickListener 点击确定时的回调
     */
    public SelectCascadeOptionsDialog(Context context, SelectTreeNode parent, int depth,
                                      int row, OnDialogBtnClickListener onOKButtonClickListener) {
        super(context, R.style.Dialog);

        this.parent = parent;
        this.depth = depth > 0 ? depth : 0;
        this.row = row % 2 == 0 ? row - 1 : row;
        this.onOKButtonClickListener = onOKButtonClickListener;

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
                        SelectCascadeOptionsDialog.this.dismiss();
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
        if (parent == null) {
            return;
        }

        initData(0, parent);
    }

    /**
     * 选项改变监听器
     */
    private OnWheelChangedListener onSelectedChangedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (isScrollFInished) {
                isScrollFInished = false;
                SelectListAdapter adapter = (SelectListAdapter) wheel.getViewAdapter();
                adapter.changeCurrentItem(newValue);

                int curDepth = (Integer) wheel.getTag(R.id.tag_depth);
                Log.e("onChanged", curDepth + ",(" + oldValue + "->" + newValue + ")");
                SelectTreeNode<ISecectedItem> curParent = adapter.getParentNode();
                if (!curParent.isLeaf()) {
                    reSetData(curDepth + 1, curParent);
                } else {
                    isScrollFInished = true;
                }
            }
        }
    };

    /**
     * 父级变化时，子级全部重置为第一项
     */
    private void reSetData(int curDepth, SelectTreeNode<ISecectedItem> curParent) {
        if (curDepth >= depth) {
            isScrollFInished = true;
            return;
        }

        WheelView childWheelView = (WheelView) llScroll.getChildAt(curDepth);

        if (childWheelView == null) {
            isScrollFInished = true;
            return;
        }
        SelectTreeNode<ISecectedItem> childParent = null;
        // 无子级数据时，置空
        if (curParent == null || curParent.isLeaf()) {
            SelectListAdapter selectAdapter = new SelectListAdapter(getContext(), null);
            childWheelView.setViewAdapter(selectAdapter);
        } else {
            childParent = curParent.getChildList()
                    .get(curParent.getSelectedChildIndex());
            childParent.setSelectedChildIndex(0);
            SelectListAdapter selectAdapter = new SelectListAdapter(getContext(), childParent);
            childWheelView.setViewAdapter(selectAdapter);
            childWheelView.setCurrentItem(0);
        }

        reSetData(curDepth + 1, childParent);
    }

    /**
     * 递归设置各层级数据
     */
    private void initData(int curDepth, SelectTreeNode<ISecectedItem> curParent) {
        if (curDepth >= depth) {
            return;
        }

        WheelView wheelView = new WheelView(getContext());
        LinearLayout.LayoutParams wheelLp = new LinearLayout.LayoutParams(0, ViewGroup
                .LayoutParams.MATCH_PARENT);
        wheelLp.weight = 1;
        wheelView.setLayoutParams(wheelLp);

        wheelView.setWheelBackground(R.color.transparent);
        wheelView.setWheelForeground(R.color.transparent);
        wheelView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);

        wheelView.setTag(R.id.tag_depth, curDepth);
        wheelView.addChangingListener(onSelectedChangedListener);

        llScroll.addView(wheelView);

        SelectTreeNode<ISecectedItem> childParent = null;

        // 有数据列表时，才赋值
        if (curParent != null && !curParent.isLeaf()) {
            int curSelectedIndex = curParent.getSelectedChildIndex();
            childParent = curParent.getChildList().get(curSelectedIndex);
            SelectListAdapter selectAdapter = new SelectListAdapter(getContext(), curParent);
            wheelView.setViewAdapter(selectAdapter);
            wheelView.setCurrentItem(curSelectedIndex);
            Log.e("initData", curDepth + ",not null");
        } else {
            Log.e("initData", curDepth + ",null");
        }

        initData(curDepth + 1, childParent);
    }

    /**
     * 给控件加上事件
     */
    private void initListiner() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOKButtonClickListener != null) {
                    SelectCascadeOptionsDialog.this.dismiss();

                    List<ISecectedItem> result = new ArrayList<>();
                    getReult(result, 0, parent);

                    onOKButtonClickListener.onOKButtonClick(result);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOKButtonClickListener != null) {
                    SelectCascadeOptionsDialog.this.dismiss();
                    onOKButtonClickListener.onClearButtonClick();
                }
            }
        });
    }

    /**
     * 从第一级开始赋值结果
     */
    private void getReult(List<ISecectedItem> result, int curDepth, SelectTreeNode<ISecectedItem>
            curNode) {
        if (curDepth >= depth || curNode == null || curNode.isLeaf()) {
            return;
        }

        SelectTreeNode<ISecectedItem> node = curNode.getChildList().get(curNode
                .getSelectedChildIndex());
        result.add(node.getObj());
        getReult(result, curDepth + 1, node);
    }

    /**
     * 列表适配器
     */
    private class SelectListAdapter extends AbstractWheelTextAdapter {
        /**
         * 父节点
         */
        private SelectTreeNode<ISecectedItem> parentNode;

        /**
         * 构造函数
         *
         * @param context 上下文
         * @param parent  父节点
         */
        public SelectListAdapter(Context context, SelectTreeNode<ISecectedItem> parent) {
            super(context, R.layout.item_selector, NO_RESOURCE);

            setItemTextResource(R.id.tv);
            this.parentNode = parent;
        }

        /**
         * 选中项改变时的回调，修改数据显示
         */
        public void changeCurrentItem(int newValue) {
            if (parentNode != null) {
                parentNode.setSelectedChildIndex(newValue);
            }
            notifyDataChangedEvent();
        }

        @Override
        public int getItemsCount() {
            return parentNode != null && parentNode.getChildList() != null ? parentNode
                    .getChildList().size()
                    : 0;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return parentNode != null && parentNode.getChildList() != null ?
                    (index >= parentNode.getChildList().size() || index < 0 ? null :
                            parentNode.getChildList().get(index).getObj().getShowText())
                    : null;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            if (index != parentNode.getSelectedChildIndex()) {
                tv.setTextColor(getContext().getResources().getColor(
                        R.color.selector_dialog_text_not_selected));
            } else {
                tv.setTextColor(getContext().getResources().getColor(
                        R.color.selector_dialog_text_selected));
            }
            return view;
        }

        public SelectTreeNode<ISecectedItem> getParentNode() {
            return parentNode;
        }
    }
}
