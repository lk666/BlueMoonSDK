package cn.com.bluemoon.lib.view.selectordialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 选项多叉树节点
 * Created by lk on 2016/7/21.
 */
public class SelectTreeNode<T extends Object> implements Serializable {
    private T obj;
    private SelectTreeNode<T> parentNode;
    private int selectedChildIndex;
    private List<SelectTreeNode<T>> childList;

    public SelectTreeNode() {
        initChildList();
    }

    public SelectTreeNode(SelectTreeNode<T> parentNode) {
        this.setParentNode(parentNode);
        initChildList();
    }

    /**
     * 是否叶节点
     */
    public boolean isLeaf() {
        if (childList == null) {
            return true;
        } else {
            if (childList.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 插入一个child节点到当前节点中
     */
    public void addChildNode(SelectTreeNode<T> treeNode) {
        initChildList();
        childList.add(treeNode);
    }

    private void initChildList() {
        if (childList == null) {
            childList = new ArrayList<>();
        }
    }

    /** 返回当前节点的父辈节点集合 */
    public List<SelectTreeNode<T>> getElders() {
        List<SelectTreeNode<T>> elderList = new ArrayList<>();
        SelectTreeNode<T> parentNode = this.getParentNode();
        if (parentNode == null) {
            return elderList;
        } else {
            elderList.add(parentNode);
            elderList.addAll(parentNode.getElders());
            return elderList;
        }
    }

    /** 返回当前节点的孩子集合 */
    public List<SelectTreeNode<T>> getChildList() {
        return childList;
    }

    public void setChildList(List<SelectTreeNode<T>> childList) {
        this.childList = childList;
    }

    public SelectTreeNode<T> getParentNode() {
        return parentNode;
    }

    public void setParentNode(SelectTreeNode<T> parentNode) {
        this.parentNode = parentNode;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public int getSelectedChildIndex() {
        return selectedChildIndex;
    }

    public void setSelectedChildIndex(int selectedChildIndex) {
        this.selectedChildIndex = selectedChildIndex;
    }

}

