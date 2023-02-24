package com.yilun.gl.dof.excute.framework.usage.domain;

import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.context.HandleContext;
import com.yilun.gl.dof.excute.framework.core.logic.DomainService;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof SpaceApplication
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 23:23
 * @Version: 1.0
 */
@Component
public class SpaceDoSvr implements DomainService {
	@Override
	public boolean isMatch(HandleContext context) {
		return false;
	}

	@Override
	public LogicResult doLogic(HandleContext context) {
		return null;
	}

	@Override
	public void reverse(HandleContext context, LogicResult logicResult) {
		DomainService.super.reverse(context, logicResult);
	}
}
