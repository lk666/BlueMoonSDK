package cn.com.bluemoon.lib.view.selectordialog;

import java.util.List;

/**
 * 点击确定时的回调接口
 */
public interface OnDialogBtnClickListener {
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