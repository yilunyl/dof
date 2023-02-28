package com.gl.dof.core.excute.framework.context;

import com.gl.dof.core.excute.framework.context.attribute.Attribute;
import com.gl.dof.core.excute.framework.context.attribute.AttributeKey;
import com.gl.dof.core.excute.framework.context.attribute.DefaultAttributeMap;

/**
 * @ClassName: biz-dof DefaultContext
 * @Description: com.gl.dof.core.excute.framework.context
 * @Author: 逸伦
 * @Date: 2023/2/24 20:36
 * @Version: 1.0
 */
public class DefaultHandleContext extends DefaultAttributeMap implements HandleContext{

	private final String contextId;
	private final String contextDesc;

	public DefaultHandleContext(String contextId, String contextDesc){
		this.contextId = contextId;
		this.contextDesc = contextDesc;
	}

	@Override
	public String getHandleContextId() {
		return contextId;
	}

	@Override
	public String getHandleContextName() {
		return contextDesc;
	}

	@Override
	public <T> Attribute<T> attr(Class<T> c) {
		AttributeKey<T> oriKey = AttributeKey.valueOf(c);
		return this.attr(oriKey);
	}

	@Override
	public <T> Attribute<T> attr(String beanId) {
		AttributeKey<T> oriKey = AttributeKey.valueOf(beanId);
		return this.attr(oriKey);
	}

	@Override
	public <T> Attribute<T> attr(Class<T> c, String alias) {
		AttributeKey<T> oriKey = AttributeKey.valueOf(c, alias);
		return this.attr(oriKey);
	}

	@Override
	public <T> boolean hasAttr(Class<T> c) {
		AttributeKey<T> oriKey = AttributeKey.valueOf(c);
		return this.hasAttr(oriKey);
	}

	@Override
	public <T> boolean hasAttr(String beanId) {
		AttributeKey<T> oriKey = AttributeKey.valueOf(beanId);
		return this.hasAttr(oriKey);
	}

	@Override
	public <T> boolean hasAttr(Class<T> c, String alias) {
		AttributeKey<T> oriKey = AttributeKey.valueOf(c, alias);
		return this.hasAttr(oriKey);
	}
}
