package com.gl.dof.core.excute.framework.exception;

import lombok.Getter;

/**
 * ServiceException class
 *
 * @author gl
 * @date 2018/03/29
 */
@Getter
public class DofServiceException extends RuntimeException{
    /**
     * code 错误码
     */
    private int code;

    /**
     * message 错误描述
     */
    private String message;


    public static DofServiceException build(int code, String message){
        DofServiceException serviceException = new DofServiceException();
        serviceException.code = code;
        serviceException.message = message;
        return serviceException;
    }

    public static DofServiceException build(DofResCode dofResCode){
        DofServiceException serviceException = new DofServiceException();
        serviceException.code = dofResCode.getCode();
        serviceException.message = dofResCode.getMessage();
        return serviceException;
    }

    public static DofServiceException build(DofResCode dofResCode, String message){
        DofServiceException serviceException = new DofServiceException();
        serviceException.code = dofResCode.getCode();
        serviceException.message = message;
        return serviceException;
    }
}
