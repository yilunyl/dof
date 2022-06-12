package com.yilun.gl.dof.excute.framework.core.rule;

import java.util.Objects;

/**
 * @description: 规则
 * @author: gule
 * @create: 2019-08-20 10:12
 **/
public interface LogicRule<T> {


    /**
     * 无规则
     */
    LogicRule NO_RULE = new LogicRule() {
        @Override
        public boolean matching(Object context) {
            return true;
        }
    };


    /**
     * 规则是否匹配
     *
     * @param context
     * @return
     */
    boolean matching(T context);


    /**
     * 创建or规则
     *
     * @param other
     * @return
     */
    default LogicRule<T> or(LogicRule<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> matching(t) || other.matching(t);
    }


    /**
     * 创建and规则
     *
     * @param other
     * @return
     */
    default LogicRule<T> and(LogicRule<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> matching(t) && other.matching(t);
    }


}
