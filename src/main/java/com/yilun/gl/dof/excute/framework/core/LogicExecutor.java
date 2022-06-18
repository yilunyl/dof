package com.yilun.gl.dof.excute.framework.core;


import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;

/**
 * @description: 执行引擎接口，核心执行类
 * @author: gule
 * @create: 2019-08-17 09:09
 **/
public interface LogicExecutor<T extends ContextData> {

    /**
     * 执行引擎 处理执行流程
     *
     * @param context 容器上下文，使用方自己控制
     * @return 执行引擎执行结果
     */
    LogicResult doLogicSchedule(T context);

}
