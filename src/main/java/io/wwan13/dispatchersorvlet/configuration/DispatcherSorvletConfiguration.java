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
import io.wwan13.dispatchersorvlet.sorvlet.SocketControllerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.DispatcherSorvlet;
import io.wwan13.dispatchersorvlet.sorvlet.SocketHandlerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.RequestHandlers;
import io.wwan13.dispatchersorvlet.sorvlet.processor.DefaultArgumentsResolver;
import io.wwan13.dispatchersorvlet.sorvlet.processor.DefaultSocketHandlerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.processor.ReflectionSocketControllerScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class DispatcherSorvletConfiguration {

    @Bean
    public SocketControllerScanner socketControllerScanner() {
        return new ReflectionSocketControllerScanner();
    }

    @Bean
    public SocketHandlerScanner socketHandlerScanner(
            SocketControllerScanner socketControllerScanner,
            ApplicationContext applicationContext
    ) {
        return new DefaultSocketHandlerScanner(socketControllerScanner, applicationContext);
    }

    @Bean
    public RequestHandlers socketHandlers(
            SocketHandlerScanner socketHandlerScanner
    ) {
        return socketHandlerScanner.scan("");
    }

    @Bean
    public ArgumentsResolver argumentResolver() {
        return new DefaultArgumentsResolver();
    }

    @Bean
    public DispatcherSorvlet dispatcherSorvlet(
            RequestHandlers requestHandlers,
            ArgumentsResolver argumentsResolver
    ) {
        return new DispatcherSorvlet(
                requestHandlers,
                argumentsResolver
        );
    }
}