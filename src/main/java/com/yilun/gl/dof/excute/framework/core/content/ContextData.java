package com.yilun.gl.dof.excute.framework.core.content;

import lombok.Data;

/**
 * @Auther: gule.gl
 * @Date: 2021/7/23-07-23-下午4:33
 * @Description: com.amap.aos.infrastructure.framework.context
 * @version: 1.0
 */
@Data
public abstract class ContextData<REQ, RES>{

	private REQ request;

	private RES response;

	protected ContextData(REQ request, RES response) {
		this.request = request;
		this.response = response;

		initContext(request, response);
	}

	protected abstract void initContext(REQ request, RES response);
}
