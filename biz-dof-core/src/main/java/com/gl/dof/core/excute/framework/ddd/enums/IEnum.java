package com.gl.dof.core.excute.framework.ddd.enums;

/**
 * @ClassName: biz-dof IEnum
 * @Description: com.yilun.gl.dof.excute.framework.ddd.enums
 * @Author: 逸伦
 * @Date: 2023/2/18 10:55
 * @Version: 1.0
 */
public interface IEnum<T> {
	T getCode();

	String getDesc();

	static <T,E extends IEnum<T>> E codeOf(Class<E> clazz,T code){
		assert clazz.isEnum();
		if(code == null){
			return null;
		}
		for (E e : clazz.getEnumConstants()) {
			if(e.getCode().equals(code)){
				return e;
			}
		}
		throw new RuntimeException("invalid code");
	}
}
