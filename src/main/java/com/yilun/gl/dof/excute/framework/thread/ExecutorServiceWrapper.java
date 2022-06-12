package com.yilun.gl.dof.excute.framework.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: gl
 * @Date: 2021/1/21 15:41
 * @Description:
 */

@Slf4j
public class ExecutorServiceWrapper extends ThreadRequestContextUtil.ThreadPoolExecutorMdcWrapper {

    private static final double QUEUE_SIZE_RATIO = 0.75;
    private static final ScheduledExecutorService SCHEDULE = Executors.newSingleThreadScheduledExecutor();
    private static final ConcurrentHashMap<String, ThreadRequestContextUtil.ThreadPoolExecutorMdcWrapper> POOL_EXECUTOR_MAP = new ConcurrentHashMap<>();

    static {
        SCHEDULE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Map.Entry<String, ThreadRequestContextUtil.ThreadPoolExecutorMdcWrapper> entry : POOL_EXECUTOR_MAP.entrySet()) {
                        ThreadRequestContextUtil.ThreadPoolExecutorMdcWrapper threadPoolExecutor = entry.getValue();
                        if (threadPoolExecutor == null) {
                            return;
                        }
                        String executorName = entry.getKey();
                        int activeCount = threadPoolExecutor.getActiveCount();
                        int queueSize = threadPoolExecutor.getQueue().size();
                        int queueRemainingCapacity = threadPoolExecutor.getQueue().remainingCapacity();
                        int initQueueSize = queueRemainingCapacity + queueSize;
                        double ratio = queueSize / initQueueSize;
                        if (ratio > QUEUE_SIZE_RATIO) {
                            log.error("ExecutorServiceWrapper线程池监控:executorName:{},activeCount:{},queueSize{},threadPoolExecutor:{} threadPool used queue ratio is {}, queueInitSize:{}",
                                    executorName, activeCount, queueSize, threadPoolExecutor, ratio, initQueueSize);
                        } else {
                            log.info("ExecutorServiceWrapper线程池监控:executorName:{},activeCount:{},queueSize{},threadPoolExecutor:{} threadPool used queue ratio is {}, queueInitSize:{}",
                                    executorName, activeCount, queueSize, threadPoolExecutor, ratio, initQueueSize);
                        }
                    }
                } catch (Exception e) {
                    log.warn("schedule fail", e);
                }
            }
        }, 0L, 20L, TimeUnit.SECONDS);
    }


    public ExecutorServiceWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * 构建一个ThreadPoolExecutor
     */
    public static ThreadPoolExecutor create(int corePoolSize, final String executorName) {

        if(POOL_EXECUTOR_MAP.containsKey(executorName)){
            return POOL_EXECUTOR_MAP.get(executorName);
        }
        ThreadRequestContextUtil.ThreadPoolExecutorMdcWrapper threadPoolExecutor = buildDefaultThreadPoolExecutor(corePoolSize, executorName);
        POOL_EXECUTOR_MAP.put(executorName, threadPoolExecutor);
        return threadPoolExecutor;
    }

    /**
     * 返回一个默认配置的线程池
     * corePoolSize: corePoolSize
     * maximumPoolSize: corePoolSize * 2
     * keepAliveTime: 60L
     * unit: TimeUnit.SECONDS
     * workQueue: new LinkedBlockingQueue<Runnable>(corePoolSize * 20)
     * ThreadFactory: executorName + "-" + atomicInteger.incrementAndGet()
     * RejectedExecutionHandler: AbortPolicy
     */
    private static ThreadRequestContextUtil.ThreadPoolExecutorMdcWrapper buildDefaultThreadPoolExecutor(int corePoolSize, final String executorName) {
        return new ThreadRequestContextUtil.ThreadPoolExecutorMdcWrapper(
                corePoolSize, corePoolSize * 2,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(corePoolSize * 20),
                new ThreadFactory() {
                    private AtomicInteger atomicInteger = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(executorName + "-" + atomicInteger.incrementAndGet());
                        return thread;
                    }
                });
    }


    public static ThreadPoolExecutor getThreadPoolExecutor(int corePoolSize, final String executorName) {
        return create(corePoolSize, executorName);
    }
}
