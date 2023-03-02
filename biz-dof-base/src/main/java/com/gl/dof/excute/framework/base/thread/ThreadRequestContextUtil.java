package com.gl.dof.excute.framework.base.thread;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @Auther: gl
 * @Date: 2021/1/21 19:18
 * @Description:
 */
public class ThreadRequestContextUtil {
    private final static Logger log = LoggerFactory.getLogger(ThreadRequestContextUtil.class);

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
}

