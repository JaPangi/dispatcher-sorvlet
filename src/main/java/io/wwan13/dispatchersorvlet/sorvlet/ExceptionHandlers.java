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

import io.wwan13.dispatchersorvlet.sorvlet.exceptioinhandler.DefaultExceptionHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public record ExceptionHandlers(
        List<ExceptionHandler> handlers
) {

    private static final ExceptionHandler DEFAULT_EXCEPTION_HANDLER = ExceptionHandler.of(
            new DefaultExceptionHandler(),
            DefaultExceptionHandler.class.getDeclaredMethods()[0]
    );

    public ExceptionHandler handlerMapping(Exception exception) {
        Exception handlingException = getHandlingException(exception);

        return handlers.stream()
                .filter(handler -> handler.matches(handlingException))
                .findFirst()
                .orElse(DEFAULT_EXCEPTION_HANDLER);
    }

    private Exception getHandlingException(Exception exception) {
        if (exception instanceof InvocationTargetException) {
            return (Exception) exception.getCause();
        }
        return exception;
    }
}
