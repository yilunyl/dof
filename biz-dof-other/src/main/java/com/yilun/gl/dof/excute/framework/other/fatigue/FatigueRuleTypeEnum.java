package com.yilun.gl.dof.excute.framework.other.fatigue;

/**
 * @ClassName: biz-dof FatigueRuleTypeEnum
 * @Description: com.yilun.gl.dof.excute.framework.fatigue
 * @Author: 逸伦
 * @Date: 2023/2/21 22:38
 * @Version: 1.0
 */
public enum FatigueRuleTypeEnum {

	PURE_LIMIT(1, "单纯的限制次数，例如限制曝光等"),
	SPECIFY_BEHAVIOR_SILENT(2, "仅指定某项操作，不包含其他行为，导致的静默期/处理期"),
	CLICK_BEHAVIOR_SILENT(3, "基于点击行为，导致的静默期/处理期"),

	;

	private final int code;
	private final String desc;

	private FatigueRuleTypeEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	public static FatigueRuleTypeEnum codeOf(int code) {
		FatigueRuleTypeEnum[] arr = values();
		for (FatigueRuleTypeEnum e : arr) {
			if (e.getCode() == code) {
				return e;
			}
		}
		return null;
	}
}

