package com.yilun.gl.dof.excute.framework.core.executor;

import com.google.common.collect.Lists;
import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.logic.ParrentLogicUnit;
import com.yilun.gl.dof.excute.framework.core.rule.LogicRule;
import com.yilun.gl.dof.excute.framework.core.rule.LogicRuleContainer;
import com.yilun.gl.dof.excute.framework.thread.ExecutorServiceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @description: 实现执行器类 支持异步处理任务
 * @author: gule
 * @create: 2019-08-19 21:02
 **/
@Slf4j
public class DefaultLogicExecutor<T extends ContextData> implements LogicExecutor<T> {

    private LinkedHashMap<String, List<ParrentLogicUnit<? extends ContextData>>> allLogic = new LinkedHashMap<>();


    public DefaultLogicExecutor(BasicLogicGroup<? extends ContextData> group){
        group.init();
        allLogic = group.getAllLogic();
    }

    /**
     * 自定义线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;


    @PostConstruct
    public void init() {
        threadPoolExecutor = ExecutorServiceWrapper.getThreadPoolExecutor(10, this.getClass().getSimpleName());
    }


    /**
     * @param context 容器上下文，使用方自己控制
     * @return LogicResult
     */
    @Override
    public LogicResult doLogicSchedule(T context) {
        StopWatch stopWatch = new StopWatch(this.getClass().getSimpleName());
        LinkedHashMap<String, List<ParrentLogicUnit<? extends ContextData>>> logicUnitTreeMap = allLogic;
        LogicResult.InvocationInfo invocationInfo = buildInvocationInfo();
        LogicResult logicResult = null;
        StringBuilder logTitle = new StringBuilder();
        StringBuilder logDetail = new StringBuilder(Thread.currentThread().getName() + "执行流程如下:");
        for (Map.Entry<String, List<ParrentLogicUnit<? extends ContextData>>> entry : logicUnitTreeMap.entrySet()) {
            stopWatch.start(entry.getKey());
            List<ParrentLogicUnit<? extends ContextData>> availableentryLogics =  filterAvailableEntryLogic(entry.getValue(), logDetail, context);
            String  simpleName = "";
            if (CollectionUtils.isEmpty(availableentryLogics)) {
                continue;
            }else if(availableentryLogics.size() == 1){
                ParrentLogicUnit logicUnit = availableentryLogics.get(0);
                logicResult = doHandleSerialLogic(logicUnit, context, invocationInfo);
                simpleName = logicUnit.getClass().getSimpleName();
            }else{
                Pair<LogicResult, String> logicResultStringPair = doHandleParallelLogic(availableentryLogics, context, invocationInfo);
                logicResult = logicResultStringPair.getLeft();
                simpleName = "并行处理任务数量=" + availableentryLogics.size() + "\t" + logicResultStringPair.getRight();
            }
            stopWatch.stop();
            //处理执行结果
            boolean resultIsSuccess = logicResult.isSuccess();
            logDetail.append("\n耗时:").append(formaatExecuteTime(stopWatch.getLastTaskTimeMillis()))
                    .append("\t执行结果:").append(resultIsSuccess)
                    .append("\tLogicUnit:").append(simpleName)
            ;
            if (!resultIsSuccess) {
                logDetail.append("\t异常:").append("code: ").append(logicResult.getCode()).append(" ").append(logicResult.getMessage()).append("\ncontext:").append(context);
                logTitle.append("流程执行失败,失败执行单元:").append(simpleName).append("\t失败原因:").append("code: ").append(logicResult.getCode()).append(" ").append(logicResult.getMessage());
                break;
            }
        }
        log.info("执行的logic: \n " + logDetail + "\n 总耗时: " + stopWatch.getTotalTimeMillis());
        if(logTitle.length() > 0){
            //有失败异常
            log.info(logTitle.toString());
        }
        assertResultNotNull(logicResult);
        logicResult.setInvocationInfo(invocationInfo);
        rollBackIfNeed(logicResult, context);
        return logicResult;
    }

    private List<ParrentLogicUnit<? extends ContextData>> filterAvailableEntryLogic(List<ParrentLogicUnit<? extends ContextData>> value, StringBuilder logDetail, T context) {
        if(CollectionUtils.isEmpty(value)){
            return Lists.newArrayList();
        }
        List<ParrentLogicUnit<? extends ContextData>> collect = value.stream().filter(logicUnit -> {
            if (!isMatching(context, logicUnit)) {
                logDetail.append("\nLogicUnit:").append(logicUnit.getClass().getSimpleName()).append("\t被规则过滤,不执行规则");
                return Boolean.FALSE;
            }else{
                return Boolean.TRUE;
            }
        }).collect(Collectors.toList());

        return collect;
    }


    private Pair<LogicResult, String> doHandleParallelLogic(List<ParrentLogicUnit<? extends ContextData>> availableentryLogics, T context ,LogicResult.InvocationInfo invocationInfo){
        //需要并行处理
        List<CompletableFuture<LogicResult>> futureList = new ArrayList<>();
        StringBuffer logThreadDetail = new StringBuffer("并行执行过程如下:");
        for(ParrentLogicUnit<? extends ContextData> parrentLogicUnit : availableentryLogics){
            futureList.add(CompletableFuture.supplyAsync(() -> {
                ParrentLogicUnit parrentLogicUnitTmp = parrentLogicUnit;
                LogicResult logicResultThread;
                StopWatch stopWatch = new StopWatch();
                try {
                    stopWatch.start(parrentLogicUnitTmp.getClass().getSimpleName());
                    logicResultThread = parrentLogicUnitTmp.doLogic(context);
                    stopWatch.stop();
                } catch (Exception e) {
                    logicResultThread = LogicResult.createException(e);
                    stopWatch.stop();
                }
                StringBuilder log = new StringBuilder();
                log.append("\n\t\t\t\t\t\t").append(Thread.currentThread().getName()).append("\t耗时:").append(formaatExecuteTime(stopWatch.getLastTaskTimeMillis()))
                        .append("\t执行结果:").append(logicResultThread.getResult())
                        .append("\tLogicUnit:").append(parrentLogicUnitTmp.getClass().getSimpleName());
                logThreadDetail.append(log);
                //添加构执行现场
                invocationInfo.addInvocationInfo(logicResultThread, parrentLogicUnitTmp);
                return logicResultThread;
            }, threadPoolExecutor));
        }
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[1]));
        // 阻塞等待
        completableFuture.join();
        List<LogicResult> logicResultList = new ArrayList<>();
        futureList.forEach(item -> logicResultList.add(item.join()));

        //判断所有的并行流程是否都执行成功
        //找到第一个不成功的
        Optional<LogicResult> first = logicResultList.stream().filter(logicResult -> !logicResult.isSuccess()).findFirst();
        LogicResult logicResult = first.orElseGet(LogicResult::createSuccess);

        return Pair.of(logicResult, logThreadDetail.toString());
    }

    private LogicResult doHandleSerialLogic(ParrentLogicUnit<? extends ContextData> logicUnit,  T context, LogicResult.InvocationInfo invocationInfo){
        StopWatch stopWatch = new StopWatch();
        LogicResult logicResult;
        try {
            ParrentLogicUnit parrentLogicUnitTmp = logicUnit;
            stopWatch.start(parrentLogicUnitTmp.getClass().getSimpleName());
            logicResult = parrentLogicUnitTmp.doLogic(context);
            stopWatch.stop();
            invocationInfo.addInvocationInfo(logicResult, parrentLogicUnitTmp);
        } catch (Exception e) {
            logicResult = LogicResult.createException(e);
            stopWatch.stop();
        }
        return logicResult;
    }

    private void assertResultNotNull(LogicResult logicResult) {
        if (logicResult == null) {
            log.warn("logicResult为空");
            throw new NullPointerException();
        }
    }

    private boolean isMatching(T context, ParrentLogicUnit<? extends ContextData> unit) {
        LogicRuleContainer logicRuleContainer = LogicRuleContainer.getInstance();
        LogicRule logicRule = logicRuleContainer.getLogicRule(unit);
        boolean mapping = logicRule.matching(context);
        return logicRule == LogicRule.NO_RULE || mapping;
    }

    /**
     * 在打印日志的时候使得消耗的时间的位数是对齐的，例如
     * 耗时: 200
     * 耗时: 002
     * @param time 时间
     * @return 输出时间
     */
    private static String formaatExecuteTime(long time){
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumIntegerDigits(6);
        nf.setGroupingUsed(false);
        return nf.format(time);
    }

    private LogicResult.InvocationInfo buildInvocationInfo() {
        LogicResult.InvocationInfo invocationInfo = new LogicResult.InvocationInfo();
        invocationInfo.setFailList(new Vector<>());
        invocationInfo.setSuccessList(new Vector<>());
        return invocationInfo;
    }

    private void rollBackIfNeed(LogicResult logicResult, T context) {
        if (!logicResult.isSuccess()) {
            List<ParrentLogicUnit> reverseLogicUnits = logicResult.getInvocationInfo().getSuccessList();
            if (CollectionUtils.isEmpty(reverseLogicUnits)) {
                return;
            }
            for (ParrentLogicUnit logicUnit : reverseLogicUnits) {
                try {
                    log.warn("流程异常，逻辑reverse:{}", logicUnit.getClass().getSimpleName());
                    logicUnit.reverse(context, logicResult);
                } catch (Exception e) {
                    log.error("roll_back_logic:{} fail,context:{}", logicUnit.getClass().getSimpleName(), context, e);
                }
            }
        }
    }
}
