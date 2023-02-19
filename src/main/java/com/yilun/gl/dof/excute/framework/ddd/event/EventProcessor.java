package com.yilun.gl.dof.excute.framework.ddd.event;

import com.yilun.gl.dof.excute.framework.ddd.exception.EventProcessException;

/**
 * @ClassName: biz-dof EventProcessor
 * @Description: com.yilun.gl.dof.excute.framework.ddd.event
 * @Author: 逸伦
 * @Date: 2023/2/18 10:57
 * @Version: 1.0
 */
public interface EventProcessor <E extends Event>  {
	String getTopic();

	String getTag();

	Boolean needProcess(E event);

	void process(E event) throws EventProcessException;
}
