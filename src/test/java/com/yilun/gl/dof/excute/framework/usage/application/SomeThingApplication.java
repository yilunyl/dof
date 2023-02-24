package com.yilun.gl.dof.excute.framework.usage.application;

import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.content.TreeWrapper;
import com.yilun.gl.dof.excute.framework.core.context.HandleContext;
import com.yilun.gl.dof.excute.framework.core.context.attribute.AttributeKey;
import com.yilun.gl.dof.excute.framework.core.entry.AbstractApplication;
import com.yilun.gl.dof.excute.framework.core.executor.tree.BasicApplication;
import com.yilun.gl.dof.excute.framework.core.executor.tree.TreeApplicationExecutor;
import com.yilun.gl.dof.excute.framework.usage.domain.CarorderDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.ChannelDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.FeatureDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.GrayDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.LibraDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.PersionSelectDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.PredioctDesDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.StrategyResponseDataDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.TimeDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.TripDoSvr;
import com.yilun.gl.dof.excute.framework.usage.model.request.TestRequest;
import com.yilun.gl.dof.excute.framework.usage.model.response.TestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof Init
 * @Description: com.yilun.gl.dof.excute.framework.usage
 * @Author: 逸伦
 * @Date: 2022/6/4 23:38
 * @Version: 1.0
 */
@Component
@Slf4j
public class SomeThingApplication extends AbstractApplication<TestRequest, TestResponse> {

	@Autowired
	private TimeDoSvr timeDoSvr;
	@Autowired
	private PredioctDesDoSvr predioctDesDoSvr;
	@Autowired
	private PersionSelectDoSvr persionSelectDoSvr;
    @Autowired
    private TripDoSvr tripDoSvr;
    @Autowired
    private FeatureDoSvr featureDoSvr;
    @Autowired
    private GrayDoSvr grayDoSvr;
    @Autowired
    private LibraDoSvr libraDoSvr;
    @Autowired
    private StrategyResponseDataDoSvr strategyResponseDataDoSvr;
    @Autowired
    private CarorderDoSvr carorderDoSvr;
    @Autowired
    private ChannelDoSvr channelDoSvr;

	@Override
	public LogicExecutor initDoSvrGroup() {
		log.warn("卡片投放业务createLogicExecutor");
		return new TreeApplicationExecutor(new BasicApplication() {
			@Override
			protected void init(TreeWrapper treeWrapper) {
				//非io操作
				treeWrapper.parallelAdd(grayDoSvr, timeDoSvr, channelDoSvr);
				//大头io操作
				treeWrapper.parallelAdd(persionSelectDoSvr, predioctDesDoSvr);
				//其他纬度io操作
				treeWrapper.parallelAdd(tripDoSvr, featureDoSvr, carorderDoSvr);
				//
				treeWrapper.add(libraDoSvr);
				//后置返回数据处理
				treeWrapper.add(strategyResponseDataDoSvr);
			}
		});
	}


	@Override
	protected void initContext(HandleContext ctx, TestRequest testRequest, Object... others) {
		AttributeKey<TestRequest> originRequestKey = AttributeKey.newInstance("originRequest");
		ctx.attr(originRequestKey).set(testRequest);
		log.info("SomeThingApplication success");
	}

	@Override
	protected TestResponse buildResponse(LogicResult logicResult, HandleContext ctx) {
		AttributeKey<TestRequest> oriKey = AttributeKey.valueOf("originRequest");
		boolean hasAttr = ctx.hasAttr(oriKey);
		if(!hasAttr){
			return null;
		}
		TestRequest testRequest2 = ctx.attr(oriKey).get();
		TestResponse testResponse = new TestResponse();
		testResponse.setFinalStringName(testRequest2.getName());
		return testResponse;
	}
}
