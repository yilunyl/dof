package com.gl.dof.core.excute.framework.executor;

/**
 * @ClassName: biz-dof DofResCode
 * @Description: com.yilun.gl.dof.excute.framework.exception
 * @Author: 逸伦
 * @Date: 2022/6/4 23:16
 * @Version: 1.0
 */
public enum ExecutorType {

	TREE_LIKE(1, "类树"),
	DAG_LIKE(2, "类图"),
	;
	private Integer code;
	private String message;

	ExecutorType(int i, String msg) {
		code = i;
		message = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
