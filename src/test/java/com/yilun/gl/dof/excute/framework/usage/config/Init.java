package com.yilun.gl.dof.excute.framework.usage.config;

import com.yilun.gl.dof.excute.framework.core.content.ListWrapper;
import com.yilun.gl.dof.excute.framework.core.executor.tree.BasicTreeLogicGroup;
import com.yilun.gl.dof.excute.framework.core.executor.tree.TreeLogicExecutor;
import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.usage.config.impl.CarorderApplication;
import com.yilun.gl.dof.excute.framework.usage.config.impl.ChannelApplication;
import com.yilun.gl.dof.excute.framework.usage.config.impl.FeatureApplication;
import com.yilun.gl.dof.excute.framework.usage.config.impl.GrayApplication;
import com.yilun.gl.dof.excute.framework.usage.config.impl.LibraApplication;
import com.yilun.gl.dof.excute.framework.usage.config.impl.PersionSelectApplication;
import com.yilun.gl.dof.excute.framework.usage.config.impl.PredioctDesApplication;
import com.yilun.gl.dof.excute.framework.usage.config.impl.StrategyResponseDataApplication;
import com.yilun.gl.dof.excute.framework.usage.config.impl.TimeApplication;
import com.yilun.gl.dof.excute.framework.usage.config.impl.TripApplication;
import com.yilun.gl.dof.excute.framework.usage.param.TestParam;
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
public class Init {

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

	@Bean(name = "createLogicExecutor")
	public LogicExecutor<TestParam> createLogicExecutor() {
		log.warn("init createLogicExecutor");
		return new TreeLogicExecutor<>(new BasicTreeLogicGroup<TestParam>() {

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
