package com.yilun.gl.dof.excute.framework.usage.application;

import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.entry.AbstractApplication;
import com.yilun.gl.dof.excute.framework.usage.model.TestContext;
import com.yilun.gl.dof.excute.framework.usage.model.request.TestRequest;
import com.yilun.gl.dof.excute.framework.usage.model.response.TestResponse;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof ChauffeurApplication
 * @Description: com.yilun.gl.dof.excute.framework.usage.application
 * @Author: 逸伦
 * @Date: 2023/2/19 15:53
 * @Version: 1.0
 */
@Component
public class ChauffeurApplication extends AbstractApplication<TestContext, TestRequest, TestResponse> {

	@Override
	public LogicExecutor<TestContext> initDoSvrGroup() {
		return null;
	}
	@Override
	protected TestContext initContext(TestRequest testRequest, Object... others) {
		return null;
	}

	@Override
	protected TestResponse buildResponse(LogicResult logicResult, TestContext testContext, TestRequest testRequest, Object... others) {
		return null;
	}
}
