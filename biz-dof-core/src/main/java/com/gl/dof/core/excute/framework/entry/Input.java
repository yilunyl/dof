package com.gl.dof.core.excute.framework.entry;

import com.gl.dof.core.excute.framework.context.HandleContext;

public interface Input<REQ> {
    void doIn(HandleContext ctx);
}
