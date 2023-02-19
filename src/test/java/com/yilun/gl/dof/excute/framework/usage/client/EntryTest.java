package com.yilun.gl.dof.excute.framework.usage.client;

import com.yilun.gl.dof.excute.framework.BizDofApplicationTests;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.usage.application.CarListApplication;
import com.yilun.gl.dof.excute.framework.usage.application.ChauffeurApplication;
import com.yilun.gl.dof.excute.framework.usage.model.TestContext;
import com.yilun.gl.dof.excute.framework.usage.model.request.TestRequest;
import com.yilun.gl.dof.excute.framework.usage.model.response.TestResponse;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @ClassName: biz-dof EntryTest
 * @Description: com.yilun.gl.dof.excute.framework.usage.entry
 * @Author: 逸伦
 * @Date: 2022/6/19 13:14
 * @Version: 1.0
 */

public class EntryTest extends BizDofApplicationTests {

	@Resource
	private CarListApplication carListApplication;
	@Resource
	private ChauffeurApplication chauffeurApplication;
	@Test
	public void entryTest(){
		TestRequest testRequest = new TestRequest();

		TestResponse response = carListApplication.doLogicSchedule(testRequest);

		Assert.assertNotNull(response);
	}


	@Test
	public void entryTest2(){
		TestRequest testRequest = new TestRequest();

		TestResponse response = chauffeurApplication.doLogicSchedule(testRequest);

		Assert.assertNotNull(response);
	}
}
