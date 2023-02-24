package com.yilun.gl.dof.excute.framework.core.context;

import com.yilun.gl.dof.excute.framework.core.context.attribute.AttributeKey;
import com.yilun.gl.dof.excute.framework.core.context.attribute.AttributeMap;

/**
 * @Auther: gule.gl
 * @Date: 2021/7/23-07-23-下午4:33
 * @Description: com.amap.aos.infrastructure.framework.context
 * @version: 1.0
 */
public interface HandleContext extends AttributeMap {

	/**
	 * 通过类直接拿对象
	 * @param c 类
	 * @return 对象
	 * @param <T> 泛型
	 */
	public  <T> T getObject(Class<T> c);

	/**
	 * 通过类直接拿对象
	 * @param c 类
	 * @return 对象
	 * @param <T> 泛型
	 */
	public  <T> T getObject(Class<T> c, String alias);

	/**
	 * 通过类直接拿对象
	 * @param beanName 对象名字
	 * @return 对象
	 * @param <T> 泛型
	 */
	public  <T> T getObject(String beanName);

}
