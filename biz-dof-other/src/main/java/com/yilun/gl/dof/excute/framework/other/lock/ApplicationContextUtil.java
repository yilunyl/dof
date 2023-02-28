package com.yilun.gl.dof.excute.framework.other.lock;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Auther: gl
 * @Date: 2020/9/1 20:26
 * @Description: 通用util，缓存了本次启动的spring的bean对象
 */
@Component
public class ApplicationContextUtil implements ApplicationListener<ContextRefreshedEvent> {

    private static ApplicationContext applicationContext;

    private ApplicationContextUtil() {
    }

    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    public static <T> T getBean(String name, Class<T> beanClass) {
        return applicationContext.getBean(name, beanClass);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        applicationContext = contextRefreshedEvent.getApplicationContext();
    }
}
