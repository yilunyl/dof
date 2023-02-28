package com.gl.dof.core.excute.framework.logic;

import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.HandleContext;

/**
 * @ClassName DataExecuteUnit
 * @Author gule.gl
 * @Date 2021/6/19 12:08 下午
 * @Description DataExecuteUnit
 * @Version 1.0
 */
public interface DomainService extends DomainServiceUnit {

	/**
	 * 回退方法，节点的正向整个流程失败后，需要的逆向操作，适用于资源回退，通知类节点
	 *
	 * @param context 上下文
	 * @param logicResult 执行结果
	 */
	@Override
	default void reverse(HandleContext context , LogicResult logicResult){

	}

}
