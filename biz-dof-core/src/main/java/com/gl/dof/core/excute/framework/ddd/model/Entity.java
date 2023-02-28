package com.gl.dof.core.excute.framework.ddd.model;

/**
 * 实体定义
 */
public interface Entity extends java.lang.Appendable,Changeable {

    Long getId();

    void setId(Long id);

    Boolean isDeleted();

    void setDeleted(Boolean deleted);
}
