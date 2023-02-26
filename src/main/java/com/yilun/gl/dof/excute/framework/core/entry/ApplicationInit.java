package com.yilun.gl.dof.excute.framework.core.entry;

import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.core.context.HandleContext;

/**
 * @ClassName: biz-dof ApplicationInit
 * @Description: com.yilun.gl.dof.excute.framework.core.entry
 * @Author: 逸伦
 * @Date: 2023/2/18 23:07
 * @Version: 1.0
 */
public interface ApplicationInit<REQ, RES> {

	/**
	 * 初始化执行逻辑
	 * @return
	 */
	LogicExecutor initDoSvrGroup();

	/**
	 * 执行初始化好的业务逻辑
	 * @param req
	 * @param others
	 * @return
	 */

	RES doLogicSchedule(REQ req, Object... others );
}
