package com.yilun.gl.dof.excute.framework.core.rule;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 全与规则
 * @author: gule
 * @create: 2019-08-20 10:38
 **/
public class AllAndLogicRule<T> implements LogicRule<T> {


    List<LogicRule<T>> childRules = new ArrayList<>();


    /**
     * 全与matching
     *
     * @param context
     * @return
     */
    @Override
    public boolean matching(T context) {
        for (LogicRule<T> logicRule : childRules) {
            boolean childMatched = logicRule.matching(context);
            if (!childMatched) {
                return false;
            }
        }
        return true;
    }


    /**
     * 添加子规则
     *
     * @param logicRule
     * @return
     */
    public AllAndLogicRule<T> addChild(LogicRule<T> logicRule) {
        childRules.add(logicRule);
        return this;
    }
}
