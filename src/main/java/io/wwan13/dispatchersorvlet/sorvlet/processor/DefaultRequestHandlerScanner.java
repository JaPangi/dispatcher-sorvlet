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

import io.wwan13.dispatchersorvlet.sorvlet.RequestHandler;
import io.wwan13.dispatchersorvlet.sorvlet.RequestHandlers;
import io.wwan13.dispatchersorvlet.sorvlet.ComponentScanner;
import io.wwan13.dispatchersorvlet.sorvlet.RequestHandlerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.RequestMapping;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.SocketController;
import io.wwan13.dispatchersorvlet.util.NamingConverter;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultRequestHandlerScanner implements RequestHandlerScanner {

    private final ComponentScanner componentScanner;
    private final ApplicationContext applicationContext;

    public DefaultRequestHandlerScanner(
            ComponentScanner componentScanner,
            ApplicationContext applicationContext
    ) {
        this.componentScanner = componentScanner;
        this.applicationContext = applicationContext;
    }

    @Override
    public RequestHandlers scan() {
        Set<Class<?>> controllerClasses = componentScanner
                .scanComponentsWithAnnotation(SocketController.class);
        Set<RequestHandler> handlers = extractAllHandlers(controllerClasses);

        return new RequestHandlers(handlers);
    }

    private Set<RequestHandler> extractAllHandlers(
            Set<Class<?>> controllerClasses
    ) {
        Set<RequestHandler> handlers = new HashSet<>();

        for (Class<?> controller : controllerClasses) {
            Object registeredController = applicationContext
                    .getBean(NamingConverter.toLowerCamelCase(controller.getSimpleName()));
            extractHandlers(registeredController, handlers);
        }

        return handlers;
    }

    private void extractHandlers(
            Object controller,
            Set<RequestHandler> handlers
    ) {
        Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(this::isHandlerMethod)
                .forEach(method -> {
                    RequestHandler handler = RequestHandler.of(controller, method);
                    handlers.add(handler);
                });
    }

    private boolean isHandlerMethod(Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }
}

