package com.yilun.gl.dof.excute.framework.usage.config.impl;

import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.logic.DataFrontProcessor;
import com.yilun.gl.dof.excute.framework.usage.param.TestParam;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof Application1
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 22:03
 * @Version: 1.0
 */
@Component
public class Application1 implements DataFrontProcessor<TestParam> {


	@Override
	public LogicResult doLogic(TestParam context) {
		return null;
	}

}
