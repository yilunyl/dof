package com.yilun.gl.dof.excute.framework.fatigue.check;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: biz-dof FatigueCheckRequest
 * @Description: com.yilun.gl.dof.excute.framework.fatigue.check
 * @Author: 逸伦
 * @Date: 2023/2/21 22:40
 * @Version: 1.0
 */
@Data
public class FatigueCheckRequest implements Serializable {

	/**
	 * 调用方 建议aone对应的名字
	 */
	private String ownerId;

	/**
	 * 设备id-adiu 再有adiu的场景下 优先使用adiu
	 */
	private String adiu;

	/**
	 * 设备id-tid 兼容小程序渠道
	 */
	private String tid;

	@Deprecated
	private String uid;
	/**
	 * 可参考如下枚举
	 * @see
	 */
	private String event;

	/**
	 * 位置id  暂定 需要和客户端确认
	 */
	private String position;

	/**
	 * 校验疲劳度参数时 该参数可不用
	 * @see
	 */
	@Deprecated
	private String action;

	/**
	 * 变更时间 默认为当前时间,单位秒
	 */
	private Long  fatigueCheckTime = System.currentTimeMillis() / 1000;

}
