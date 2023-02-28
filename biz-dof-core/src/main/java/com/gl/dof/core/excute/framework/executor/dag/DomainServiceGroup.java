package com.gl.dof.core.excute.framework.executor.dag;


import com.gl.dof.core.excute.framework.content.DagWrapper;

/**
 * @ClassName InitLogicGroup
 * @Author gule.gl
 * @Date 2021/6/19 1:00 下午
 * @Description InitLogicGroup
 * @Version 1.0
 */
public abstract class DomainServiceGroup {

    protected abstract void init(DagWrapper treeWrapper);

}
