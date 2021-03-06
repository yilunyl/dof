package com.yilun.gl.dof.excute.framework.core.rule;

import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.logic.ParrentLogicUnit;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 规则容器
 * @author: gule
 * @create: 2019-08-19 21:23
 **/
public final class LogicRuleContainer<T extends ContextData> {

    private volatile static LogicRuleContainer instance;
    private static final Object LOCK = new Object();

    /**
     * 缓存执行单元->执行规则
     */
    private ConcurrentHashMap<ParrentLogicUnit<T>, LogicRule<T>> logicUnitToRuleMap = new ConcurrentHashMap<>();


    private LogicRuleContainer() {

    }

    /**
     * 获取规则容器
     *
     * @return LogicRuleContainer
     */
    public static LogicRuleContainer getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (LOCK) {
                if (Objects.isNull(instance)) {
                    instance = new LogicRuleContainer();
                }
            }
        }
        return instance;
    }

    /**
     * 给最小执行单元注册执行规则
     *
     * @param logicUnit
     * @param logicRule
     */
    public void registerRule(ParrentLogicUnit<T> logicUnit, LogicRule<T> logicRule) {
        logicUnitToRuleMap.put(logicUnit, logicRule);
    }


    /**
     * 获取logicRule
     *
     * @param logicUnit
     * @return
     */
    public LogicRule getLogicRule(ParrentLogicUnit logicUnit) {
        LogicRule logicRuleCache = logicUnitToRuleMap.get(logicUnit);
        return logicRuleCache == null ? LogicRule.NO_RULE : logicRuleCache;

    }



}
