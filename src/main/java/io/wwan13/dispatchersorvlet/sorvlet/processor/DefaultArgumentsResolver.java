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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wwan13.dispatchersorvlet.exception.ResolveArgumentException;
import io.wwan13.dispatchersorvlet.sorvlet.ArgumentsResolver;
import io.wwan13.dispatchersorvlet.sorvlet.RequestHandler;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.KeyParameter;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.RequestBody;
import io.wwan13.dispatchersorvlet.sorvlet.dto.request.SocketRequest;
import io.wwan13.dispatchersorvlet.sorvlet.util.TypeConverter;

import java.lang.reflect.Parameter;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class DefaultArgumentsResolver implements ArgumentsResolver {

//    private final static ObjectMapper objectMapper = new ObjectMapper()
//            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final String KEY_PARAMETER_DELIMITER = "_";
    private static final String KEY_PARAMETER_FORMAT = "{%s}";

    public Object[] resolve(RequestHandler handler, SocketRequest request) {
        Parameter[] parameters = handler.getHandlerParameters();
        int parametersLength = parameters.length;
        Object[] arguments = new Object[parametersLength];

        for (int i = 0; i < parametersLength; i++) {
            Parameter parameter = parameters[i];
            arguments[i] = resolveArgument(parameter, request, handler);
        }

        return arguments;
    }

    private Object resolveArgument(
            Parameter parameter,
            SocketRequest request,
            RequestHandler handler
    ) {
        if (parameter.isAnnotationPresent(RequestBody.class)) {
            return resolveRequestBody(request.body(), parameter);
        }

        if (parameter.isAnnotationPresent(KeyParameter.class)) {
            return resolveKeyParameter(handler.requestKey(), request.key(), parameter);
        }

        throw new ResolveArgumentException();
    }

    private Object resolveRequestBody(Object requestBody, Parameter parameter) {
        return TypeConverter.convert(requestBody, parameter.getType());
    }

    private Object resolveKeyParameter(
            String registeredKey,
            String requestKey,
            Parameter parameter
    ) {
        String keyParameter = String.format(KEY_PARAMETER_FORMAT, parameter.getName());
        int keyParameterIndex =
                List.of(registeredKey.split(KEY_PARAMETER_DELIMITER)).indexOf(keyParameter);

        String givenValue = requestKey.split(KEY_PARAMETER_DELIMITER)[keyParameterIndex];

        return TypeConverter.convert(givenValue, parameter.getType());
    }
}
