package com.yilun.gl.dof.excute.framework.exception;

/**
 * @ClassName: biz-dof DofResCode
 * @Description: com.yilun.gl.dof.excute.framework.exception
 * @Author: 逸伦
 * @Date: 2022/6/4 23:16
 * @Version: 1.0
 */
public enum DofResCode {

	SUCCESS(0, "成功"),
	FAILE(1, "失败"),
	EMPTY(3, "处理结果为空")
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
