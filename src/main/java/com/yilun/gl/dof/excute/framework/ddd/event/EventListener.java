package com.yilun.gl.dof.excute.framework.ddd.event;

/**
 * @ClassName: biz-dof EventListener
 * @Description: com.yilun.gl.dof.excute.framework.ddd.event
 * @Author: 逸伦
 * @Date: 2023/2/18 10:56
 * @Version: 1.0
 */
public interface EventListener <E extends Event> {

	Class<E> getEventClass();

	EventProcessor<E>[] getEventProcessors();
}
