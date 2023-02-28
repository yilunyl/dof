package com.yilun.gl.dof.excute.framework.other.constants;

/**
 * ApiConstant class
 *
 * @author gl
 * @date 2018/04/01
 */
public class ApiConstant {
    /**
     * 响应code key
     */
    public static final String KEY_CODE = "code";
    /**
     * 响应message key
     */
    public static final String KEY_MESSAGE = "message";

    /**
     * 响应data key
     */
    public static final String KEY_DATA = "data";

    /**
     * 响应成功code
     */
    public static final int CODE_SUCCESS = 0;

    /**
     * 响应失败code
     */
    public static final int CODE_FAIL = -1;

    /**
     * 值已存在
     */
    public static final int CODE_EXSITS = -2;
    /**
     * 响应成功message
     */
    public static final String MESSAGE_SUCCESS = "成功";

    /**
     * 响应失败message
     */
    public static final String MESSAGE_FAIL = "失败";

    /**
     * 服务器内部错误code
     */
    public static final int CODE_FAIL_INTERNAL = 500;

    /**
     * 服务器内部错误message
     */
    public static final String MESSAGE_FAIL_INTERNAL = "服务器内部错误";

    /**
     * 值已存在
     */
    public static final String ALREADY_EXSITS = "已经存在";
    /**
     * token验证失败错误message
     */
    public static final int CODE_TOKEN_CHECK_FAIL = 999;

    /**
     * token验证失败message
     */
    public static final String MESSAGE_TOKEN_CHECK_FAIL = "token校验失败";

    /**
     * 当前登录人角色验证失败
     */
    public static final int CODE_CURRENT_ROLE_CHECK_FAIL = 998;

    /**
     * 当前登录人角色验证失败message
     */
    public static final String MESSAGE_CURRENT_ROLE_CHECK_FAIL = "当前登录人角色校验失败";

    /**
     * 点击过快code
     */
    public static final int CODE_CLICK_TOO_QUICK_FAIL = 991;

    /**
     * 点击过快message
     */
    public static final String MESSAGE_CLICK_TOO_QUICK_FAIL = "操作太快了,请稍后重试...";
    /**
     * 工单当前状态下不允许此操作code
     */
    public static final int CODE_STATUS_CHECK_FAIL = 800;
    /**
     * 工单当前状态不允许此操作message
     */
    public static final String MESSAGE_STATUS_CHECK_FAIL = "订单当前状态不允许此操作";

    /**
     * 业务错误码---不需要打印error级别日志的code值（仅供提示给客户端）
     */
    public static final int CODE_SUGGEST_FAIL = -3;

    /**
     * 客户端对话框提示编码
     */
    public static final Integer TIP_CODE = 1;
    /**
     * 第三方调用参数错误的code码
     */
    public static final int EXTERNAL_FAIL_CODE = 400;
    /**
     * 第三方调用错误的提示信息
     */
    public static final String EXTERNAL_FAIL_KEY_SERCET_NOTNULL = "key,secert请求参数不能为空";

    public static final String EXTERNAL_FAIL_KEY_SERCET_ERROR = "key,secert请求参数错误";

    public static final String EXTERNAL_FAIL_SIGNATURE_NOTNULL = "请求签名不能为空";

    public static final String EXTERNAL_FAIL_SIGNATURE_ERROR = "请求签名错误";

    public static final String USER_ID_NOT_EXSIT = "对应人员不存在";

    public static final String STORE_ID_NOT_EXIST = "对应整备店不存在";

    public static final String STORE_ORDER_PREFIX = "SO";
}
