package com.gl.dof.core.excute.framework.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: biz-dof DofReference
 * @Description: com.gl.dof.core.excute.framework.annotation
 * @Author: 逸伦
 * @Date: 2023/2/28 09:33
 * @Version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface DofReference {

	String funcKey() default "";

	/**
	 * request class, default value is void.class
	 */
	Class<?> requestClass() default void.class;

	/**
	 * request class, default value is void.class
	 */
	Class<?> responseClass() default void.class;

	int corePoolSize() default 6;
}
