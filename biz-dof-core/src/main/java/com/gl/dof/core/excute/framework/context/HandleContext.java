package com.gl.dof.core.excute.framework.context;

import com.gl.dof.core.excute.framework.context.attribute.Attribute;
import com.gl.dof.core.excute.framework.context.attribute.AttributeMap;

/**
 * @Auther: gule.gl
 * @Date: 2021/7/23-07-23-下午4:33
 * @Description: com.amap.aos.infrastructure.framework.context
 * @version: 1.0
 */
public interface HandleContext extends AttributeMap {


	public String getHandleContextId();

	public String getHandleContextName();
	public <T> Attribute<T> attr(Class<T> c);

	public <T> Attribute<T> attr(String beanId);
	public <T> Attribute<T> attr(Class<T> c, String alias);


	public <T> boolean hasAttr(Class<T> c);

	public <T> boolean hasAttr(String beanId);

	public <T> boolean hasAttr(Class<T> c, String alias);
}
