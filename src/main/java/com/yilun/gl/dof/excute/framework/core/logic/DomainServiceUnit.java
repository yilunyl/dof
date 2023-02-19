package com.yilun.gl.dof.excute.framework.core.logic;

import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;

/**
 * @description: 执行引擎接口，核心执行类，每个执行单元足够小，并且内部逻辑足够内聚
 * @author: gule
 * @create: 2019-08-17 09:09
 **/
public interface DomainServiceUnit<T extends ContextData>{


    /**
     * 是否匹配规则, 该方法会被调用两次
     *  一次是在完整的执行流程之前过滤一次
     *  一次是在doLogic之前执行一次，前后两次执行的时机不同，中间的数据可能发生变化
     * @return 是否匹配
     */
    boolean  isMatch(T context);

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
