package com.yilun.gl.dof.excute.framework.core;


import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.content.TreeWrapper;

/**
 * @ClassName InitLogicGroup
 * @Author gule.gl
 * @Date 2021/6/19 1:00 下午
 * @Description InitLogicGroup
 * @Version 1.0
 */
public abstract class DomainServiceGroup<T extends ContextData> {

    protected abstract void init(TreeWrapper listWrapper);

}
