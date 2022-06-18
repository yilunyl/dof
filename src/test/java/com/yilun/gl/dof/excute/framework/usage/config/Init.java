package com.yilun.gl.dof.excute.framework.usage.config;

import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.content.ListWrapper;
import com.yilun.gl.dof.excute.framework.core.executor.tree.BasicTreeLogicGroup;
import com.yilun.gl.dof.excute.framework.core.executor.tree.TreeLogicExecutor;
import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.usage.config.impl.Application1;
import com.yilun.gl.dof.excute.framework.usage.config.impl.Application2;
import com.yilun.gl.dof.excute.framework.usage.config.impl.Application3;
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
	private Application3 application3;
	@Autowired
	private Application2 application2;
	@Autowired
	private Application1 application1;

	@Bean(name = "createLogicExecutor")
	public LogicExecutor<TestParam> createLogicExecutor() {
		log.warn("init createLogicExecutor");
		return new TreeLogicExecutor<>(new BasicTreeLogicGroup<TestParam>() {

			@Override
			public void dataFrontProcessor(ListWrapper listWrapper) {
				listWrapper.parallelAdd(application1, application2);
			}

			@Override
			public void dataProcessor(ListWrapper listWrapper) {
				listWrapper.add(application2);
			}

			@Override
			public void dataPostProcessor(ListWrapper listWrapper) {
				listWrapper.add(application3);
			}
		});
	}
}
