package com.yilun.gl.dof.excute.framework.domain;

import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.HandleContext;
import com.gl.dof.core.excute.framework.logic.DomainService;
import org.springframework.stereotype.Component;

/**
 * @ClassName: biz-dof CarorderApplication
 * @Description: com.yilun.gl.dof.excute.framework.usage.config.impl
 * @Author: 逸伦
 * @Date: 2022/6/18 23:48
 * @Version: 1.0
 */
@Component
public class AddressDoSvr implements DomainService {

	public static String addressKey = "address";
	public static String addressKey2 = "address2";
	@Override
	public boolean isMatch(HandleContext context) {
		return true;
	}
	@Override
	public LogicResult doLogic(HandleContext context) {

		String address = "北京市海淀区";
		String address2 = "北京市海淀区quququq";

		context.attr(String.class, addressKey).set(address);
		context.attr(String.class, addressKey2).set(address2);
		return LogicResult.createSuccess();
	}
}
