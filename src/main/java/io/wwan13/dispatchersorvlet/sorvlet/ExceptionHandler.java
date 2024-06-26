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

package io.wwan13.dispatchersorvlet.sorvlet;

import io.wwan13.dispatchersorvlet.sorvlet.dto.response.SocketResponse;
import io.wwan13.dispatchersorvlet.sorvlet.util.MethodExecutor;
import io.wwan13.dispatchersorvlet.sorvlet.util.TypeConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public record ExceptionHandler(
        Class<? extends Exception> exceptionClazz,
        Object controllerAdvice,
        Method method
) {

    public static ExceptionHandler of(Object controllerAdvice, Method method) {
        validateParameter(method);

        Class<? extends Exception> supportedException = getSupportedException(method);
        return new ExceptionHandler(
                supportedException,
                controllerAdvice,
                method
        );
    }

    public static void validateParameter(Method method) {
        boolean hasOnlyOneParameter = method.getParameterCount() == 1;
        boolean isExceptionType = Exception.class.isAssignableFrom(method.getParameterTypes()[0]);

        if (!hasOnlyOneParameter || !isExceptionType) {
            throw new IllegalStateException(
                    "Exception handler mush have only one Exception type parameter"
            );
        }
    }

    public static Class<? extends Exception> getSupportedException(Method method) {
        return method.getDeclaredAnnotation(
                io.wwan13.dispatchersorvlet.sorvlet.annotation.ExceptionHandler.class
        ).support();
    }

    public boolean matches(Exception exception) {
        return exceptionClazz.isAssignableFrom(exception.getClass());
    }

    public Object handle(Exception exception) {
        try {
            Object argument = TypeConverter.convert(exception, exceptionClazz);
            return MethodExecutor.execute(controllerAdvice, method, argument);
        } catch (InvocationTargetException e) {
            return SocketResponse.error(
                    e.getClass().getSimpleName(),
                    "Exception occurred while handling an exception."
            );
        }
    }
}
