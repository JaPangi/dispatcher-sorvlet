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

import io.wwan13.dispatchersorvlet.sorvlet.dto.request.SocketRequest;
import io.wwan13.dispatchersorvlet.sorvlet.util.SocketMessageSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.InvocationTargetException;

public class DispatcherSorvlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherSorvlet.class);

    private final RequestHandlers requestHandlers;
    private final ArgumentsResolver argumentsResolver;
    private final ExceptionHandlers exceptionHandlers;

    public DispatcherSorvlet(
            RequestHandlers requestHandlers,
            ArgumentsResolver argumentsResolver,
            ExceptionHandlers exceptionHandlers
    ) {
        this.requestHandlers = requestHandlers;
        this.argumentsResolver = argumentsResolver;
        this.exceptionHandlers = exceptionHandlers;
    }

    public String doService(String socketMessage) {
        try {
            SocketRequest request = SocketMessageSerializer.deserialize(socketMessage);

            RequestHandler handler = requestHandlers.handlerMapping(request.key());

            Object[] arguments = argumentsResolver.resolve(handler, request);
            Object response = handler.handle(arguments);

            return SocketMessageSerializer.serialize(response);
        } catch (Exception e) {
            Exception rootException = getRootException(e);

            ExceptionHandler exceptionHandler = exceptionHandlers.handlerMapping(rootException);
            Object response = exceptionHandler.handle(rootException);

            log.error("[{}] Exception raised ({})",
                    MDC.get("client_id"), rootException.getMessage());

            return SocketMessageSerializer.serialize(response);
        }
    }

    private Exception getRootException(Exception exception) {
        if (exception instanceof InvocationTargetException) {
            return (Exception) exception.getCause();
        }
        return exception;
    }
}
