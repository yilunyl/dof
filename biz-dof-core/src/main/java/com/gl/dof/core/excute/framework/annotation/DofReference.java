package com.gl.dof.core.excute.framework.annotation;


import com.gl.dof.core.excute.framework.executor.ExecutorType;

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
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface DofReference {

	/**
	 * 编排流程key 必填
	 * @return key
	 */
	String funcKey();

	/**
	 * 具体得编排流程,如果为空，即 logicFlow="" 则会从默认生效的配置文件中读取
	 * @return 流程
	 */
	String logicFlow() default "";

	/**
	 * 执行器选择
	 * @return 流程
	 */
	ExecutorType execTypeSel() default ExecutorType.TREE_LIKE;

	/**
	 * 编排需要的核心线程数
	 * 最大线程数是核心数的两倍
	 * @return int
	 */
	int corePoolSize() default -1;

	/**
	 * 线程池是否使用通用线程池
	 * 当不使用的时候，新线程池的名字默认使用funcKey
	 * @return 是
	 */
	boolean useCommonPool() default true;
}
