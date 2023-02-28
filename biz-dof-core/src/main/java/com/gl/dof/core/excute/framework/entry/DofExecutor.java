package com.gl.dof.core.excute.framework.entry;


import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.HandleContext;

public interface DofExecutor<REQ, RES>  {

    /**
     * 执行初始化好的业务逻辑
     * @param req
     * @param others
     * @return
     */

    RES doLogicSchedule(Input<REQ> input, Output<RES> output );

}
