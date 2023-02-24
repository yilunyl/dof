package com.yilun.gl.dof.excute.framework.usage.domain;

import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.context.HandleContext;
import com.yilun.gl.dof.excute.framework.core.context.attribute.AttributeKey;
import com.yilun.gl.dof.excute.framework.core.logic.DomainService;
import com.yilun.gl.dof.excute.framework.usage.model.request.TestRequest;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof Application2
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 22:09
 * @Version: 1.0
 */
@Component
public class PredioctDesDoSvr implements DomainService {

	@Override
	public boolean isMatch(HandleContext context) {
		return true;
	}
	@Override
	public LogicResult doLogic(HandleContext context) {
		TestRequest testRequest = context.getObject(TestRequest.class);
		testRequest.setName("张志颖");
		return LogicResult.createSuccess();
	}
}
