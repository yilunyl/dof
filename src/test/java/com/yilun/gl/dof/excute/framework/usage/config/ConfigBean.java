package com.yilun.gl.dof.excute.framework.usage.config;

import com.yilun.gl.dof.excute.framework.core.content.ListWrapper;
import com.yilun.gl.dof.excute.framework.core.executor.tree.BasicTreeLogicGroup;
import com.yilun.gl.dof.excute.framework.core.executor.tree.TreeLogicExecutor;
import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.usage.impl.CarorderApplication;
import com.yilun.gl.dof.excute.framework.usage.impl.ChannelApplication;
import com.yilun.gl.dof.excute.framework.usage.impl.FeatureApplication;
import com.yilun.gl.dof.excute.framework.usage.impl.GrayApplication;
import com.yilun.gl.dof.excute.framework.usage.impl.LibraApplication;
import com.yilun.gl.dof.excute.framework.usage.impl.PersionSelectApplication;
import com.yilun.gl.dof.excute.framework.usage.impl.PredioctDesApplication;
import com.yilun.gl.dof.excute.framework.usage.impl.StrategyResponseDataApplication;
import com.yilun.gl.dof.excute.framework.usage.impl.TimeApplication;
import com.yilun.gl.dof.excute.framework.usage.impl.TripApplication;
import com.yilun.gl.dof.excute.framework.usage.context.TestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: biz-dof Init
 * @Description: com.yilun.gl.dof.excute.framework.usage
 * @Author: 逸伦
 * @Date: 2022/6/4 23:38
 * @Version: 1.0
 */
@Configuration
@Slf4j
public class ConfigBean {

	@Autowired
	private TimeApplication timeApplication;
	@Autowired
	private PredioctDesApplication predioctDesApplication;
	@Autowired
	private PersionSelectApplication persionSelectApplication;
    @Autowired
    private TripApplication tripApplication;
    @Autowired
    private FeatureApplication featureApplication;
    @Autowired
    private GrayApplication grayApplication;
    @Autowired
    private LibraApplication libraApplication;
    @Autowired
    private StrategyResponseDataApplication strategyResponseDataApplication;
    @Autowired
    private CarorderApplication carorderApplication;
    @Autowired
    private ChannelApplication channelApplication;

	@Bean(name = "cardLogicExecutor")
	public LogicExecutor<TestContext> cardLogicExecutor() {
		log.warn("卡片投放业务createLogicExecutor");
		return new TreeLogicExecutor<>(new BasicTreeLogicGroup<TestContext>() {
			@Override
			public void dataFrontProcessor(ListWrapper listWrapper) {
                //非io操作
                listWrapper.parallelAdd(grayApplication, timeApplication, channelApplication);
                //大头io操作
				listWrapper.parallelAdd(persionSelectApplication, predioctDesApplication);
			}
			@Override
			public void dataProcessor(ListWrapper listWrapper) {
                //其他纬度io操作
				listWrapper.parallelAdd(tripApplication, featureApplication, carorderApplication);
			}
			@Override
			public void dataPostProcessor(ListWrapper listWrapper) {
				listWrapper.add(libraApplication);
                //后置返回数据处理
                listWrapper.add(strategyResponseDataApplication);
			}
		});
	}
}
