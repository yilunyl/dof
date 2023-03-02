package com.gl.dof.excute.framework.base.thread.threadPool;

import com.gl.dof.excute.framework.base.thread.ThreadRequestContextUtil;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @ClassName: biz-dof SpringThreadPoolTaskExecutorMdcWrapper
 * @Description: spring的线程池
 * @Author: 逸伦
 * @Date: 2023/2/26 21:06
 * @Version: 1.0
 */
public class SpringThreadPoolTaskExecutorMdcWrapper extends ThreadPoolTaskExecutor {
	@Override
	public void execute(Runnable task) {
		super.execute(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public void execute(Runnable task, long startTimeout) {
		super.execute(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()), startTimeout);
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
	public ListenableFuture<?> submitListenable(Runnable task) {
		return super.submitListenable(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
		return super.submitListenable(ThreadRequestContextUtil.wrap(task, MDC.getCopyOfContextMap()));
	}
}
