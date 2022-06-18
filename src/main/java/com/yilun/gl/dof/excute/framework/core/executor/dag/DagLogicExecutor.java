package com.yilun.gl.dof.excute.framework.core.executor.dag;

import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.content.ContextData;

/**
 * @ClassName: biz-dof DagLogicExecutor
 * @Description: 基于有向无环图结构实现的执行器，支持复杂的业务逻辑编排
 * @Author: 逸伦
 * @Date: 2022/6/18 21:51
 * @Version: 1.0
 */
public class DagLogicExecutor <T extends ContextData> implements LogicExecutor<T> {


	@Override
	public LogicResult doLogicSchedule(T context) {
		return null;
	}
}
