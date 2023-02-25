package com.yilun.gl.dof.excute.framework.usage.application;

import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.content.TreeWrapper;
import com.yilun.gl.dof.excute.framework.core.context.HandleContext;
import com.yilun.gl.dof.excute.framework.core.entry.AbstractApplication;
import com.yilun.gl.dof.excute.framework.core.executor.tree.BasicApplication;
import com.yilun.gl.dof.excute.framework.core.executor.tree.TreeApplicationExecutor;
import com.yilun.gl.dof.excute.framework.usage.domain.AddressDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.ChannelDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.AgeDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.GrayDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.LibraDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.NameDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.PersionSelectDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.ResponseDoSvr;
import com.yilun.gl.dof.excute.framework.usage.domain.StrategyResponseDataDoSvr;
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
	private ResponseDoSvr responseDoSvr;
	@Autowired
	private NameDoSvr predioctDesDoSvr;
	@Autowired
	private PersionSelectDoSvr persionSelectDoSvr;
    @Autowired
    private TripDoSvr tripDoSvr;
    @Autowired
    private AgeDoSvr featureDoSvr;
    @Autowired
    private GrayDoSvr grayDoSvr;
    @Autowired
    private LibraDoSvr libraDoSvr;
    @Autowired
    private StrategyResponseDataDoSvr strategyResponseDataDoSvr;
    @Autowired
    private AddressDoSvr carorderDoSvr;
    @Autowired
    private ChannelDoSvr channelDoSvr;

	@Override
	public LogicExecutor initDoSvrGroup() {
		log.warn("SomeThing业务createLogicExecutor");
		return new TreeApplicationExecutor(new BasicApplication() {
			@Override
			protected void init(TreeWrapper treeWrapper) {
				//非io操作
				treeWrapper.parallelAdd(grayDoSvr, channelDoSvr);
				//大头io操作
				treeWrapper.parallelAdd(persionSelectDoSvr, predioctDesDoSvr);
				//其他纬度io操作
				treeWrapper.parallelAdd(tripDoSvr, featureDoSvr, carorderDoSvr);
				//
				treeWrapper.parallelAdd(libraDoSvr, responseDoSvr);
				//后置返回数据处理
				treeWrapper.add(strategyResponseDataDoSvr);
			}
		});
	}


	@Override
	protected void initContext(HandleContext ctx, TestRequest testRequest, Object... others) {
		ctx.attr(TestRequest.class).set(testRequest);
		log.info("SomeThingApplication success");
	}

	@Override
	protected TestResponse buildResponse(LogicResult logicResult, HandleContext ctx) {
		boolean hasAttr = ctx.hasAttr(TestResponse.class);
		if(!hasAttr){
			return null;
		}
		return ctx.attr(TestResponse.class).get();
	}
}
