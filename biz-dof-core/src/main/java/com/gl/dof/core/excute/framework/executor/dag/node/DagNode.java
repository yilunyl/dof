package com.gl.dof.core.excute.framework.executor.dag.node;

import com.gl.dof.core.excute.framework.logic.DomainServiceUnit;

/**
 * @ClassName: biz-dof Test
 * @Description: 有向无环图节点
 * @Author: 逸伦
 * @Date: 2022/6/18 22:01
 * @Version: 1.0
 */
public class DagNode<T extends DomainServiceUnit> {

	private final T logicUnit;

	public DagNode(T logicUnit) {
		this.logicUnit = logicUnit;
	}
}
