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
import io.wwan13.dispatchersorvlet.sorvlet.annotation.RequestBody;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.RequestMapping;
import io.wwan13.dispatchersorvlet.sorvlet.dto.response.SocketResponse;
import io.wwan13.dispatchersorvlet.sorvlet.dto.response.SuccessResponse;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest extends UnitTest {

    @RequestMapping(key = "TEST")
    static class Test1Controller {

        @RequestMapping(key = "FIRST")
        public SocketResponse test() {
            return SocketResponse.success("first");
        }
    }

    @Test
    void should_CreateCombinedRequestKey_when_ControllerHasKey() {
        // given
        final Object controller = new Test1Controller();
        final Method method = controller.getClass().getDeclaredMethods()[0];

        RequestHandler requestHandler = RequestHandler.of(controller, method);

        // when
        String result = requestHandler.requestKey();

        // then
        assertThat(result).isEqualTo("TEST_FIRST");
    }

    @Test
    void should_ExecuteControllerMethod_when_ParameterNotExists() {
        // given
        final Object controller = new Test1Controller();
        final Method method = controller.getClass().getDeclaredMethods()[0];

        RequestHandler requestHandler = RequestHandler.of(controller, method);

        // when
        SuccessResponse response = (SuccessResponse) requestHandler.handle(new Object[0]);

        // then
        assertThat(response.getStatus().name()).isEqualTo("SUCCESS");
        assertThat(response.getData()).isEqualTo("first");
    }

    static class Test2Controller {

        @RequestMapping(key = "SECOND")
        public SocketResponse test(@RequestBody String arg) {
            return SocketResponse.success(arg);
        }
    }

    @Test
    void should_CreateCombinedRequestKey_when_ControllerDoesntHasKey() {
        // given
        final Object controller = new Test2Controller();
        final Method method = controller.getClass().getDeclaredMethods()[0];

        RequestHandler requestHandler = RequestHandler.of(controller, method);

        // when
        String result = requestHandler.requestKey();

        // then
        assertThat(result).isEqualTo("SECOND");
    }

    @Test
    void should_ExecuteControllerMethod_when_WithParameter() {
        // given
        final Object controller = new Test2Controller();
        final Method method = controller.getClass().getDeclaredMethods()[0];

        RequestHandler requestHandler = RequestHandler.of(controller, method);
        Object[] args = new Object[1];
        args[0] = "argument";

        // when
        SuccessResponse response = (SuccessResponse) requestHandler.handle(args);

        // then
        assertThat(response.getStatus().name()).isEqualTo("SUCCESS");
        assertThat(response.getData()).isEqualTo(args[0]);
    }
}