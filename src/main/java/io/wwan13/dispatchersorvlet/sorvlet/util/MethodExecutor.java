/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.wwan13.dispatchersorvlet.sorvlet.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodExecutor {

    private MethodExecutor() {
        throw new IllegalStateException("Cannot instantiate a utility class!");
    }

    public static Object execute(Object object, Method method, Object argument)
            throws InvocationTargetException {
        try {
            method.setAccessible(true);
            return method.invoke(object, argument);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access handler");
        }
    }

    public static Object execute(Object object, Method method, Object[] arguments)
            throws InvocationTargetException {
        try {
            method.setAccessible(true);
            return method.invoke(object, arguments);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access handler");
        }
    }
}
