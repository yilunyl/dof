package com.yilun.gl.dof.excute.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: gl
 * @Date: 2020/10/13 10:59
 * @Description:
 */
@Slf4j
public class ObjectUtil {

    private final static Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    /**
     * 获取对象的所有属性值，返回一个对象MAP
     */
    public static Map<String, Object> getFiledValues(Object o) {
        String[] fieldNames = getFiledName(o);
        Object[] value = new Object[fieldNames.length];
        Map<String, Object> valueMap = new HashMap<>();
        for (int i = 0; i < fieldNames.length; i++) {
            valueMap.put(fieldNames[i], getFieldValueByName(fieldNames[i], o));
        }
        return valueMap;
    }

    /**
     * 获取属性名数组
     */
    public static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getType());
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 根据属性名获取属性值
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

}
