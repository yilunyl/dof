package com.yilun.gl.dof.excute.framework.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * @Auther: gl
 * @Date: 2021/1/22 14:10
 * @Description:
 */
@Slf4j
public class ForkJoinWorkerThreadWrapper extends ForkJoinWorkerThread {

    private final static ThreadLocal<Integer> taskCounter = new ThreadLocal<Integer>();

    /**
     * Creates a ForkJoinWorkerThread operating in the given pool.
     *
     * @param pool the pool this thread works in
     * @throws NullPointerException if pool is null
     */
    protected ForkJoinWorkerThreadWrapper(ForkJoinPool pool) {
        super(pool);
    }

    @Override
    protected void onStart() {
        super.onStart();

        log.info("MyWorkerThread {}: Initializing task counter.\n", getId());
        taskCounter.set(0);
    }

    @Override
    protected void onTermination(Throwable exception) {
        log.info("MyWorkerThread:{} {}\n", getId(), taskCounter.get());
        super.onTermination(exception);
    }

    public void addTask() {
        taskCounter.set(taskCounter.get() + 1);
        ;
    }
}
