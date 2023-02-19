package com.yilun.gl.dof.excute.framework.ddd.enums;

/**
 * @ClassName: biz-dof IEnumConverter
 * @Description: com.yilun.gl.dof.excute.framework.ddd.enums
 * @Author: 逸伦
 * @Date: 2023/2/18 10:55
 * @Version: 1.0
 */
public class IEnumConverter {
	public static <T> T getCode(IEnum<T> iEnum) {
		if (iEnum == null) {
			return null;
		}
		return iEnum.getCode();
	}
}
