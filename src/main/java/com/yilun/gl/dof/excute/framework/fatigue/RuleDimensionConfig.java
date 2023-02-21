package com.yilun.gl.dof.excute.framework.fatigue;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: biz-dof RuleDimensionConfig
 * @Description: com.yilun.gl.dof.excute.framework.fatigue
 * @Author: 逸伦
 * @Date: 2023/2/21 22:36
 * @Version: 1.0
 */
@Data
public class RuleDimensionConfig implements Serializable {

	/**
	 * id
	 */
	private String ruleId;

	/**
	 * 规则名字
	 */
	private String ruleName;

	/**
	 * 疲劳度控制类型
	 * @see FatigueRuleTypeEnum
	 */
	private Integer fatigueType;

	/**
	 * 时间id
	 */
	private String event;

	/**
	 * 展示位置
	 */
	private String position;

	/**
	 * 常规统计动作动作行为
	 * @see
	 */
	private String statisticalAction;

	/**
	 * 次数控制
	 */
	private RuleTimeConfig ruleTimeConfig;

	/**
	 * 例如连续曝光100次，没有点击关闭操作，则10天不展示，用来记录没有点击关闭操作的case
	 * todo 该字段的定义需要在确认下
	 */
	private List<String> excludeActions;
}
