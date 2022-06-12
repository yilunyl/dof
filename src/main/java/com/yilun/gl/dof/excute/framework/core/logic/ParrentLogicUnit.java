package com.yilun.gl.dof.excute.framework.core.logic;

import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.executor.LogicResult;

/**
 * @description: 执行引擎接口，核心执行类，每个执行单元足够小，并且内部逻辑足够内聚
 * @author: gule
 * @create: 2019-08-17 09:09
 **/
public interface ParrentLogicUnit<T extends ContextData>{


    /**
     * 是否走异步
     *
     * @return 是否异步
     */
    boolean parallel();

    /**
     * 执行单元核心执行方法
     *
     * @param context 执行单元所处的上下文
     * @return 执行单元的执行结果
     */
    LogicResult doLogic(T context);


    /**
     * 回退方法，节点的正向整个流程失败后，需要的逆向操作，适用于资源回退，通知类节点
     *
     * @param context 上下文
     * @param logicResult 执行结果
     */
    void reverse(T context, LogicResult logicResult);
}
