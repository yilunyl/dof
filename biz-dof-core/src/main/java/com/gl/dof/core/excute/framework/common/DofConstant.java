package com.gl.dof.core.excute.framework.common;

/**
 * @ClassName: biz-dof Constant
 * @Description: com.gl.dof.core.excute.framework.common
 * @Author: 逸伦
 * @Date: 2023/3/2 09:02
 * @Version: 1.0
 */
public class DofConstant {

	/**
	 * 如果从配置文件读取配置流的前缀
	 */
	private static final String PROPERTIE_PREFIX_FOR_LOGXIFLOW = "dof.logicflow.";

	public static String getPropertieLogxiflowKey(String funcKey){
		return PROPERTIE_PREFIX_FOR_LOGXIFLOW + funcKey;
	}
}
