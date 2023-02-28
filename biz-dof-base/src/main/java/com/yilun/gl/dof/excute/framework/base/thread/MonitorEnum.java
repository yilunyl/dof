package com.yilun.gl.dof.excute.framework.base.thread;

/**
 * @ClassName: biz-dof MonitorEnum
 * @Description: com.yilun.gl.dof.excute.framework.thread
 * @Author: 逸伦
 * @Date: 2023/2/26 19:20
 * @Version: 1.0
 */
public enum MonitorEnum {

	COMMON("common", "线程池运行状态监控"),
	REJECTED("rejected", "任务拒绝"),
	TASK_RUN("task_run", "任务执行"),
	TASK_QUEUEING("task_queueing", "任务排队")
	;
	private String code;
	private String message;

	MonitorEnum(String code1, String msg) {
		code = code1;
		message = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "code:" + code + "_" + "msg:" + message;
	}
}
