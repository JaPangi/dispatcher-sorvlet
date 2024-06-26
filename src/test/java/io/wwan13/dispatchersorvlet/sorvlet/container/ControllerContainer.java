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

package io.wwan13.dispatchersorvlet.sorvlet.container;

import io.wwan13.dispatchersorvlet.sorvlet.annotation.KeyParameter;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.RequestMapping;
import io.wwan13.dispatchersorvlet.sorvlet.dto.response.SocketResponse;

public class ControllerContainer {

    @RequestMapping(key = "TEST")
    public static class TestController {

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
        public SocketResponse fourth(@KeyParameter String param) {
            return SocketResponse.success(param);
        }
    }

    public static class Test2Controller {

        @RequestMapping(key = "FIRST")
        public SocketResponse first() {
            return SocketResponse.success("first");
        }

        public SocketResponse second() {
            return SocketResponse.success("second");
        }
    }
}
