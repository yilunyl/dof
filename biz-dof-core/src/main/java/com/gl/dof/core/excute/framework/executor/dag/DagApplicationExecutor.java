package com.gl.dof.core.excute.framework.executor.dag;

import com.gl.dof.core.excute.framework.executor.ExecutorType;
import com.gl.dof.core.excute.framework.executor.LogicExecutor;
import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.HandleContext;

/**
 * @ClassName: biz-dof DagLogicExecutor
 * @Description: 基于有向无环图结构实现的执行器，支持复杂的业务逻辑编排
 * @Author: 逸伦
 * @Date: 2022/6/18 21:51
 * @Version: 1.0
 */
public class DagApplicationExecutor implements LogicExecutor {


	@Override
	public ExecutorType executorType() {
		return ExecutorType.DAG_LIKE;
	}

	@Override
	public LogicResult doLogicSchedule(HandleContext context) {
		return null;
	}
}
