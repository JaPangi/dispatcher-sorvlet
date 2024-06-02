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

package io.wwan13.dispatchersorvlet.configuration;

import io.wwan13.dispatchersorvlet.sorvlet.ArgumentsResolver;
import io.wwan13.dispatchersorvlet.sorvlet.ComponentScanner;
import io.wwan13.dispatchersorvlet.sorvlet.DispatcherSorvlet;
import io.wwan13.dispatchersorvlet.sorvlet.ExceptionHandlerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.ExceptionHandlers;
import io.wwan13.dispatchersorvlet.sorvlet.RequestHandlerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.RequestHandlers;
import io.wwan13.dispatchersorvlet.sorvlet.processor.DefaultArgumentsResolver;
import io.wwan13.dispatchersorvlet.sorvlet.processor.DefaultExceptionHandlerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.processor.DefaultRequestHandlerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.processor.ReflectionComponentScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class DispatcherSorvletConfiguration {

    @Bean
    public ComponentScanner componentScanner(
            SocketServerProperties socketServerProperties
    ) {
        return new ReflectionComponentScanner(socketServerProperties.scanBasePackage());
    }

    @Bean
    public RequestHandlers requestHandlers(
            ComponentScanner componentScanner,
            ApplicationContext applicationContext
    ) {
        RequestHandlerScanner requestHandlerScanner =
                new DefaultRequestHandlerScanner(componentScanner, applicationContext);

        return requestHandlerScanner.scan();
    }

    @Bean
    public ArgumentsResolver argumentsResolver() {
        return new DefaultArgumentsResolver();
    }

    @Bean
    public ExceptionHandlers exceptionHandlers(
            ComponentScanner componentScanner,
            ApplicationContext applicationContext
    ) {
        ExceptionHandlerScanner scanner =
                new DefaultExceptionHandlerScanner(componentScanner, applicationContext);

        return scanner.scan();
    }

    @Bean
    public DispatcherSorvlet dispatcherSorvlet(
            RequestHandlers requestHandlers,
            ArgumentsResolver argumentsResolver,
            ExceptionHandlers exceptionHandlers
    ) {
        return new DispatcherSorvlet(
                requestHandlers,
                argumentsResolver,
                exceptionHandlers
        );
    }
}
