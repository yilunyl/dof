package com.yilun.gl.dof.excute.framework.util;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @ClassName: biz-dof ResultCode
 * @Description: com.yilun.gl.dof.excute.framework.util
 * @Author: 逸伦
 * @Date: 2023/2/21 22:30
 * @Version: 1.0
 */
@Data
public final class ResultCode implements Serializable {

	private static final long serialVersionUID = -3120636736734857509L;
	private final String code;
	private final String desc;

	private ResultCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static ResultCode create(String code, String desc) {
		return new ResultCode(code, desc);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }
		ResultCode that = (ResultCode)o;
		return Objects.equals(code, that.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}
}
