package com.yilun.gl.dof.excute.framework.application;

import com.gl.dof.core.excute.framework.LogicExecutor;
import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.content.TreeWrapper;
import com.gl.dof.core.excute.framework.context.HandleContext;
import com.gl.dof.core.excute.framework.entry.AbstractApplication;
import com.gl.dof.core.excute.framework.executor.tree.BasicApplication;
import com.gl.dof.core.excute.framework.executor.tree.TreeApplicationExecutor;
import com.yilun.gl.dof.excute.framework.domain.NameDoSvr;
import com.yilun.gl.dof.excute.framework.domain.*;
import com.yilun.gl.dof.excute.framework.domain.ResponseDoSvr;
import com.yilun.gl.dof.excute.framework.model.request.TestRequest;
import com.yilun.gl.dof.excute.framework.model.response.TestResponse;
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
public class SomeThing2Application extends AbstractApplication<TestRequest, TestResponse> {

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
