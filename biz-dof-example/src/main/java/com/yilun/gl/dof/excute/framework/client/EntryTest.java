package com.yilun.gl.dof.excute.framework.client;

import com.gl.dof.core.excute.framework.annotation.DofReference;
import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.HandleContext;
import com.gl.dof.core.excute.framework.entry.DofExecutor;
import com.gl.dof.core.excute.framework.entry.Input;
import com.gl.dof.core.excute.framework.entry.Output;
import com.yilun.gl.dof.excute.framework.application.SomeThingApplication;
import com.yilun.gl.dof.excute.framework.model.request.TestRequest;
import com.yilun.gl.dof.excute.framework.model.response.TestResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@DofReference(funcKey="test1",logicFlow ="[addressDoSvr,channelDoSvr]," +
			"grayDoSvr,libraDoSvr," +
			"[nameDoSvr,persionSelectDoSvr]," +
			"responseDoSvr, spaceDoSvr,strategyResponseDataDoSvr, tripDoSvr")
	private DofExecutor<TestRequest, TestResponse> test1;

	@DofReference(funcKey="test2",useCommonPool = false, corePoolSize = 20, logicFlow ="[addressDoSvr,channelDoSvr]," +
			"grayDoSvr,libraDoSvr," +
			"[nameDoSvr,persionSelectDoSvr]," +
			"responseDoSvr, spaceDoSvr,strategyResponseDataDoSvr, tripDoSvr")
	private DofExecutor<TestRequest, TestResponse> test2;

	@DofReference(funcKey="test3",logicFlow ="")
	private DofExecutor<TestRequest, TestResponse> test3;

	@GetMapping(path = "/get/test")
	public void entryTest(){
		TestRequest testRequest = new TestRequest();
		testRequest.setName("汤姆");
		TestResponse testResponse = test1.doLogicSchedule(new Input<TestRequest>() {
			@Override
			public void doIn(HandleContext ctx) {
				ctx.attr(TestRequest.class).set(testRequest);
			}
		}, new Output<TestResponse>() {
			@Override
			public TestResponse doOut(HandleContext ctx, LogicResult logicResult) {
				if(ctx.hasAttr(TestResponse.class)){
					return ctx.attr(TestResponse.class).get();
				}
				return null;
			}
		});
		log.info("EntryTest_someThing2Application_TestResponse2={} ", testResponse);
		Assert.assertNotNull(testResponse);
	}


	@GetMapping(path = "/get/test2")
	public void entryTest2(){
		TestRequest testRequest = new TestRequest();
		TestResponse testResponse = test2.doLogicSchedule(new Input<TestRequest>() {
			@Override
			public void doIn(HandleContext ctx) {
				ctx.attr(TestRequest.class).set(testRequest);
			}
		}, new Output<TestResponse>() {
			@Override
			public TestResponse doOut(HandleContext ctx, LogicResult logicResult) {
				if(ctx.hasAttr(TestResponse.class)){
					return ctx.attr(TestResponse.class).get();
				}
				return null;
			}
		});
		log.info("EntryTest_someThing2Application_TestResponse2={} ", testResponse);
		Assert.assertNotNull(testResponse);
	}
}
