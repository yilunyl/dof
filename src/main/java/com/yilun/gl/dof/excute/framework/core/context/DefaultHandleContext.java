package com.yilun.gl.dof.excute.framework.core.context;

import com.yilun.gl.dof.excute.framework.core.context.attribute.AttributeKey;
import com.yilun.gl.dof.excute.framework.core.context.attribute.DefaultAttributeMap;

/**
 * @ClassName: biz-dof DefaultContext
 * @Description: com.yilun.gl.dof.excute.framework.core.context
 * @Author: 逸伦
 * @Date: 2023/2/24 20:36
 * @Version: 1.0
 */
public class DefaultHandleContext extends DefaultAttributeMap implements HandleContext{

	@Override
	public  <T> T getObject(Class<T> c) {
		AttributeKey<T> oriKey = AttributeKey.valueOf(c);
		return this.attr(oriKey).get();
	}

	@Override
	public <T> T getObject(Class<T> c, String alias) {
		AttributeKey<T> oriKey = AttributeKey.valueOf(c, alias);
		return this.attr(oriKey).get();
	}

	@Override
	public <T> T getObject(String beanName) {
		AttributeKey<T> oriKey = AttributeKey.valueOf(beanName);
		return this.attr(oriKey).get();
	}

	public DefaultHandleContext(){}
}
