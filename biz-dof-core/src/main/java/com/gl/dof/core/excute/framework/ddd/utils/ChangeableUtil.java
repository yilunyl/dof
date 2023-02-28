package com.gl.dof.core.excute.framework.ddd.utils;

import com.gl.dof.core.excute.framework.ddd.model.Changeable;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.List;

public final class ChangeableUtil {
    private ChangeableUtil() {

    }

    public static boolean isChanged(Object obj) {
        for (Field field : getAllFields(obj)) {
            if (Changeable.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(obj);
                    if (null != fieldValue) {
                        Changeable changeable = Changeable.class.cast(fieldValue);
                        if (changeable.isChanged()) {
                            return true;
                        }
                    }
                } catch (IllegalAccessException e) {
                    // nop
                }
            }
        }
        return false;
    }


    private static List<Field> getAllFields(Object obj) {
        return getAllFields(obj.getClass(), Lists.newArrayList());
    }

    private static List<Field> getAllFields(Class clazz, List<Field> fieldList) {
        fieldList.addAll(Lists.newArrayList(clazz.getDeclaredFields()));
        Class<?> superClass = clazz.getSuperclass();
        if (Object.class == superClass) {
            return fieldList;
        }
        return getAllFields(superClass, fieldList);
    }
}
