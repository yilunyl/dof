/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.gl.dof.core.excute.framework.context.attribute;


import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.gl.dof.excute.framework.base.util.ObjectUtil.checkNonEmpty;
import static org.mockito.internal.util.Checks.checkNotNull;


/**
 * A pool of {@link io.netty.util.Constant}s.
 *
 * @param <T> the type of the constant
 */
public abstract class ConstantPool<T extends Constant<T>> {

    private final ConcurrentMap<String, T> constants = PlatformDependent.newConcurrentHashMap();

    private final AtomicInteger nextId = new AtomicInteger(1);

    /**
     * Shortcut of {@link #valueOf(String) valueOf(firstNameComponent.getName() + "#" + secondNameComponent)}.
     */
    public T valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return valueOf(
                checkNotNull(firstNameComponent, "firstNameComponent").getName() +
                '#' +
                checkNotNull(secondNameComponent, "secondNameComponent"));
    }

    public T valueOf(Class<?> firstNameComponent) {
        return valueOf(
                checkNotNull(firstNameComponent, "firstNameComponent").getName());
    }

    /**
     * Returns the {@link io.netty.util.Constant} which is assigned to the specified {@code name}.
     * If there's no such {@link io.netty.util.Constant}, a new one will be created and returned.
     * Once created, the subsequent calls with the same {@code name} will always return the previously created one
     * (i.e. singleton.)
     *
     * @param name the name of the {@link io.netty.util.Constant}
     */
    public T valueOf(String name) {
        return getOrCreate(checkNonEmpty(name, "name"));
    }

    /**
     * Get existing constant by name or creates new one if not exists. Threadsafe
     *
     * @param name the name of the {@link io.netty.util.Constant}
     */
    private T getOrCreate(String name) {
        T constant = constants.get(name);
        if (constant == null) {
            final T tempConstant = newConstant(nextId(), name);
            constant = constants.putIfAbsent(name, tempConstant);
            if (constant == null) {
                return tempConstant;
            }
        }

        return constant;
    }

    /**
     * Returns {@code true} if a {@link AttributeKey} exists for the given {@code name}.
     */
    public boolean exists(String name) {
        return constants.containsKey(checkNonEmpty(name, "name"));
    }

    /**
     * Creates a new {@link io.netty.util.Constant} for the given {@code name} or fail with an
     * {@link IllegalArgumentException} if a {@link io.netty.util.Constant} for the given {@code name} exists.
     */
    public T newInstance(String name) {
        return createOrThrow(checkNonEmpty(name, "name"));
    }

    /**
     * Creates constant by name or throws exception. Threadsafe
     *
     * @param name the name of the {@link Constant}
     */
    private T createOrThrow(String name) {
        T constant = constants.get(name);
        if (constant == null) {
            final T tempConstant = newConstant(nextId(), name);
            constant = constants.putIfAbsent(name, tempConstant);
            if (constant == null) {
                return tempConstant;
            }
        }

        throw new IllegalArgumentException(String.format("'%s' is already in use", name));
    }

    /**
     * 初始化相关数据
     */
    protected void clear(){
        constants.clear();
        nextId.set(1);
    }
    protected abstract T newConstant(int id, String name);

    @Deprecated
    public final int nextId() {
        return nextId.getAndIncrement();
    }
}
