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

/**
 * 多层级递进级联条件选择弹窗（如省市区）
 *
 * @author Luokai
 */
public class SelectOptionDialog extends Dialog {
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
     * 传入的obj需继承此接口
     */
    public interface ISecectedItem {
        /**
         * 在滚动栏中显示的文本
         */
        String getShowText();
    }

    /**
     * 点击确定时的回调
     */
    public OnOKButtonClickListener onOKButtonClickListener;

    /**
     * 点击确定时的回调接口
     */
    public interface OnOKButtonClickListener {
        /**
         * 点击确定时的回调
         *
         * @param selectedObj 选择项（从第一级开始）
         */
        void onOKButtonClick(List<ISecectedItem> selectedObj);

        /**
         * 点击清除按钮的回调
         */
        void onClearButtonClick();
    }

    /**
     * 父节点
     */
    private SelectTreeNode<ISecectedItem> parent;

    private int depth;
    private int row;

    /**
     * 构造函数
     *
     * @param context                 上下文
     * @param parent                  包含第一层数据的空节点
     * @param depth                   有多少个可选滚轮（层级）
     * @param row                     每个可选滚轮（层级）有多少行，奇数
     * @param onOKButtonClickListener 点击确定时的回调
     */
    public SelectOptionDialog(Context context, SelectTreeNode<ISecectedItem> parent, int depth,
                              int row,
                              OnOKButtonClickListener onOKButtonClickListener) {
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
                        SelectOptionDialog.this.dismiss();
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
        SelectTreeNode<ISecectedItem> cur = parent;
        for (int i = 0; i < depth; i++) {
            WheelView wheelView = new WheelView(getContext());
            LinearLayout.LayoutParams wheelLp = new LinearLayout.LayoutParams(0, ViewGroup
                    .LayoutParams.MATCH_PARENT);
            wheelLp.weight = 1;
            wheelView.setLayoutParams(wheelLp);

            wheelView.setWheelBackground(R.color.transparent);
            wheelView.setWheelForeground(R.color.transparent);
            wheelView.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);

            SelectListAdapter selectAdapter = new SelectListAdapter(getContext(), cur);
            wheelView.setViewAdapter(selectAdapter);
            wheelView.setCurrentItem(cur.getSelectedChildIndex());
            wheelView.setTag(R.id.tag_depth, i);
            wheelView.addChangingListener(onSelectedChangedListener);

            llScroll.addView(wheelView);
            cur = cur.getChildList().get(cur.getSelectedChildIndex());
        }
    }

    /**
     * 选项改变监听器
     */
    OnWheelChangedListener onSelectedChangedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            SelectListAdapter adapter = (SelectListAdapter) wheel.getViewAdapter();
            ((SelectListAdapter) wheel.getViewAdapter()).changeCurrentItem(newValue);

            int curDepth = (Integer) wheel.getTag(R.id.tag_depth);
            SelectTreeNode<ISecectedItem> curParent = adapter.getParentNode();
            reSetChild(curDepth, curParent, newValue);
        }
    };

    /**
     * 父级变化时，子级全部重置
     */
    private void reSetChild(int curDepth, SelectTreeNode<ISecectedItem> curParent,
                            int selectedIndex) {
        if (curDepth >= depth) {
            return;
        }
        // 有子级数据时
        if (curParent != null && curParent.getChildList() != null) {
            SelectTreeNode<ISecectedItem> childParent = curParent.getChildList().get(selectedIndex);

            WheelView childWheelView = (WheelView) llScroll.getChildAt(curDepth + 1);
            SelectListAdapter selectAdapter = new SelectListAdapter(getContext(), childParent);
            childWheelView.setViewAdapter(selectAdapter);
            childParent.setSelectedChildIndex(0);
            childWheelView.setCurrentItem(0);
            // TODO: lk 2016/7/21
            selectAdapter.changeCurrentItem(0);
//                    childWheelView.removeChangingListener(onSelectedChangedListener);
//                    childWheelView.addChangingListener(onSelectedChangedListener);

            reSetChild(curDepth + 1, childParent, 0);
        } else {
            WheelView childWheelView = (WheelView) llScroll.getChildAt(curDepth + 1);
            SelectListAdapter selectAdapter = new SelectListAdapter(getContext(), null);
            childWheelView.setViewAdapter(selectAdapter);
            selectAdapter.changeCurrentItem(0);
            reSetChild(curDepth + 1, null, 0);
        }
    }

    /**
     * 给控件加上事件
     */
    private void initListiner() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOKButtonClickListener != null) {
                    SelectOptionDialog.this.dismiss();

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
                    SelectOptionDialog.this.dismiss();
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
        if (curDepth >= depth || curNode == null || curNode.getChildList() == null) {
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

        public SelectTreeNode<SelectOptionDialog.ISecectedItem> getParentNode() {
            return parentNode;
        }
    }
}
