package com.gl.dof.excute.framework.base.thread;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * @ClassName: biz-dof DofRunnable
 * @Description: com.yilun.gl.dof.excute.framework.thread
 * @Author: 逸伦
 * @Date: 2023/2/26 19:06
 * @Version: 1.0
 */
public class DofRunnable implements Runnable {

	private final Runnable runnable;

	private final Long submitTime;

	private Long startTime;

	private final String taskName;

	public DofRunnable(Runnable runnable, String taskName) {
		this.runnable = runnable;
		submitTime = System.currentTimeMillis();
		this.taskName = taskName;
	}

	@Override
	public void run() {
		runnable.run();
	}

	public Long getSubmitTime() {
		return submitTime;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public String getTaskName() {
		return taskName;
	}


	public static DofRunnable of(Runnable runnable, String name) {
		if (StringUtils.isBlank(name)) {
			name = runnable.getClass().getSimpleName() + "-" + UUID.randomUUID();
		}
		return new DofRunnable(runnable, name);
	}
}
