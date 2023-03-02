package com.gl.dof.excute.framework.base.util;

/**
 * @ClassName: biz-dof BaseResultCode
 * @Description: com.yilun.gl.dof.excute.framework.util
 * @Author: 逸伦
 * @Date: 2023/2/21 22:32
 * @Version: 1.0
 */
public interface BaseResultCode {
	/**
	 * 通用成功
	 */
	ResultCode SUCCESS = ResultCode.create("SUCCESS", "成功");
	/**
	 * 通用失败
	 */
	ResultCode FAIL = ResultCode.create("FAIL", "失败");
	/**
	 * 单元化切流预留CODE
	 */
	ResultCode WRITE_IS_FORBIDDEN = ResultCode.create("WRITE_IS_FORBIDDEN", "当前正在单元切流,处于禁写中");
	/**
	 * 单元化切流预留CODE
	 */
	ResultCode READ_IS_FORBIDDEN = ResultCode.create("READ_IS_FORBIDDEN", "当前正在单元切流,处于禁读中");
	/**
	 * 服务限流CODE
	 */
	ResultCode QPS_LIMIT = ResultCode.create("QPS_LIMIT", "服务限流");
	/**
	 * 服务未实现
	 */
	ResultCode NOT_IMPLEMENTED = ResultCode.create("NOT_IMPLEMENTED", "服务未实现");
	/**
	 * 超时
	 */
	ResultCode TIMEOUT = ResultCode.create("TIMEOUT", "超时");
	/**
	 * 服务异常
	 */
	ResultCode SYSTEM_ERROR = ResultCode.create("SYSTEM_ERROR", "服务异常");
	/**
	 * 参数错误
	 */
	ResultCode PARAM_ERROR = ResultCode.create("PARAM_ERROR", "参数错误");
	/**
	 * 校验失败/未命中灰度
	 */
	ResultCode CHECK_FAIL = ResultCode.create("CHECK_FAIL", "校验失败/未命中灰度");
	/**
	 * 重复调用
	 */
	ResultCode DUPLICATE_FAIL = ResultCode.create("DUPLICATE_FAIL", "重复调用");
	/**
	 * 不支持的服务调用方
	 */
	ResultCode NOT_SUPPORT_SERVICE_SOURCE = ResultCode.create("NOT_SUPPORT_SERVICE_SOURCE", "不支持的服务调用方");
}
