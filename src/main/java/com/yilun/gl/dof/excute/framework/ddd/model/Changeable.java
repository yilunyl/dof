package com.yilun.gl.dof.excute.framework.ddd.model;

/**
 * 属性是否修改，解决update-tracing
 */
public interface Changeable {
    boolean isChanged();
}
