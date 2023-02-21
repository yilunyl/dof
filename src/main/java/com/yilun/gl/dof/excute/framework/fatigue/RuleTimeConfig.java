package com.yilun.gl.dof.excute.framework.fatigue;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: biz-dof RuleTimeConfig
 * @Description: com.yilun.gl.dof.excute.framework.fatigue
 * @Author: 逸伦
 * @Date: 2023/2/21 22:34
 * @Version: 1.0
 */
@Data
public class RuleTimeConfig implements Serializable {

	/**
	 * 统计周期周期
	 */
	private Integer statisticalPeriod;

	/**
	 * 单位 目前有 分 小时 天 等
	 * @see
	 */
	private Integer periodUnit;

	/**
	 * 次数
	 */
	private Long frequencyThreshold;

	/**
	 * 处理周期 在 XXX天不展示的时候 会用到该字段
	 */
	private Integer processingPeriod;
}
