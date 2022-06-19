package com.yilun.gl.dof.excute.framework.usage.impl;

import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.logic.DataProcessor;
import com.yilun.gl.dof.excute.framework.usage.context.TestContext;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof SpaceApplication
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 23:23
 * @Version: 1.0
 */
@Component
public class SpaceApplication implements DataProcessor<TestContext> {
	@Override
	public LogicResult doLogic(TestContext context) {
		return null;
	}

	@Override
	public boolean isMatch(TestContext c) {

		return DataProcessor.super.isMatch(c);
	}

	@Override
	public void reverse(TestContext context, LogicResult logicResult) {
		DataProcessor.super.reverse(context, logicResult);
	}
}
