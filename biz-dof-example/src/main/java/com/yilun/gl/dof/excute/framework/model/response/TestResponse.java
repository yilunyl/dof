package com.yilun.gl.dof.excute.framework.model.response;

/**
 * @ClassName: biz-dof TestResponse
 * @Description: com.yilun.gl.dof.excute.framework.usage.entry
 * @Author: 逸伦
 * @Date: 2022/6/19 13:24
 * @Version: 1.0
 */
public class TestResponse {
	private String finalStringName;

	public String getFinalStringName() {
		return finalStringName;
	}

	public void setFinalStringName(String finalStringName) {
		this.finalStringName = finalStringName;
	}

	@Override
	public String toString() {
		return "TestResponse{" +
				"finalStringName='" + finalStringName + '\'' +
				'}';
	}
}
