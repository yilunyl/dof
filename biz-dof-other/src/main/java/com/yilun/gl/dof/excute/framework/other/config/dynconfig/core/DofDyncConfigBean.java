package com.yilun.gl.dof.excute.framework.other.config.dynconfig.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gl.dof.core.excute.framework.exception.DofServiceException;
import com.yilun.gl.dof.excute.framework.other.config.dynconfig.DofDyncValue;
import com.yilun.gl.dof.excute.framework.other.config.dynconfig.core.entity.ConfigChangedEvent;
import com.yilun.gl.dof.excute.framework.other.config.dynconfig.core.entity.RefreshConfgiEntity;
import com.yilun.gl.dof.excute.framework.other.constants.ApiConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.spring.ServiceBean;
import org.apache.dubbo.config.spring.context.event.ServiceBeanExportedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: gl
 * @Date: 2020/10/22 15:59
 * @Description:
 */
@Component
public class DofDyncConfigBean implements ApplicationListener, ApplicationContextAware, BeanPostProcessor {
    private final static Logger log = LoggerFactory.getLogger(DofDyncConfigBean.class);
    @Autowired
    private DyncConfigContext dyncConfigContext;

    /**
     * 最外层map：key是path
     * 里层map：key是bean，val是List<Field>
     */
    private ConcurrentHashMap<String, Map<Object, Set<Field>>> localFieldMap = new ConcurrentHashMap<>(20);

    /**
     * spring的上下文
     */
    private ApplicationContext applicationContext;

    private String getWholePath(DofDyncValue annotation) {
        String fullPath = "/" + annotation.project() + "." + annotation.path();
        return fullPath.replace(".", "/");
    }

    /**
     * 将从zk获取到的值转成具体的对象
     *
     * @param jsonString jonsString
     */
    private void parseFieldObj(String jsonString, Field field, Object bean) {
        if (StringUtils.isBlank(jsonString)) {
            return;
        }
        field.setAccessible(true);
        try {
            Object value = null;
            if (field.getType().isAssignableFrom(Integer.class) || field.getType().isAssignableFrom(int.class)) {
                //处理整形
                value = Integer.parseInt(jsonString);
            } else if (field.getType().isAssignableFrom(Boolean.class) || field.getType().isAssignableFrom(boolean.class)) {
                value = Boolean.valueOf(jsonString);
            } else if (field.getType().isAssignableFrom(Double.class) || field.getType().isAssignableFrom(double.class)) {
                //处理double
                value = Double.parseDouble(jsonString);
            } else if (field.getType().isAssignableFrom(Long.class) || field.getType().isAssignableFrom(long.class)) {
                //处理long
                value = Long.parseLong(jsonString);
            } else if (field.getType().isAssignableFrom(String.class)) {
                //处理String
                value = jsonString.trim();
            } else if (jsonString.startsWith("{")) {
                //处理json对象
                JSONObject json = JSONObject.parseObject(jsonString);
                value = JSON.toJavaObject(json, field.getType());
            } else if (jsonString.startsWith("[") && field.getType().isAssignableFrom(List.class)) {
                //处理对象数组
                JSONArray objects = JSONArray.parseArray(jsonString);
                Class<?>[] innerClasses = field.getAnnotation(DofDyncValue.class).innerClass();
                if (innerClasses.length != 1) {
                    throw DofServiceException.build(ApiConstant.CODE_SUGGEST_FAIL, "存在对象数组，需要在注解上填写对象数组里面的对象类，不然无法处理");
                }
                value = objects.toJavaList(innerClasses[0]);
            } else if (field.getType().isAssignableFrom(List.class)) {
                //处理逗号分隔的对象
                if (StringUtils.isBlank(jsonString)) {
                    value = Collections.EMPTY_LIST;
                } else {
                    String[] split = jsonString.split(",");
                    value = new ArrayList<>(Arrays.asList(split));
                }
            } else {
                throw DofServiceException.build(ApiConstant.CODE_SUGGEST_FAIL, "不支持的类型解析，需要自己处理");
            }
            field.set(bean, value);
        } catch (Exception e) {
            log.error("dofDyncConfigBean.parseFieldObj \n " +
                    "字段:{}, 转换成相关对象解析错误:", field.getName() + e.getMessage(), e.getCause());
        }
    }


    /**
     * 使用spring的事件去处理相关的更新
     *
     * @param event zk变更事件
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ConfigChangedEvent) {
            updateDataByConfigChangedEvent(event);
        }
        if (event instanceof ServiceBeanExportedEvent) {
            ServiceBeanExportedEvent serviceBeanExportedEvent = (ServiceBeanExportedEvent) event;
            ServiceBean serviceBean = serviceBeanExportedEvent.getServiceBean();
            Object serviceBeanRef = serviceBean.getRef();
            doHandleInitializationLocalFieldMap(serviceBeanRef);
        }
    }

    private void updateDataByConfigChangedEvent(ApplicationEvent event) {
        RefreshConfgiEntity configDataBean = (RefreshConfgiEntity) event.getSource();
        if (Objects.isNull(configDataBean)) {
            log.error("无效的配置中心变更事件:{}", JSON.toJSON(event));
            return;
        }
        log.info("DofDyncHandleEvent path:{}, data:{}", configDataBean.getWholePath(), new String(configDataBean.getData()));
        Map<Object, Set<Field>> objectListMap = localFieldMap.get(generateMapKey(configDataBean.getWholePath()));
        if (CollectionUtils.isNotEmptyMap(objectListMap)) {
            //说明这个路径下引用到的key都需要进行更新
            objectListMap.forEach((objName, fields) -> {
                for (Field field : fields) {
                    parseFieldObj(new String(configDataBean.getData()), field, objName);
                }
            });
        }
    }

    /**
     * 通过postProcess去收集带有注解的字段
     *
     * @param bean     bean
     * @param beanName beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        doHandleInitializationLocalFieldMap(bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 销毁  断开相关的和配置中心的链接
     */
    @PreDestroy
    public void destory() {
        log.info("dofDyncConfigBean is doing destory...");
        List<DyncConfigService> all = dyncConfigContext.getAll();
        all.forEach(DyncConfigService::destroy);
    }

    /**
     * key按照所有节点用'/'分隔，例如 '1234/haha/123'
     *
     * @param oldKey oldKey
     * @return 格式化以后的key
     */
    private String generateMapKey(String oldKey) {
        if (StringUtils.isNotEmpty(oldKey)) {
            return oldKey.replace(".", "/").trim();
        }
        return "";
    }

    /**
     * doHandleInitializationLocalFieldMap
     *
     * @param bean obj
     */
    private void doHandleInitializationLocalFieldMap(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        //第一次创建bean肯定没有对应的值
        Arrays.stream(fields).filter(field -> Objects.nonNull(field.getAnnotation(DofDyncValue.class))).forEach(field -> {
            DofDyncValue annotation = field.getAnnotation(DofDyncValue.class);
            //根据i获取到具体的配置中心类型
            DyncConfigService dyncConfigService = dyncConfigContext.chooseConfig(annotation.dyncType());
            String fullPath = getWholePath(annotation);
            byte[] dataByte = dyncConfigService.createNodeCache(fullPath);
            if (Objects.isNull(dataByte)) {
                throw DofServiceException.build(ApiConstant.CODE_FAIL, "路径" + fullPath + "未配置，请配置后重启");
            }
            //获取初始值并初始化
            parseFieldObj(new String(dataByte), field, bean);
            //保存
            String key = generateMapKey(getWholePath(annotation));
            if (localFieldMap.containsKey(key)) {
                Map<Object, Set<Field>> objectListMap = localFieldMap.get(key);
                if (objectListMap.containsKey(bean)) {
                    Set<Field> existBeanFields = objectListMap.get(bean);
                    existBeanFields.add(field);
                } else {
                    Set<Field> fieldList = new HashSet<>(5);
                    fieldList.add(field);
                    objectListMap.put(bean, fieldList);
                }
            } else {
                Map<Object, Set<Field>> objFieldMap = new HashMap<>(5);
                Set<Field> fieldList = new HashSet<>(5);
                fieldList.add(field);
                objFieldMap.put(bean, fieldList);
                localFieldMap.put(key, objFieldMap);
            }
        });
    }

}
