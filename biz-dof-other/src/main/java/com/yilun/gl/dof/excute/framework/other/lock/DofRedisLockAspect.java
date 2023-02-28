package com.yilun.gl.dof.excute.framework.other.lock;

import com.gl.dof.core.excute.framework.exception.DofServiceException;
import com.yilun.gl.dof.excute.framework.base.util.ObjectUtil;
import com.yilun.gl.dof.excute.framework.other.constants.ApiConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @Auther: gl
 * @Date: 2020/8/7 15:01
 * @Description:
 */
@Slf4j
@Component
@Aspect
@Order(1)
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class DofRedisLockAspect {

    private DofRedisUtil redisUtil;

    @Pointcut("@annotation(com.yilun.gl.dof.excute.framework.other.lock.DofRedisLock)")
    public void dofRedisLockAnnotationPointcut() {
    }

    /**
     * @param joinPoint
     * @throws DofServiceException 获取锁失败
     * @throws Throwable        目标方法执行异常
     */
    @Around(value = "dofRedisLockAnnotationPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (Objects.isNull(redisUtil)) {
            redisUtil = new DofRedisUtil(null);
        }
        if (Objects.isNull(redisUtil)) {
            log.error("can not get redisUtil bean, we can not use redis lock!");
            return joinPoint.proceed();
        }
        Method originMethod = resolveMethod(joinPoint);
        DofRedisLock lock = originMethod.getAnnotation(DofRedisLock.class);
        //获取key
        String key = generateRedisKey(joinPoint, lock);
        String value = UUID.randomUUID().toString();
        boolean locked = redisUtil.tryGetDistributedLock(
                key,
                value,
                lock.expire(),
                lock.retryTimes(),
                lock.sleepMillis());

        if (!locked) {
            log.error("RedisLockAspectTro try get lock fail.key:{},value:{}", key, value);
            throw DofServiceException.build(ApiConstant.CODE_FAIL, "try get distributed lock fail.key:" + key);
        }
        log.info("RedisLockAspectTro try get lock success. key : {},value : {}", key, value);
        try {
            return joinPoint.proceed();
        } finally {
            redisUtil.releaseDistributedLock(key, value);
            log.info("RedisLockAspectTro unlock. key : {},value : {}", key, value);
        }
    }

    private String generateRedisKey(ProceedingJoinPoint joinPoint, DofRedisLock annotation) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(annotation.prefixKey());
        Object[] parameterArgs = joinPoint.getArgs();
        //入参参数类
        Class<?>[] annoClasses = annotation.dynamicKeyClass();
        if (parameterArgs.length <= 0 || annoClasses.length <= 0) {
            keyBuilder.append(annotation.tailfixKey());
            return keyBuilder.toString();
        }
        //当属性未填的时候也是直接返回
        if (annotation.propertyName().length <= 0) {
            keyBuilder.append(annotation.tailfixKey());
            return keyBuilder.toString();
        }

        for (Class<?> annoClass : annoClasses) {
            for (Object arg : parameterArgs) {
                if (!annoClass.isAssignableFrom(arg.getClass())) {
                    continue;
                }
                keyBuilder.append(getDynamicFromPara(arg, annotation.propertyName()));
            }
        }
        keyBuilder.append(annotation.tailfixKey());
        return keyBuilder.toString();
    }

    /**
     * @param arg          入参对象
     * @param propertyName 需要拿出的对象属相
     * @return 拼到一起
     */
    private String getDynamicFromPara(Object arg, String[] propertyName) {
        Field[] declaredFields = arg.getClass().getDeclaredFields();
        if (declaredFields.length <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Map<String, Object> filedValues = ObjectUtil.getFiledValues(arg);
        for (String para : propertyName) {
            builder.append(filedValues.getOrDefault(para, ""));
        }
        return builder.toString();
    }

    /**
     * 获取加了该注解的方法
     *
     * @param joinPoint joinPoint
     * @return 返回当前的使用方法
     */
    private Method resolveMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        Method method = getDeclaredMethodFor(targetClass, signature.getName(),
                signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("Cannot resolve target method: " + signature.getMethod().getName());
        }
        return method;
    }

    /**
     * Get declared method with provided name and parameterTypes in given class and its super classes.
     * All parameters should be valid.
     *
     * @param clazz          class where the method is located
     * @param name           method name
     * @param parameterTypes method parameter type list
     * @return resolved method, null if not found
     */
    private Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethodFor(superClass, name, parameterTypes);
            }
        }
        return null;
    }

}
