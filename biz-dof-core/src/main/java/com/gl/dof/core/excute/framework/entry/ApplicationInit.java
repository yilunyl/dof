package com.gl.dof.core.excute.framework.entry;

import com.gl.dof.core.excute.framework.executor.LogicExecutor;

/**
 * @ClassName: biz-dof ApplicationInit
 * @Description: com.gl.dof.core.excute.framework.entry
 * @Author: 逸伦
 * @Date: 2023/2/18 23:07
 * @Version: 1.0
 */
public interface ApplicationInit<REQ, RES> extends DofExecutor<REQ, RES>{

	/**
	 * 初始化执行逻辑
	 * @return
	 */
	LogicExecutor initDoSvrGroup();
}
