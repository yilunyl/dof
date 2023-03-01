package com.gl.dof.core.excute.framework.annotation;

import com.gl.dof.core.excute.framework.entry.AbstractApplication;
import com.gl.dof.core.excute.framework.exception.DofResCode;
import com.gl.dof.core.excute.framework.exception.DofServiceException;
import com.gl.dof.core.excute.framework.factory.ExecutorFactory;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DofReferenceBean implements ApplicationListener, ApplicationContextAware, BeanPostProcessor {

    private static final HashMap<String, AbstractApplication> funcMap = new HashMap<>();

    private static final HashMap<String, String> funcLoogicFlowMap = new HashMap<>();

    private static final ConcurrentHashMap<Field, Object> fieldObjMap = new ConcurrentHashMap<>();

    private final static Logger logger = LoggerFactory.getLogger(DofReferenceBean.class);

    private ApplicationContext applicationContext;

    @SneakyThrows
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ContextRefreshedEvent){
            this.genFieldProxy();
        }

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        this.initFieldMap(bean);
        return bean;
    }

    /**
     * 格式 [] 需要并行  , 需要串行
     *
     * @param bean
     */
    private void initFieldMap(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        //初始化对应的bean执行流程
        Arrays.stream(fields).filter(field -> Objects.nonNull(field.getAnnotation(DofReference.class))).forEach(field -> {
            fieldObjMap.put(field, bean);
        });
    }

    private void genFieldProxy(){
        fieldObjMap.forEach((field,bean) -> {
            try {
                AbstractApplication executor = null;
                DofReference annotation = field.getAnnotation(DofReference.class);
                if (funcMap.containsKey(annotation.funcKey())) {
                    executor = funcMap.get(annotation.funcKey());
                    String oldLogicFlow = funcLoogicFlowMap.get(annotation.funcKey());
                    if (Objects.equals(oldLogicFlow, annotation.logicFlow())) {
                        logger.error("DofReferenceBean 存在相同的编排功能key 但是编排流程不一样 ");
                        throw DofServiceException.build(DofResCode.REPEAT_FUNCKEY_BUT_LOGIC_FLOW_IS_DIFFERENT);
                    }
                } else {
                    executor = ExecutorFactory.getInstance(applicationContext).getExecutor(annotation);
                    funcMap.put(annotation.funcKey(), executor);
                    funcLoogicFlowMap.put(annotation.funcKey(), annotation.logicFlow());
                }
                if (Objects.isNull(executor)) {
                    logger.error("DofReferenceBean 无法构建有效的编排流程");
                    throw DofServiceException.build(DofResCode.EXECUTOR_INIT_FAIL);
                }
                field.setAccessible(true);
                field.set(bean, executor);
            } catch (IllegalAccessException e) {
                logger.error("DofReferenceBean doHandleInitializationLocalFieldMap exception ",e);
                throw DofServiceException.build(DofResCode.EXECUTOR_INIT_EXCEPTION);
            }
        });
    }

}
