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
import io.wwan13.dispatchersorvlet.sorvlet.RequestHandlers;
import io.wwan13.dispatchersorvlet.sorvlet.processor.stub.StubApplicationContext;
import io.wwan13.dispatchersorvlet.sorvlet.processor.stub.StubSocketControllerScanner;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultRequestHandlerScannerTest extends UnitTest {

    static DefaultRequestHandlerScanner handlerScanner =
            new DefaultRequestHandlerScanner(
                    new StubSocketControllerScanner(),
                    new StubApplicationContext()
            );

    @Test
    void should_ExtractAllHandlers() {
        // given
        final String basePath = "";

        // when
        RequestHandlers handlers = handlerScanner.scan(basePath);

        // then
        assertThat(handlers.handlers().size()).isEqualTo(5);
    }
}