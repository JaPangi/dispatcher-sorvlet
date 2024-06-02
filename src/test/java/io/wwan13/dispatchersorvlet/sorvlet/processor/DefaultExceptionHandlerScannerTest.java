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

import io.wwan13.dispatchersorvlet.UnitTest;
import io.wwan13.dispatchersorvlet.sorvlet.ExceptionHandler;
import io.wwan13.dispatchersorvlet.sorvlet.ExceptionHandlerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.ExceptionHandlers;
import io.wwan13.dispatchersorvlet.sorvlet.container.ControllerAdviceContainer;
import io.wwan13.dispatchersorvlet.sorvlet.processor.stub.StubApplicationContext;
import io.wwan13.dispatchersorvlet.sorvlet.processor.stub.StubComponentScanner;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultExceptionHandlerScannerTest extends UnitTest {

    @Test
    void should_ExtractAllHandlers() {
        // given
        final String basePath = "";
        final StubComponentScanner componentScanner = new StubComponentScanner();
        componentScanner.componentsWillBe(
                Set.of(
                        ControllerAdviceContainer.TestControllerAdvice.class
                )
        );

        final ExceptionHandlerScanner scanner =
                new DefaultExceptionHandlerScanner(componentScanner, new StubApplicationContext());

        // when
        ExceptionHandlers handlers = scanner.scan(basePath);

        // then
        assertThat(handlers.handlers().size()).isEqualTo(3);
    }

    @Test
    void should_SortedWithExtendedLevelDesc() {
        // given
        final String basePath = "";
        final StubComponentScanner componentScanner = new StubComponentScanner();
        componentScanner.componentsWillBe(
                Set.of(
                        ControllerAdviceContainer.TestControllerAdvice.class
                )
        );

        final ExceptionHandlerScanner scanner =
                new DefaultExceptionHandlerScanner(componentScanner, new StubApplicationContext());

        // when
        ExceptionHandlers handlers = scanner.scan(basePath);
        List<? extends Class<? extends Exception>> handlerSupports = handlers.handlers().stream()
                .map(ExceptionHandler::exceptionClazz)
                .toList();

        // then
        assertThat(handlerSupports).isEqualTo(
                List.of(
                        IllegalStateException.class,
                        RuntimeException.class,
                        Exception.class
                )
        );
    }
}