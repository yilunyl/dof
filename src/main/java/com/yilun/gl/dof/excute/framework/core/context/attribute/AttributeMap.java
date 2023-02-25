package com.yilun.gl.dof.excute.framework.core.context.attribute;



/**
 * @ClassName: biz-dof AttributeMap
 * @Description: com.yilun.gl.dof.excute.framework.core.context
 * @Author: 逸伦
 * @Date: 2023/2/24 09:35
 * @Version: 1.0
 */
public interface AttributeMap {

	/**
	 * Get the {@link Attribute} for the given {@link AttributeKey}. This method will never return null, but may return
	 * an {@link Attribute} which does not have a value set yet.
	 */
	<T> Attribute<T> attr(AttributeKey<T> key);

	/**
	 * Returns {@code true} if and only if the given {@link Attribute} exists in this {@link io.netty.util.AttributeMap}.
	 */
	<T> boolean hasAttr(AttributeKey<T> key);

	/**
	 * 清空AttributeMap
	 * @return
	 */
	@Deprecated
	void clear();
}
