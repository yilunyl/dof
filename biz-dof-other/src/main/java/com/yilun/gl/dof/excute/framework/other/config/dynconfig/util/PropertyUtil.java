/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yilun.gl.dof.excute.framework.other.config.dynconfig.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

/**
 * @ClassName PropertyUtil
 * @Description
 * @Author gule
 * @Version 1.0
 **/

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyUtil {

    public static <T> T instanceClassFromEnv(final Environment environment, final String prefix, final Class<T> targetClass) {
        Binder binder = Binder.get(environment);
        String prefixParam = prefix.endsWith(".") ? prefix.substring(0, prefix.length() - 1) : prefix;
        return binder.bind(prefixParam, targetClass).get();
    }
}
