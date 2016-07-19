package cn.com.bluemoon.lib.utils.threadhelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 线程辅助类，适合执行可结束的任务
 * <p>
 * 应根据实际情况划分多个线程池
 * <p>
 * <b>使用此线程辅助类应避免在异步线程中使用线程辅助类启动异步线程，因为可能因为使用同一线程池出现饿死的情况</b>
 * <p>
 * 根据与主线程的关系，划分任务类型：<br/>
 * 1、无回调的异步任务（{@link Runnable}） )<br/>
 * 2、在主线程启动，并回调主线程的异步任务 <br/>
 * 3、主动回调主线程的任务（{@link Runnable}）（其实就是主线程handler的post的简化版）
 * 
 * @author luokai
 * 
 */
public class ThreadPool {
	public final static String TAG = "ThreadPool";

	public final static int CPU_CORE = Runtime.getRuntime()
			.availableProcessors();
	/** 普通线程池，执行时间较短的任务 */
	public final static ExecutorService GENERAL_THREAD_POOL = new ThreadPoolExecutor(
			CPU_CORE + 1, CPU_CORE * 2 + 1, 60L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	/** 图片线程池，瞬间并发高、时耗较长（如时耗5s、瞬间并发20/50），线程池大较好 */
	public final static ExecutorService PICTURE_THREAD_POOL = new ThreadPoolExecutor(
			CPU_CORE + 1, CPU_CORE * 2 + 1, 60L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());
}
