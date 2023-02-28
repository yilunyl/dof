package com.gl.dof.core.excute.framework.ddd.exception;

/**
 * @ClassName: biz-dof RepositoryException
 * @Description: com.yilun.gl.dof.excute.framework.ddd.exception
 * @Author: 逸伦
 * @Date: 2023/2/18 10:59
 * @Version: 1.0
 */
public class RepositoryException extends BizException {
	public RepositoryException(String code, String msg) {
		super(code, msg);
	}

	public RepositoryException(String code, String msg, Throwable cause) {
		super(code, msg, cause);
	}
}
