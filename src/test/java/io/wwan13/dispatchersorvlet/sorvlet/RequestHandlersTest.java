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

import io.wwan13.dispatchersorvlet.UnitTest;
import io.wwan13.dispatchersorvlet.exception.HandlerNotFoundException;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.RequestMapping;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.SocketController;
import io.wwan13.dispatchersorvlet.sorvlet.dto.response.SocketResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestHandlersTest extends UnitTest {

    @SocketController
    @RequestMapping(key = "TEST")
    static class TestController {

        @RequestMapping(key = "FIRST")
        public SocketResponse first() {
            return SocketResponse.success("first");
        }

        @RequestMapping(key = "SECOND")
        public SocketResponse second() {
            return SocketResponse.success("second");
        }

        @RequestMapping(key = "THIRD")
        public SocketResponse third() {
            return SocketResponse.success("third");
        }

        @RequestMapping(key = "FOURTH_{param}")
        public SocketResponse fourth(String param) {
            return SocketResponse.success(param);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "TEST_FIRST, TEST_FIRST",
            "TEST_SECOND, TEST_SECOND",
            "TEST_THIRD, TEST_THIRD",
            "TEST_FOURTH_4, TEST_FOURTH",
    })
    void should_MappingHandler(
            final String requestKey,
            final String expected
    ) {
        // given
        final Set<RequestHandler> handlers = new HashSet<>();
        final Object controller = new TestController();
        Arrays.stream(controller.getClass().getDeclaredMethods()).forEach(method -> {
            RequestHandler handler = RequestHandler.of(controller, method);
            handlers.add(handler);
        });

        final RequestHandlers requestHandlers = new RequestHandlers(handlers);

        // when
        RequestHandler handler = requestHandlers.handlerMapping(requestKey);

        // then
        assertThat(handler.requestKey()).contains(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "TEST_FIFTH",
            "TEST_FIRST_1",
            "TEST_FOURTH_4_GOOD",
    })
    void should_ThrowException_when_HandlerNotExists(
            final String requestKey
    ) {
        // given
        final Set<RequestHandler> handlers = new HashSet<>();
        final Object controller = new TestController();
        Arrays.stream(controller.getClass().getDeclaredMethods()).forEach(method -> {
            RequestHandler handler = RequestHandler.of(controller, method);
            handlers.add(handler);
        });

        final RequestHandlers requestHandlers = new RequestHandlers(handlers);

        // when, then
        assertThatThrownBy(() -> requestHandlers.handlerMapping(requestKey))
                .isInstanceOf(HandlerNotFoundException.class);
    }
}