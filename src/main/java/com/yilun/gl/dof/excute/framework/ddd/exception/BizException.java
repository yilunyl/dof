package com.yilun.gl.dof.excute.framework.ddd.exception;

/**
 * @ClassName: biz-dof BizException
 * @Description: com.yilun.gl.dof.excute.framework.ddd.exception
 * @Author: 逸伦
 * @Date: 2023/2/18 10:58
 * @Version: 1.0
 */
public class BizException extends Exception{
	private final String code;

	public String getCode() {
		return code;
	}

	public BizException(String code,String msg){
		super(msg);
		this.code = code;
	}

	public BizException(String code,String msg,Throwable cause){
		super(msg,cause);
		this.code = code;
	}
}
