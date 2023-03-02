package com.gl.dof.excute.framework.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @description: 重试工具类
 * @author: gule
 * @create: 2019-05-23 15:14
 **/
public class RetryUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RetryUtil.class);

    /**
     * 重试指定retry count次数,并且将EXCEPTION纪录在指定的LOGGER中
     *
     * @param executor     外部实现业务
     * @param retryCount   重试次数
     * @param exceptionLog 指定EXCEPTION LOG
     * @param <T>          指定返回类型
     * @return 执行结果
     */
    public static <T> T retryByCountWhenException(IExecutor<T> executor, int retryCount, Logger exceptionLog) {

        for (int i = 0; i < retryCount; i++) {
            try {
                return executor.execute();
            } catch (Exception e) {
                Logger log = exceptionLog == null ? LOG : exceptionLog;
                String errorMsg = "retryByCountWhenException retry count:" + i + ", error:" + e;
                if (i == retryCount - 1) {
                    log.error(errorMsg, e);
                } else {
                    log.warn(errorMsg, e);
                }
            }
        }

        return null;
    }

    /**
     * 重试指定retry count次数,并且将EXCEPTION纪录在指定的LOGGER中,并且每次失败时候休息一下
     */
    public static <T> T retryByCountWhenException(IExecutor<T> executor, int retryCount, Logger exceptionLog,
                                                  long sleepMillis) {
        for (int i = 0; i < retryCount; i++) {
            try {
                return executor.execute();
            } catch (Exception e) {
                Logger log = exceptionLog == null ? LOG : exceptionLog;
                String errorMsg = "retryByCountWhenException retry count:" + i + ", error:" + e;
                if (i == retryCount - 1) {
                    log.error(errorMsg, e);
                } else {
                    log.warn(errorMsg, e);
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(sleepMillis);
                } catch (Exception ex) {
                    log.error("retryByCountWhenException thread is interrupted ! error:{}", ex, ex);
                }
            }
        }
        return null;
    }


    /**
     * 根据返回结果重试指定retry count次数,并且将EXCEPTION纪录在指定的LOGGER中,并且每次失败时候休息一下
     *
     * @param executor     具体业务实现接口
     * @param callback     结果回调处理
     * @param retryCount   重试次数
     * @param exceptionLog 日志
     * @param sleepMillis  休眠时间 毫秒
     * @param errorMsg     异常信息
     * @param <T>          返回结果
     * @return 返回重试之后的结果
     */
    public static <T> T retryByCountWhenCondition(IExecutor<T> executor, ICallback<T> callback, int retryCount,
                                                  Logger exceptionLog, long sleepMillis, String errorMsg) {

        T t = null;
        for (int i = 0; i < retryCount; i++) {
            try {
                t = executor.execute();
                if (callback.execute(t)) {
                    return t;
                }
            } catch (Exception e) {
                if (exceptionLog != null) {
                    exceptionLog.warn("retryByCountWhenCondition , model or reason={} ,retry count :{}", errorMsg, i, e);
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(sleepMillis * (i + 1));
            } catch (Exception ex) {
                if (exceptionLog != null) {
                    exceptionLog.warn("retryByCountWhenCondition thread is interrupted ! error:{}",
                            errorMsg, ex);
                }
            }
            if (exceptionLog != null) {
                exceptionLog.warn(errorMsg + ",第{}次查询", i);
            }
        }
        return t;
    }

    /**
     * 留给外部实现
     *
     * @param <T>
     */
    public interface IExecutor<T> {
        T execute() throws Exception;
    }

    /**
     * 自定义结果回调函数
     *
     * @param <T>
     */
    public interface ICallback<T> {
        boolean execute(T t) throws Exception;
    }
}
