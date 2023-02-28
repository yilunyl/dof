package com.gl.dof.core.excute.framework.ddd.exception;

/**
 * @ClassName: biz-dof EventProcessException
 * @Description: com.yilun.gl.dof.excute.framework.ddd.exception
 * @Author: 逸伦
 * @Date: 2023/2/18 10:59
 * @Version: 1.0
 */
public class EventProcessException extends BizException {
	public EventProcessException(String code, String msg) {
		super(code, msg);
	}

	public EventProcessException(String code, String msg, Throwable cause) {
		super(code, msg, cause);
	}
}
