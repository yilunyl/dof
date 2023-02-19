package com.yilun.gl.dof.excute.framework.core.entry;

import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.content.ContextData;

/**
 * @ClassName: biz-dof ApplicationInit
 * @Description: com.yilun.gl.dof.excute.framework.core.entry
 * @Author: 逸伦
 * @Date: 2023/2/18 23:07
 * @Version: 1.0
 */
public interface ApplicationInit<T extends ContextData, REQ, RES> {

	LogicExecutor<T> initDoSvrGroup();

	RES doLogicSchedule(REQ req, Object... others );
}
