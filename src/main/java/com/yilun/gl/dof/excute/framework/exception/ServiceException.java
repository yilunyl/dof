package com.yilun.gl.dof.excute.framework.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ServiceException class
 *
 * @author gl
 * @date 2018/03/29
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class ServiceException extends RuntimeException{
    /**
     * code 错误码
     */
    private final int code;

    /**
     * message 错误描述
     */
    private final String message;
}
