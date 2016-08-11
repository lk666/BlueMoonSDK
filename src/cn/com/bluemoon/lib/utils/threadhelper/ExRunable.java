package cn.com.bluemoon.lib.utils.threadhelper;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Future;

/**
 * 主动回调主线程的{@link Future}，轻量级的AsyncTask
 *
 * @author luokai
 */
public abstract class ExRunable implements Runnable {

    private static final int FEEDBACK = 0x1;

    @SuppressWarnings("unchecked")
    private static Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == FEEDBACK) {
                FeedbackBean fb = (FeedbackBean) msg.obj;
                if (fb.mFeedback != null) {
                    fb.mFeedback.feedback(fb.mObj);
                }
            }
        }
    };

    /**
     * 异步操作完成后主线程中的操作
     */
    private Feedback mFeedback;

    public ExRunable(Feedback feedback) {
        mFeedback = feedback;
    }

    /**
     * 执行实际的异步操作，返回结果，给{@link Feedback}使用
     */
    public abstract Object execute();

    @Override
    public void run() {
        // 在异步线程执行execute操作
        Object obj = execute();
        if (mFeedback != null) {
            mMainHandler.sendMessage(mMainHandler.obtainMessage(FEEDBACK,
                    new FeedbackBean(mFeedback, obj)));
        }
    }

    public class FeedbackBean {
        public Feedback mFeedback;
        public Object mObj;

        public FeedbackBean(Feedback feedback, Object obj) {
            this.mFeedback = feedback;
            this.mObj = obj;
        }
    }

}
