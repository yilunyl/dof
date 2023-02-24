package com.yilun.gl.dof.excute.framework.usage.domain;

import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.context.HandleContext;
import com.yilun.gl.dof.excute.framework.core.logic.DomainService;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof StrategyResponseDataApplication
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 23:36
 * @Version: 1.0
 */
@Component
public class StrategyResponseDataDoSvr implements DomainService {
	@Override
	public LogicResult doLogic(HandleContext context) {
		return LogicResult.createSuccess();
	}

	@Override
	public boolean isMatch(HandleContext context) {
		return true;
	}
}
