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

package io.wwan13.dispatchersorvlet.sorvlet.processor;

import io.wwan13.dispatchersorvlet.sorvlet.ComponentScanner;
import io.wwan13.dispatchersorvlet.sorvlet.ExceptionHandler;
import io.wwan13.dispatchersorvlet.sorvlet.ExceptionHandlerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.ExceptionHandlers;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.SocketControllerAdvice;
import io.wwan13.dispatchersorvlet.sorvlet.util.ExceptionUtil;
import io.wwan13.dispatchersorvlet.util.NamingConverter;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class DefaultExceptionHandlerScanner implements ExceptionHandlerScanner {

    private final ComponentScanner componentScanner;
    private final ApplicationContext applicationContext;

    public DefaultExceptionHandlerScanner(
            ComponentScanner componentScanner,
            ApplicationContext applicationContext
    ) {
        this.componentScanner = componentScanner;
        this.applicationContext = applicationContext;
    }

    @Override
    public ExceptionHandlers scan() {
        Set<Class<?>> controllerAdviceClasses = componentScanner
                .scanComponentsWithAnnotation(SocketControllerAdvice.class);
        List<ExceptionHandler> exceptionHandlers = extractAllHandlers(controllerAdviceClasses);

        return new ExceptionHandlers(exceptionHandlers);
    }

    private List<ExceptionHandler> extractAllHandlers(
            Set<Class<?>> controllerAdviceClasses
    ) {
        List<ExceptionHandler> handlers = new ArrayList<>();

        for (Class<?> controllerAdvice : controllerAdviceClasses) {
            Object registeredControllerAdvice = applicationContext
                    .getBean(NamingConverter.toLowerCamelCase(controllerAdvice.getSimpleName()));
            extractHandlers(registeredControllerAdvice, handlers);
        }

        return sortByExceptionExtendedLevel(handlers);
    }

    private void extractHandlers(
            Object controllerAdvice,
            List<ExceptionHandler> handlers
    ) {
        Arrays.stream(controllerAdvice.getClass().getDeclaredMethods())
                .filter(this::isHandlerMethod)
                .forEach(method -> {
                    ExceptionHandler handler = ExceptionHandler.of(controllerAdvice, method);
                    handlers.add(handler);
                });
    }

    private boolean isHandlerMethod(Method method) {
        return method.isAnnotationPresent(
                io.wwan13.dispatchersorvlet.sorvlet.annotation.ExceptionHandler.class
        );
    }

    private List<ExceptionHandler> sortByExceptionExtendedLevel(
            List<ExceptionHandler> handlers
    ) {
        return handlers.stream()
                .sorted(
                        Comparator.comparing(
                                handler -> ExceptionUtil
                                        .getExceptionExtendedLevel(handler.exceptionClazz()),
                                Comparator.reverseOrder()
                        )
                )
                .toList();
    }
}
