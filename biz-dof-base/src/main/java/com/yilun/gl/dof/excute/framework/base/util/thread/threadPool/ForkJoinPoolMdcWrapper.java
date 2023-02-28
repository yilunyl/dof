package com.yilun.gl.dof.excute.framework.base.util.thread.threadPool;

import com.yilun.gl.dof.excute.framework.base.util.thread.ThreadRequestContextUtil;
import org.slf4j.MDC;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @ClassName: biz-dof ForkJoinPoolMdcWrapper
 * @Description: ForkJoinPool的线程池
 * @Author: 逸伦
 * @Date: 2023/2/26 21:05
 * @Version: 1.0
 */
public class ForkJoinPoolMdcWrapper extends ForkJoinPool {
	public ForkJoinPoolMdcWrapper() {
		super();
	}

	public ForkJoinPoolMdcWrapper(int parallelism) {
		super(parallelism);
	}

	public ForkJoinPoolMdcWrapper(int parallelism, ForkJoinWorkerThreadFactory factory,
	                              Thread.UncaughtExceptionHandler handler, boolean asyncMode) {
		super(parallelism, factory, handler, asyncMode);
	}

	@Override
	public void execute(Runnable task) {
		super.execute(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public <T> ForkJoinTask<T> submit(Runnable task, T result) {
		return super.submit(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()), result);
	}

	@Override
	public <T> ForkJoinTask<T> submit(Callable<T> task) {
		return super.submit(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()));
	}
}
