package com.yilun.gl.dof.excute.framework.usage.domain;

import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.logic.DomainService;
import com.yilun.gl.dof.excute.framework.usage.model.TestContext;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof CarorderApplication
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 23:48
 * @Version: 1.0
 */
@Component
public class CarorderDoSvr implements DomainService<TestContext> {

	@Override
	public boolean isMatch(TestContext context) {
		return true;
	}
	@Override
	public LogicResult doLogic(TestContext context) {
		return null;
	}
}
