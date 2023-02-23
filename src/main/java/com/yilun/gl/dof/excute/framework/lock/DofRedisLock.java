package com.yilun.gl.dof.excute.framework.lock;

import com.yilun.gl.dof.excute.framework.exception.DofServiceException;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于redis的锁工具,目前支持的使用方式
 * 1、接口上直接加该注解
 * 2、本类可以调用其他类的方法
 * 3、调用本类中的其他的方法的时候的处理,两种二选一即可
 * 【1】该方法必须声明成public,并且在调用该方法的地方需要使用((Class)AopContext.currentProxy()).method
 * 【2】注入自己,然后自己.相应的方法
 * eg. @Autowired
 * private BillDubboServiceImpl self;
 * self.update();update为加该注解的方法
 * <p>
 * 使用方式
 *
 * @DofRedisLock( prefixKey = "test-pre", dynamicKeyClass = BillDetailRequestEntity.class, propertyName = {"billId", "pageNum", "pageSize"}, tailfixKey = "test-tail")
 * public test(BillDetailRequestEntity para1){}
 * 该使用方式会按照依次按照prefixKey、propertyName在BillDetailRequestEntity入参中的实际值、tailfixKey进行拼接缓存使用的key=test-preAD2020052514594208000023110test-tail
 * key的缓存时间、重试次数、休息时间在expire、retryTimes、sleepMillis
 * <p>
 * 其中对象也支持dynamicKeyClass = {BillDetailRequestEntity1.class,BillDetailRequestEntity2.class}这种使用
 * @throws DofServiceException  获取锁失败
 * @throws Throwable  目标方法执行异常
 * @Author gule
 * @Date 2020-08-16
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DofRedisLock {

    /**
     * key prefix
     *
     * @return
     */
    String prefixKey();

    /**
     * 入参的类
     *
     * @return
     */
    Class<?>[] dynamicKeyClass() default {};

    /**
     * 类属性的字段
     *
     * @return
     */
    String[] propertyName() default "";

    /**
     * tail prefix
     *
     * @return
     */
    String tailfixKey() default "";

    /**
     * expire. default = 5 * 60 * 1_000L
     *
     * @return
     */
    long expire() default 5 * 60 * 1_000L;

    /**
     * retyr times. default = 3
     *
     * @return
     */
    int retryTimes() default 3;

    /**
     * sleep millis. default = 60 * 100
     *
     * @return
     */
    long sleepMillis() default 100;

}
