package com.yilun.gl.dof.excute.framework.core.content;

import com.yilun.gl.dof.excute.framework.ddd.model.BaseParam;

/**
 * @Auther: gule.gl
 * @Date: 2021/7/23-07-23-下午4:33
 * @Description: com.amap.aos.infrastructure.framework.context
 * @version: 1.0
 */
public abstract class ContextData<REQ, RES>{

	private REQ request;

	private RES response;

	public ContextData(REQ request) {
		this.request = request;
	}

	public REQ getRequest() {
		return request;
	}

	public void setRequest(REQ request) {
		this.request = request;
	}

	public RES getResponse() {
		return response;
	}

	public void setResponse(RES response) {
		this.response = response;
	}
}
