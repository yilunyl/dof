package com.yilun.gl.dof.excute.framework.base.util;

import com.yilun.gl.dof.excute.framework.base.util.thread.ExecutorServiceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @description: 异步工具类
 * @author: gl
 * @create: 2019-05-28 12:10
 **/


@Slf4j
public final class AsyncTool {

    private final static Logger logger = LoggerFactory.getLogger(AsyncTool.class);

    private static final ExecutorService executorService = ExecutorServiceWrapper.getThreadPoolExecutor(100, AsyncTool.class.getSimpleName());

    static {
        // 暂时挂个shutdownhook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                executorService.shutdown();
            }
        });
    }

    public interface Executor {
        void doExecute();
    }

    public static void submit(final Executor executor, ExecutorService executorService) {
        submitWithRetry(executor, 1, executorService);
    }

    public static void submit(Executor executor) {
        submitWithRetry(executor, 1);
    }

    public static void submitWithRetry(final Executor executor, final int count) {
        submitWithRetry(executor, count, executorService);
    }

    public static void submitWithRetry(final Executor executor, final int count, ExecutorService executorService) {
        Runnable retryRunnable = new Runnable() {
            @Override
            public void run() {
                boolean needRetry = true;
                for (int i = 0; i < count && needRetry; i++) {
                    try {
                        executor.doExecute();
                        needRetry = false;
                    } catch (Exception ex) {
                        logger.error("invoke biz error : ", ex);
                    }
                }
            }
        };
        executorService.submit(retryRunnable);
    }

}
