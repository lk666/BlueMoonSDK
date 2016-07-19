package cn.com.bluemoon.lib.utils.threadhelper;

/**
 * 当执行完耗时操作后，进行的回调， 方法的回调会在主线程中进行
 *
 * @author luokai
 */
public interface Feedback<T> {
    /**
     * 当执行完耗时操作后，进行的回调， 方法的回调会在主线程中进行。
     *
     * @param obj 反馈的数据对象
     */
    void feedback(T obj);
}
