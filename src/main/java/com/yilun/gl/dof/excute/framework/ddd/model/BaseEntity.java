package com.yilun.gl.dof.excute.framework.ddd.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.yilun.gl.dof.excute.framework.ddd.utils.ChangeableUtil;

/**
 * 基础实体，定义常用属性
 */
public abstract class BaseEntity implements Entity {
    private Long id;

    private String tracerId;

    private Field<Boolean> deleted;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Boolean isDeleted() {
        return get(deleted);
    }

    public Boolean isDeletedChanged(){
        if(deleted == null || deleted.getValue() == null){
            return null;
        }
        return deleted.isChanged();
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = set(this.deleted, deleted);
    }

//    @Override
    public boolean isAppended() {
        return this.id == null;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isChanged() {
        return ChangeableUtil.isChanged(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof BaseEntity)){
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        if(this.getId() ==null || that.getId() ==null){
            return false;
        }
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        if(this.getId() ==null){
            return super.hashCode();
        }
        return this.getId().hashCode();
    }

    protected  <T> T get(Field<T> field){
        if (field== null) {
            return null;
        }
        return field.getValue();
    }
    protected <T> Field<T> set(Field<T> field, T value) {
        if (field == null) {
            field = Field.build(value);
        } else {
            field.setValue(value);
        }
        return field;
    }

    public String getTracerId() {
        return tracerId;
    }

    public void setTracerId(String tracerId) {
        this.tracerId = tracerId;
    }
}
