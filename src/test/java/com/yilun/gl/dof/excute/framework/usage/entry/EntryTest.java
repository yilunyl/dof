package com.yilun.gl.dof.excute.framework.usage.entry;

import com.yilun.gl.dof.excute.framework.BizDofApplicationTests;
import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.usage.context.TestContext;
import com.yilun.gl.dof.excute.framework.usage.context.request.TestRequest;
import com.yilun.gl.dof.excute.framework.usage.context.response.TestResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @ClassName: biz-dof EntryTest
 * @Description: com.yilun.gl.dof.excute.framework.usage.entry
 * @Author: 逸伦
 * @Date: 2022/6/19 13:14
 * @Version: 1.0
 */

public class EntryTest extends BizDofApplicationTests {

	@Qualifier("cardLogicExecutor")
	private LogicExecutor<TestContext> cardLogicExecutor;

	@Test
	public void entryTest(){
		TestRequest testRequest = new TestRequest();
		TestResponse testResponse = new TestResponse();
		TestContext testContext = new TestContext(testRequest, testResponse);
		LogicResult logicResult = cardLogicExecutor.doLogicSchedule(testContext);

		if(logicResult.isSuccess()){
			Assert.assertNotNull(testContext.getResponse());
		}
	}
}
