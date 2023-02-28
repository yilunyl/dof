package com.gl.dof.core.excute.framework.entry;

import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.HandleContext;

public interface Output<RES> {
    RES doOut(HandleContext ctx, LogicResult logicResult);

}
