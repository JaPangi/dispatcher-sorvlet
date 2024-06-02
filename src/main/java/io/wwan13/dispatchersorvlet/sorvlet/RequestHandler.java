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

import io.wwan13.dispatchersorvlet.sorvlet.annotation.KeyParameter;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.RequestBody;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.RequestMapping;
import io.wwan13.dispatchersorvlet.sorvlet.util.MethodExecutor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public record RequestHandler(
        String requestKey,
        Object controller,
        Method method
) {

    private static final String REQUEST_KEY_FORMAT = "%s_%s";

    public static RequestHandler of(Object controller, Method method) {
        validateParameter(method);

        return new RequestHandler(
                getRequestKey(controller, method),
                controller,
                method
        );
    }

    private static void validateParameter(Method method) {
        Parameter[] parameters = method.getParameters();

        for (Parameter parameter : parameters) {
            boolean hasResolvableAnnotation = parameter.isAnnotationPresent(RequestBody.class) ||
                    parameter.isAnnotationPresent(KeyParameter.class);

            if (!hasResolvableAnnotation) {
                throw new IllegalStateException(
                        "Controller method's parameters must have resolve annotation"
                );
            }
        }
    }

    private static String getRequestKey(Object controller, Method method) {
        if (!controller.getClass().isAnnotationPresent(RequestMapping.class)) {
            return method.getAnnotation(RequestMapping.class).key();
        }

        String keyPrefix = controller.getClass().getAnnotation(RequestMapping.class).key();
        String methodKey = method.getAnnotation(RequestMapping.class).key();

        return String.format(REQUEST_KEY_FORMAT, keyPrefix, methodKey);
    }

    public Object handle(Object[] arguments) {
        return MethodExecutor.execute(controller, method, arguments);
    }

    public Parameter[] getHandlerParameters() {
        return method.getParameters();
    }
}
