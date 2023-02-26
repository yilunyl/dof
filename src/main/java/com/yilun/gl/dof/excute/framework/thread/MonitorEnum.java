package com.yilun.gl.dof.excute.framework.thread;

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
	private Integer code;
	private String message;

	MonitorEnum(String code, String msg) {
		code = code;
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

	@Override
	public String toString() {
		return "code=" + code + "_" + "msg=" + message;
	}
}
