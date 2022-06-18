package com.yilun.gl.dof.excute.framework.usage.config;

import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.content.ListWrapper;
import com.yilun.gl.dof.excute.framework.core.executor.tree.BasicTreeLogicGroup;
import com.yilun.gl.dof.excute.framework.core.executor.tree.TreeLogicExecutor;
import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.usage.param.Test;
import lombok.extern.slf4j.Slf4j;
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

	@Bean(name = "createLogicExecutor")
	public LogicExecutor<Test> createLogicExecutor() {
		log.warn("init createLogicExecutor");
		return new TreeLogicExecutor<>(new BasicTreeLogicGroup<ContextData>() {

			@Override
			public void dataFrontProcessor(ListWrapper listWrapper) {

			}

			@Override
			public void dataProcessor(ListWrapper listWrapper) {

			}

			@Override
			public void dataPostProcessor(ListWrapper listWrapper) {

			}
		});
	}
}
