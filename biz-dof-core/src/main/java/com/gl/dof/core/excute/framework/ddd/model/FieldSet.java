package com.gl.dof.core.excute.framework.ddd.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;
/**
 * 实体集合属性,update-tracing
 * @param <T>
 */
public class FieldSet<T> implements Set<T>,Changeable {
    private boolean changed = false;

    private final Set<T> set;

    public FieldSet() {
        set = Sets.newHashSet();
    }

    public FieldSet(Collection<T> collection) {
        this.set = Sets.newHashSet(collection);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(T t) {
        this.changed = true;
        return set.add(t);
    }

    @Override
    public boolean remove(Object o) {
        this.changed = true;
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        this.changed=true;
        return set.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        this.changed=true;
        return set.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        this.changed = true;
        return set.retainAll(c);
    }

    @Override
    public void clear() {
        this.changed=true;
        set.clear();
    }

    @Override
    public boolean isChanged() {
        return changed;
    }
}
