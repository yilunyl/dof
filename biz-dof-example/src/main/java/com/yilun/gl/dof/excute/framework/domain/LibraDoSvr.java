package com.yilun.gl.dof.excute.framework.domain;

import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.HandleContext;
import com.gl.dof.core.excute.framework.context.attribute.AttributeKey;
import com.gl.dof.core.excute.framework.logic.DomainLogic;
import com.yilun.gl.dof.excute.framework.model.request.TestRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof TripApplication
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 23:24
 * @Version: 1.0
 */
@Component
public class LibraDoSvr implements DomainLogic {
	@Override
	public LogicResult doLogic(HandleContext context) {
		AttributeKey<TestRequest> oriKey = AttributeKey.valueOf(TestRequest.class);
		TestRequest testRequest = context.attr(oriKey).get();
		if(StringUtils.isBlank(testRequest.getName())){
			testRequest.setName("jerry");
		}
		return LogicResult.createSuccess();
	}

	@Override
	public boolean isMatch(HandleContext context) {
		return true;
	}
}
