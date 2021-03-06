package com.yilun.gl.dof.excute.framework.core.common;

import com.yilun.gl.dof.excute.framework.core.executor.tree.BasicTreeLogicGroup;
import com.yilun.gl.dof.excute.framework.core.logic.ParrentLogicUnit;
import com.yilun.gl.dof.excute.framework.exception.DofResCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 执行结果
 * @author: gule
 * @create: 2019-08-18 11:37
 **/

@Data
@Slf4j
public class LogicResult {

    private final static int CODE_SUCCESS = 200;

    private final static int CODE_REPEAT = 300;

    private final static int CODE_EXCEPTION = 500;


    private InvocationInfo invocationInfo;


    /**
     * 执行结果枚举
     */
    public enum RESULT {
        /**
         * 成功
         */
        SUCCESS,

        /**
         * 重复
         */
        REPEAT,
        /**
         * 失败
         */
        FAILED,
        /**
         * 异常
         */
        EXCEPTION,

        /**
         * 不匹配
         */
        UNMATCHED
    }

    /**
     * 执行结果
     */
    private RESULT result;
    /**
     * 执行失败文案
     */
    private String message;

    /**
     * 执行结果码
     */
    private Exception exception;

    /**
     * 执行code
     */
    private Integer code;

    /**
     * 执行异常
     */
    private DofResCode dofResCode;

    /**
     * 执行器现场
     */
    @Data
    public static class InvocationInfo {

        /**
         * 执行单元组
         */
        private BasicTreeLogicGroup logicGroup;
        /**
         * 最后一个执行单元
         */
        private ParrentLogicUnit last = null;

        /**
         * 执行成功列表
         */
        private List<ParrentLogicUnit> successList = new ArrayList<>();

        /**
         * 执行失败列表
         */
        private List<ParrentLogicUnit> failList = new ArrayList<>();

        /**
         * 添加执行现场
         * @param logicResultThread 执行结果
         * @param parrentLogicUnitTmp 执行单位
         */
        public synchronized void addInvocationInfo(LogicResult logicResultThread, ParrentLogicUnit parrentLogicUnitTmp){
            if(logicResultThread.isSuccess()){
                this.getSuccessList().add(parrentLogicUnitTmp);
            }else{
                this.getFailList().add(parrentLogicUnitTmp);
            }
        }
    }

    /**
     * 是否执行成功
     *
     * @return
     */
    public boolean isSuccess() {
        return RESULT.SUCCESS == getResult() || RESULT.REPEAT == getResult();
    }

    /**
     * 执行成功结果
     *
     * @return
     */
    public static LogicResult createSuccess() {
        LogicResult result = new LogicResult();
        result.setResult(RESULT.SUCCESS);
        result.setCode(CODE_SUCCESS);
        return result;
    }


    /**
     * 执行成功结果
     *
     * @return
     */
    public static LogicResult createRepeat() {
        LogicResult result = new LogicResult();
        result.setResult(RESULT.REPEAT);
        result.setCode(CODE_REPEAT);
        return result;
    }

    /**
     * 执行异常结果
     *
     * @return
     */
    @Deprecated
    public static LogicResult createException(Exception e) {
        log.info("发生异常:",e);
        LogicResult result = new LogicResult();
        result.setException(e);
        String exceptionMsg = e.getMessage();
        result.setCode(CODE_EXCEPTION);
        result.setResult(RESULT.EXCEPTION);
        result.setMessage(StringUtils.isEmpty(exceptionMsg) ? "发生异常" : exceptionMsg);
        return result;
    }

    /**
     * 执行失败结果
     *
     * @return
     */
    public static LogicResult createFailResult(DofResCode errorCode) {
        LogicResult result = new LogicResult();
        result.setResult(RESULT.FAILED);
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());
        result.setDofResCode(errorCode);
        return result;
    }

    public static LogicResult createUnMatchedResult() {
        LogicResult result = new LogicResult();
        result.setResult(RESULT.UNMATCHED);
        result.setCode(CODE_SUCCESS);
        result.setMessage("unMatched_logic,success");
        result.setDofResCode(null);
        return result;
    }
    /**
     * 执行失败结果
     *
     * @return
     */
    public static LogicResult createFailResult(DofResCode errorCode, String useOtherMsg) {
        LogicResult result = new LogicResult();
        result.setResult(RESULT.FAILED);
        result.setCode(errorCode.getCode());
        if(StringUtils.isEmpty(useOtherMsg)){
            useOtherMsg = errorCode.getMessage();
        }
        result.setMessage(useOtherMsg);
        result.setDofResCode(errorCode);
        return result;
    }
}
