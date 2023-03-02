package com.gl.dof.excute.framework.base.util;


import java.io.Serializable;
import java.util.Objects;

/**
 * @see
 * 区别于 FuncRet  不同点在于对error的msg处理上，FuncRetWrapper并不会拼接ip信息
 * @param <T>
 */
public class FuncRetWrapper<T> implements Serializable {
    private static final long serialVersionUID = -8256160551756528882L;
    private ResultCode resultCode;
    private String message;
    private T data;

    public FuncRetWrapper() {
    }

    public static <T> FuncRetWrapper<T> success(T data) {
        FuncRetWrapper<T> result = new FuncRetWrapper<>();
        result.data = data;
        result.resultCode = BaseResultCode.SUCCESS;
        return result;
    }

    public static <T> FuncRetWrapper<T> success(T data, String messageFormat, Object... args) {
        FuncRetWrapper<T> result = new FuncRetWrapper<>();
        result.data = data;
        result.resultCode = BaseResultCode.SUCCESS;
        result.message = String.format(messageFormat, args);
        return result;
    }

    public static <T> FuncRetWrapper<T> error(ResultCode resultCode) {
        return error(resultCode, resultCode.getDesc());
    }

    public static <T> FuncRetWrapper<T> error(String messageFormat, Object... args) {
        return error(BaseResultCode.SYSTEM_ERROR, messageFormat, args);
    }

    public static <T> FuncRetWrapper<T> error(ResultCode resultCode, String messageFormat, Object... args) {
        FuncRetWrapper<T> result = new FuncRetWrapper<>();
        result.resultCode = resultCode;
        result.message = String.format(messageFormat, args);
        return result;
    }

    public static <T> FuncRetWrapper<T> error(ResultCode resultCode, String message, T data) {
        FuncRetWrapper<T> result = new FuncRetWrapper<>();
        result.data = data;
        result.resultCode = resultCode;
        result.message = String.format(message);
        return result;
    }

    public static <T> FuncRetWrapper<T> error(FuncRetWrapper<?> failResult) {
        FuncRetWrapper<T> result = new FuncRetWrapper<>();
        result.resultCode = failResult.getResultCode();
        result.message = failResult.getMessage();
        return result;
    }

    public boolean isSuccess() {
        return BaseResultCode.SUCCESS.equals(this.resultCode);
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public ResultCode getResultCode() {
        return this.resultCode;
    }

//    public String toString() {
//        return JsonUtils.toJson(this);
//    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            FuncRetWrapper<?> result = (FuncRetWrapper)o;
            return Objects.equals(this.resultCode, result.resultCode) && Objects.equals(this.data, result.data);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.resultCode, this.data});
    }


}
