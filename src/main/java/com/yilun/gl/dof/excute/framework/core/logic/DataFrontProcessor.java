package com.yilun.gl.dof.excute.framework.core.logic;

import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.content.ContextData;

/**
 * @ClassName DataPrepareUnit
 * @Author gule.gl
 * @Date 2021/6/19 12:04 下午
 * @Description DataPrepareUnit
 * @Version 1.0
 */
public interface DataFrontProcessor<T extends ContextData> extends ParrentLogicUnit<T> {

	@Override
	default boolean parallel(){
		return false;
	}


	/**
	 * 回退方法，节点的正向整个流程失败后，需要的逆向操作，适用于资源回退，通知类节点
	 *
	 * @param context 上下文
	 * @param logicResult 执行结果
	 */
	@Override
	default void reverse(T context, LogicResult logicResult){

	}
}
