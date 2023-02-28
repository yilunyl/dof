package com.gl.dof.core.excute.framework.ddd.model;

/**
 * 实体属性，update-tracing
 * @param <T>
 */
public final class Field<T> implements Changeable {
    private boolean changed = false;
    private T value;

    private Field(T value){
        this.value = value;
    }

    public void setValue(T value){
        if(!equalsValue(value)){
            this.changed = true;
        }
        this.value = value;
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    public T getValue() {
        return value;
    }

    public boolean equalsValue(T value){
        if(this.value == null && value == null){
            return true;
        }
        if(this.value == null){
            return false;
        }
        if(value == null){
            return false;
        }
        return this.value.equals(value);
    }

    public static <T> Field<T> build(T value){
        return new Field<T>(value);
    }
}
