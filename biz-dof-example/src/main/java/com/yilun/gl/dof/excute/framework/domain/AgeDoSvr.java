package com.yilun.gl.dof.excute.framework.domain;

import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.HandleContext;
import com.gl.dof.core.excute.framework.logic.DomainLogic;
import com.yilun.gl.dof.excute.framework.model.request.TestRequest;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof Application2
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 22:09
 * @Version: 1.0
 */
@Component
public class AgeDoSvr implements DomainLogic {


	@Override
	public boolean isMatch(HandleContext context) {
		return true;
	}

	@Override
	public LogicResult doLogic(HandleContext context) {
		TestRequest object = context.attr(TestRequest.class).get();
		object.setAge(10);
		return LogicResult.createSuccess();
	}

}
