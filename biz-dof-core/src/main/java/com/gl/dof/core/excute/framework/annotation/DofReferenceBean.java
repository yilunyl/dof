package com.gl.dof.core.excute.framework.annotation;

import com.gl.dof.core.excute.framework.entry.AbstractApplication;
import com.gl.dof.core.excute.framework.exception.DofResCode;
import com.gl.dof.core.excute.framework.exception.DofServiceException;
import com.gl.dof.core.excute.framework.factory.ExecutorFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Component
public class DofReferenceBean implements ApplicationListener, ApplicationContextAware, BeanPostProcessor {

    private static final HashMap<String, AbstractApplication> funcMap = new HashMap<>();

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        doHandleInitializationLocalFieldMap(bean);
        return bean;
    }

    /**
     * 格式 [] 需要并行  , 需要串行
     * @param bean
     */
    private void doHandleInitializationLocalFieldMap(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        //初始化对应的bean执行流程
        Arrays.stream(fields).filter(field -> Objects.nonNull(field.getAnnotation(DofReference.class))).forEach(field -> {
            DofReference annotation = field.getAnnotation(DofReference.class);
            AbstractApplication executor = ExecutorFactory.getInstance(applicationContext).getExecutor(annotation);
            if(Objects.isNull(executor)){
                throw DofServiceException.build(DofResCode.EXECUTOR_INIT_FAIL);
            }
            try {
                field.setAccessible(true);
                field.set(executor, bean);
            } catch (IllegalAccessException e) {
                throw DofServiceException.build(DofResCode.EXECUTOR_INIT_FAIL);
            }
        });
    }
}
