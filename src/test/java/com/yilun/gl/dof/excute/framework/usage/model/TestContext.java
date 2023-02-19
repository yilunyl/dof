package com.yilun.gl.dof.excute.framework.usage.model;

import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.usage.model.request.TestRequest;
import com.yilun.gl.dof.excute.framework.usage.model.response.TestResponse;

import java.util.List;

/**
 * @ClassName: biz-dof Test
 * @Description: com.yilun.gl.dof.excute.framework.usage
 * @Author: 逸伦
 * @Date: 2022/6/4 23:39
 * @Version: 1.0
 */

public class TestContext extends ContextData<TestRequest, TestResponse> {

	private List<Object> strategyList;


	private Object response;

	public TestContext(TestRequest request) {
		super(request);
	}
}
