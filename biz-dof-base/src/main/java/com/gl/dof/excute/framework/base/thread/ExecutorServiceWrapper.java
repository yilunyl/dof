package com.gl.dof.excute.framework.base.thread;

import com.gl.dof.excute.framework.base.thread.threadPool.JdkThreadPoolExecutorMdcWrapper;
import com.gl.dof.excute.framework.base.util.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
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

public class ExecutorServiceWrapper extends JdkThreadPoolExecutorMdcWrapper {
    private static final ScheduledExecutorService SCHEDULE = Executors.newSingleThreadScheduledExecutor();

    private final static Logger logger = LoggerFactory.getLogger(ExecutorServiceWrapper.class);

    private static final ConcurrentHashMap<String, JdkThreadPoolExecutorMdcWrapper> POOL_EXECUTOR_MAP = new ConcurrentHashMap<>();

    static {
        SCHEDULE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Map.Entry<String, JdkThreadPoolExecutorMdcWrapper> entry : POOL_EXECUTOR_MAP.entrySet()) {
                        JdkThreadPoolExecutorMdcWrapper threadPoolExecutor = entry.getValue();
                        if (threadPoolExecutor == null) {
                            return;
                        }
                        String executorName = entry.getKey();
                        int activeCount = threadPoolExecutor.getActiveCount();
                        int queueSize = threadPoolExecutor.getQueue().size();
                        int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
                        int queueRemainingCapacity = threadPoolExecutor.getQueue().remainingCapacity();
                        int queueCapacity = queueRemainingCapacity + queueSize;
                        /**
                         * 1. 线程池活跃度告警。活跃度 = activeCount / maximumPoolSize，当活跃度达到配置的阈值时，会进行事前告警。
                         * 2. 队列容量告警。容量使用率 = queueSize / queueCapacity，当队列容量达到配置的阈值时，会进行事前告警。
                         * 3. 拒绝策略告警。当触发拒绝策略时，会进行告警。
                         * 4. 任务执行超时告警。重写 ThreadPoolExecutor 的 afterExecute() 和 beforeExecute()，根据当前时间和开始时间的差值算出任务执行时长，超过配置的阈值会触发告警。
                         * 5. 任务排队超时告警。重写 ThreadPoolExecutor 的  beforeExecute()，记录提交任务时时间，根据当前时间和提交时间的差值算出任务排队时长，超过配置的阈值会触发告警
                         */
                        logger.info("ExecutorServiceWrapper_monitor type={}|executorName={}|activeCount={}|线程池活跃度={}|队列容量={}|",
                                MonitorEnum.COMMON,
                                executorName,
                                activeCount,
                                MathUtil.divide(activeCount, maximumPoolSize, 3),
                                MathUtil.divide(queueSize, queueCapacity, 3)
                                );
                    }
                } catch (Exception e) {
                    logger.warn("schedule fail", e);
                }
            }
        }, 0L, 20L, TimeUnit.SECONDS);
    }

    private ExecutorServiceWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, String executorName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler, executorName);
    }

    /**
     * ThreadPoolExecutor with DofRunnable
     * @param corePoolSize 核心线程数
     * @param executorName 线程池名字
     * @param taskRunTimeout 任务执行时间 该参数生效的话 线程必须使用DofRunnable
     * @param taskWaitTimeout 任务等待时间 该参数生效的话 线程必须使用DofRunnable
     * @return ThreadPoolExecutor
     */
    private static ThreadPoolExecutor create(int corePoolSize, final String executorName, long taskRunTimeout, long taskWaitTimeout) {

        if(POOL_EXECUTOR_MAP.containsKey(executorName)){
            logger.info("ExecutorServiceWrapper#create ThreadPoolExecutor_exist, executorName={}", executorName);
            JdkThreadPoolExecutorMdcWrapper threadPoolExecutorMdcWrapper = POOL_EXECUTOR_MAP.get(executorName);
            int oldCorePoolSize = threadPoolExecutorMdcWrapper.getCorePoolSize();
            if(corePoolSize != oldCorePoolSize){
                threadPoolExecutorMdcWrapper.setCorePoolSize(Math.max(oldCorePoolSize, corePoolSize));
            }
            return threadPoolExecutorMdcWrapper;
        }
        JdkThreadPoolExecutorMdcWrapper threadPoolExecutor = buildDefaultThreadPoolExecutor(corePoolSize, executorName, taskRunTimeout, taskWaitTimeout);
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
    private static JdkThreadPoolExecutorMdcWrapper buildDefaultThreadPoolExecutor(int corePoolSize, final String executorName, long taskRunTimeout, long taskWaitTimeout) {
        JdkThreadPoolExecutorMdcWrapper executorMdcWrapper =  new JdkThreadPoolExecutorMdcWrapper(
                corePoolSize,
                corePoolSize * 2,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(corePoolSize * 20),
                new ThreadFactory() {
                    private AtomicInteger atomicInteger = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        DofRunnable dofRunnable = new DofRunnable(r, executorName);
                        Thread thread = new Thread(dofRunnable);
                        thread.setName(executorName + "-" + atomicInteger.incrementAndGet());
                        return thread;
                    }
                },
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        logger.error("ExecutorServiceWrapper rejectedExecution name={}  ", executorName);
                    }
                }, executorName);

        executorMdcWrapper.setTaskQueueTimeout(taskWaitTimeout);
        executorMdcWrapper.setTaskRunTimeout(taskRunTimeout);
        return executorMdcWrapper;
    }

    public static ThreadPoolExecutor getThreadPoolExecutor(int corePoolSize, final String executorName) {
        return create(corePoolSize, executorName,1,1);
    }


    public static ThreadPoolExecutor getThreadPoolExecutor(int corePoolSize, final String executorName, long taskRunTimeout, long taskWaitTimeout) {
        return create(corePoolSize, executorName, taskRunTimeout, taskWaitTimeout);
    }
}
