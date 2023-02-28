package com.yilun.gl.dof.excute.framework.client;

import com.yilun.gl.dof.excute.framework.application.SomeThing2Application;
import com.yilun.gl.dof.excute.framework.application.SomeThingApplication;
import com.yilun.gl.dof.excute.framework.model.request.TestRequest;
import com.yilun.gl.dof.excute.framework.model.response.TestResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName: biz-dof EntryTest
 * @Description: com.yilun.gl.dof.excute.framework.usage.entry
 * @Author: 逸伦
 * @Date: 2022/6/19 13:14
 * @Version: 1.0
 */

@RestController
@Slf4j
public class EntryTest {

	@Resource
	private SomeThingApplication someThingApplication;
	@Resource
	private SomeThing2Application someThing2Application;

	@GetMapping(path = "/get/test")
	public void entryTest(){
		TestRequest testRequest = new TestRequest();
		TestResponse response = someThingApplication.doLogicSchedule(testRequest);
		log.info("EntryTest_someThingApplication_TestResponse1={} ", response);
		testRequest.setName("汤姆");
		response = someThingApplication.doLogicSchedule(testRequest);
		log.info("EntryTest_someThingApplication_TestResponse2={} ", response);
		Assert.assertNotNull(response);

		TestResponse testResponse = someThing2Application.doLogicSchedule(testRequest);
		log.info("EntryTest_someThing2Application_TestResponse2={} ", testResponse);
		Assert.assertNotNull(testResponse);
	}
}
