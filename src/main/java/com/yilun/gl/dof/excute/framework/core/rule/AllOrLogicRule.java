package com.yilun.gl.dof.excute.framework.core.rule;


import java.util.ArrayList;
import java.util.List;

/**
 * @description: 全或执行规则
 * @author: gule
 * @create: 2019-08-20 10:27
 **/
public class AllOrLogicRule<T> implements LogicRule<T> {

    List<LogicRule<T>> childRules = new ArrayList<>();

    /**
     * 全或matching
     *
     * @param context
     * @return
     */
    @Override
    public boolean matching(T context) {
        for (LogicRule<T> logicRule : childRules) {
            boolean childMatched = logicRule.matching(context);
            if (childMatched) {
                return true;
            }
        }
        return false;
    }


    /**
     * 添加规则
     *
     * @param logicRule
     * @return
     */
    public AllOrLogicRule<T> addChildRule(LogicRule<T> logicRule) {
        childRules.add(logicRule);
        return this;
    }


}
