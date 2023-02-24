package com.yilun.gl.dof.excute.framework.core.rule;

import com.yilun.gl.dof.excute.framework.core.context.HandleContext;

import java.util.Objects;

/**
 * @description: 规则
 * @author: gule
 * @create: 2019-08-20 10:12
 **/
public interface LogicRule {


    /**
     * 无规则
     */
    LogicRule NO_RULE = new LogicRule() {
        @Override
        public boolean matching(HandleContext context) {
            return false;
        }

    };


    /**
     * 规则是否匹配
     *
     * @param context
     * @return
     */
    boolean matching(HandleContext context);


    /**
     * 创建or规则
     *
     * @param other
     * @return
     */
    default LogicRule or(LogicRule other) {
        Objects.requireNonNull(other);
        return (t) -> matching(t) || other.matching(t);
    }


    /**
     * 创建and规则
     *
     * @param other
     * @return
     */
    default LogicRule and(LogicRule other) {
        Objects.requireNonNull(other);
        return (t) -> matching(t) && other.matching(t);
    }


}
