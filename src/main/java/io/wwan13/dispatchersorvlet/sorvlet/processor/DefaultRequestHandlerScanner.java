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
import io.wwan13.dispatchersorvlet.sorvlet.SocketControllerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.RequestHandlerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.RequestMapping;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultRequestHandlerScanner implements RequestHandlerScanner {

    private final SocketControllerScanner socketControllerScanner;
    private final ApplicationContext applicationContext;

    public DefaultRequestHandlerScanner(
            SocketControllerScanner socketControllerScanner,
            ApplicationContext applicationContext
    ) {
        this.socketControllerScanner = socketControllerScanner;
        this.applicationContext = applicationContext;
    }

    @Override
    public RequestHandlers scan(String scanBasePackages) {
        Set<Class<?>> controllerClasses =
                socketControllerScanner.scanControllerClasses(scanBasePackages);
        Set<RequestHandler> handlers = extractAllHandlers(controllerClasses);

        return new RequestHandlers(handlers);
    }

    private Set<RequestHandler> extractAllHandlers(
            Set<Class<?>> controllerClasses
    ) {
        Set<RequestHandler> handlers = new HashSet<>();

        for (Class<?> controller : controllerClasses) {
            Object registeredController = applicationContext.getBean(toBeanName(controller));
            extractHandlers(registeredController, handlers);
        }

        return handlers;
    }

    private String toBeanName(Class<?> controller) {
        String controllerName = controller.getSimpleName();
        char firstLetter = controllerName.charAt(0);
        return controllerName.replace(
                controllerName.charAt(0),
                Character.toLowerCase(firstLetter)
        );
    }

    private void extractHandlers(
            Object controller,
            Set<RequestHandler> handlers
    ) {
        Arrays.stream(controller.getClass().getMethods())
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

