package com.gl.dof.core.excute.framework.executor;


import com.gl.dof.core.excute.framework.context.HandleContext;
import com.gl.dof.core.excute.framework.common.LogicResult;

/**
 * @description: 执行引擎接口，核心执行类
 * @author: gule
 * @create: 2019-08-17 09:09
 **/
public interface LogicExecutor {

    ExecutorType executorType();

    /**
     * 执行引擎 处理执行流程
     *
     * @param ctx 容器上下文，使用方自己控制
     * @return 执行引擎执行结果
     */
    LogicResult doLogicSchedule(HandleContext ctx);

}
