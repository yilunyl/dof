package com.yilun.gl.dof.excute.framework.other.fatigue;

/**
 * @ClassName: biz-dof FatiguePeriodTypeEnum
 * @Description: com.yilun.gl.dof.excute.framework.fatigue
 * @Author: 逸伦
 * @Date: 2023/2/21 22:35
 * @Version: 1.0
 */
public enum FatiguePeriodTypeEnum {

	MINUTE(1, "分钟"),
	HOUR(2, "小时"),
	DAY(3, "天"),
	WEEK(4, "周"),
	MONTH(5, "月"),
	QUARTER(6, "季度"),
	YEAR(7, "年"),
	;

	private final int code;
	private final String desc;

	private FatiguePeriodTypeEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	public static FatiguePeriodTypeEnum codeOf(int code) {
		FatiguePeriodTypeEnum[] arr = values();
		for (FatiguePeriodTypeEnum e : arr) {
			if (e.getCode() == code) {
				return e;
			}
		}
		return null;
	}
}
