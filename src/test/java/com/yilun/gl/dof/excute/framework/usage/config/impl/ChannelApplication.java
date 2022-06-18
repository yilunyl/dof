package com.yilun.gl.dof.excute.framework.usage.config.impl;

import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.logic.DataProcessor;
import com.yilun.gl.dof.excute.framework.usage.param.TestParam;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof CarorderApplication
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 23:48
 * @Version: 1.0
 */
@Component
public class ChannelApplication implements DataProcessor<TestParam> {
	@Override
	public LogicResult doLogic(TestParam context) {
		return null;
	}
}
