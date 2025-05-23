package com.yilun.gl.dof.excute.framework.domain;

import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.HandleContext;
import com.gl.dof.core.excute.framework.logic.DomainLogic;
import com.yilun.gl.dof.excute.framework.model.request.TestRequest;
import com.yilun.gl.dof.excute.framework.model.response.TestResponse;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof Application3
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 22:10
 * @Version: 1.0
 */
@Component
public class ResponseDoSvr implements DomainLogic {

	@Override
	public LogicResult doLogic(HandleContext context) {

		String address = context.attr(String.class, AddressDoSvr.addressKey).get();
		String address2 = context.attr(String.class, AddressDoSvr.addressKey2).get();
		TestRequest testRequest = context.attr(TestRequest.class).get();
		TestResponse testResponse = new TestResponse();
		context.attr(TestResponse.class).set(testResponse);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("姓名=").append(testRequest.getName()).append("\n")
				.append("年龄=").append(testRequest.getAge()).append("\n")
				.append("地址=").append(address).append("\n")
				.append("地址2=").append(address2).append("\n");
		testResponse.setFinalStringName(stringBuilder.toString());
		//移除上下文的request
		context.attr(TestRequest.class).set(null);
		return LogicResult.createSuccess();
	}

	@Override
	public boolean isMatch(HandleContext context) {
		return true;
	}
}
