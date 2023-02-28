package com.gl.dof.core.excute.framework.exception;

/**
 * @ClassName: biz-dof DofResCode
 * @Description: com.yilun.gl.dof.excute.framework.exception
 * @Author: 逸伦
 * @Date: 2022/6/4 23:16
 * @Version: 1.0
 */
public enum DofResCode {

	SUCCESS(0, "success"),
	FAILE(1, "fail"),

	EXECUTOR_INIT_FAIL(2, "executor_init_fail"),
	EMPTY(3, "result_empty"),
	ILLEGAL_ACCESS(4, "illegal_access")
	;
	private Integer code;
	private String message;

	DofResCode(int i, String msg) {
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
