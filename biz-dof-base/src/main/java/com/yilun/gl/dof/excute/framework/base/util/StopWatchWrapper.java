package com.yilun.gl.dof.excute.framework.base.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.Objects;
import java.util.UUID;

/**
 * @ClassName: biz-dof StopWatchWrapper
 * @Description: com.yilun.gl.dof.excute.framework.util
 * @Author: 逸伦
 * @Date: 2023/2/19 14:19
 * @Version: 1.0
 */
public class StopWatchWrapper extends StopWatch {

	private final static Logger log = LoggerFactory.getLogger(StopWatchWrapper.class);

	private StopWatchWrapper(String id) {
		super(id);
	}

	private StopWatchWrapper() {
		super();
	}
	public void start(String taskName) {
		if(StringUtils.isBlank(taskName)){
			taskName = super.getId() + "_sub";
		}
		if(StringUtils.isNotBlank(super.currentTaskName())){
			if(Objects.equals(super.currentTaskName(), taskName)){
				log.error("StopWatchWrapper_start_repeat_task {}, please check", taskName);
				return;
			}
			if (super.isRunning()) {
				super.stop();
			}
			super.start(taskName);
		}else{
			super.start(taskName);
		}
	}

	public void stop() {
		if(StringUtils.isBlank(super.currentTaskName())){
			return;
		}
		if (super.isRunning()) {
			super.stop();
		}
	}

	public static StopWatchWrapper getInstance(String taskName){
		if(StringUtils.isBlank(taskName)){
			taskName = UUID.randomUUID().toString();
		}
		return new StopWatchWrapper(taskName);
	}
}
