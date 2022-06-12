package com.yilun.gl.dof.excute.framework.thread;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: gl
 * @Date: 2021/1/21 19:18
 * @Description:
 */
public class ThreadRequestContextUtil {

    /**
     * 日志跟踪id名。
     */
    public static final String LOG_TRACE_ID = "traceId";

    public static void setTraceIdIfAbsent() {
        if (MDC.get(LOG_TRACE_ID) == null) {
            MDC.put(LOG_TRACE_ID, RandomStringUtils.random(10));
        }
    }

    public static void setTraceId() {
        MDC.put(LOG_TRACE_ID, RandomStringUtils.random(10));
    }

    public static void setTraceId(String traceId) {
        MDC.put(LOG_TRACE_ID, traceId);
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(requestAttributes, true);
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            //赋值上下文信息
            if (Objects.isNull(RequestContextHolder.getRequestAttributes())) {
                RequestContextHolder.setRequestAttributes(requestAttributes, true);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(requestAttributes, true);
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            //赋值上下文信息
            if (Objects.isNull(RequestContextHolder.getRequestAttributes())) {
                RequestContextHolder.setRequestAttributes(requestAttributes, true);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }

    /**
     * spring的线程池
     */
    public static class ThreadPoolTaskExecutorMdcWrapper extends ThreadPoolTaskExecutor {

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

    /**
     * jdk自带的线程池
     */
    public static class ThreadPoolExecutorMdcWrapper extends ThreadPoolExecutor {

        private RequestAttributes requestAttributes;

        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        }

        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                            RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
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
    }

    /**
     * ForkJoinPool的线程池
     */
    public static class ForkJoinPoolMdcWrapper extends ForkJoinPool {
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
}

