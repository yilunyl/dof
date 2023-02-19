package com.yilun.gl.dof.excute.framework.usage.domain;

import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.logic.DomainService;
import com.yilun.gl.dof.excute.framework.usage.model.TestContext;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof SpaceApplication
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 23:23
 * @Version: 1.0
 */
@Component
public class SpaceDoSvr implements DomainService<TestContext> {
	@Override
	public LogicResult doLogic(TestContext context) {
		return null;
	}

	@Override
	public boolean isMatch(TestContext context) {
		return true;
	}

	@Override
	public void reverse(TestContext context, LogicResult logicResult) {
		DomainService.super.reverse(context, logicResult);
	}
}
