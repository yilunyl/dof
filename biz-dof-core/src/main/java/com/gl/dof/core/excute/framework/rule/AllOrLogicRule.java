package com.gl.dof.core.excute.framework.rule;


import com.gl.dof.core.excute.framework.context.HandleContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 全或执行规则
 * @author: gule
 * @create: 2019-08-20 10:27
 **/
public class AllOrLogicRule implements LogicRule {

    List<LogicRule> childRules = new ArrayList<>();

    /**
     * 全或matching
     *
     * @param context
     * @return
     */
    @Override
    public boolean matching(HandleContext context) {
        for (LogicRule logicRule : childRules) {
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
    public AllOrLogicRule addChildRule(LogicRule logicRule) {
        childRules.add(logicRule);
        return this;
    }


}
