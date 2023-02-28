package com.yilun.gl.dof.excute.framework.base.thread.threadPool;

import com.yilun.gl.dof.excute.framework.base.thread.DofRunnable;
import com.yilun.gl.dof.excute.framework.base.thread.MonitorEnum;
import com.yilun.gl.dof.excute.framework.base.thread.ThreadRequestContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: biz-dof ThreadPoolExecutorMdcWrapper
 * @Description: jdk自带的线程池
 * @Author: 逸伦
 * @Date: 2023/2/26 21:00
 * @Version: 1.0
 */
public class JdkThreadPoolExecutorMdcWrapper extends ThreadPoolExecutor {

	private final static Logger log = LoggerFactory.getLogger(JdkThreadPoolExecutorMdcWrapper.class);

	private String executorName;
	/**
	 * Task execute timeout, unit (ms), just for statistics.
	 */
	private long taskRunTimeout;
	/**
	 * Task queue wait timeout, unit (ms), just for statistics.
	 */
	private long taskQueueTimeout;

	public JdkThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                                       BlockingQueue<Runnable> workQueue, String executorName) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		this.executorName = executorName;
	}

	public JdkThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                                       BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, String executorName) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
		this.executorName = executorName;
	}

	public JdkThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                                       BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, String executorName) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
		this.executorName = executorName;
	}

	public JdkThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                                       BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
	                                       RejectedExecutionHandler handler, String executorName) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		this.executorName = executorName;
	}

	@Override
	public void execute(Runnable task) {
		super.execute(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return super.submit(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()), result);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return super.submit(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public Future<?> submit(Runnable task) {
		return super.submit(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		if (!(r instanceof DofRunnable)) {
			super.beforeExecute(t, r);
			return;
		}
		if (taskQueueTimeout > 0) {
			DofRunnable runnable = (DofRunnable) r;
			long waitTime = System.currentTimeMillis() - runnable.getSubmitTime();
			if (waitTime > taskQueueTimeout) {
				//例如可以做告警或者统计
				log.info("ExecutorServiceWrapper_monitor type={}|executorName={}|taskName={}|runTime={}|",
						MonitorEnum.TASK_QUEUEING,
						this.executorName,
						runnable.getTaskName(),
						waitTime
				);
			}
		}
		super.beforeExecute(t, r);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if (!(r instanceof DofRunnable)) {
			super.afterExecute(r, t);
			return;
		}
		if (taskRunTimeout > 0) {
			DofRunnable runnable = (DofRunnable) r;
			long runTime = System.currentTimeMillis() - runnable.getStartTime();
			if (runTime > taskRunTimeout) {
				//例如可以做告警或者统计
				log.info("ExecutorServiceWrapper_monitor type={}|executorName={}|taskName={}|runTime={}|",
						MonitorEnum.TASK_RUN,
						this.executorName,
						runnable.getTaskName(),
						runTime
				);
			}
		}
		super.afterExecute(r, t);
	}

	public void setTaskRunTimeout(long taskRunTimeout) {
		this.taskRunTimeout = taskRunTimeout;
	}

	public void setTaskQueueTimeout(long taskQueueTimeout) {
		this.taskQueueTimeout = taskQueueTimeout;
	}
}
