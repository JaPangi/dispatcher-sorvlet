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

package io.wwan13.dispatchersorvlet.configuration.support;

import io.wwan13.dispatchersorvlet.configuration.SocketServerProperties;

public class SocketServerPropertiesRegistry {

    private static final int DEFAULT_SOCKET_SERVER_PORT = 8071;
    private static final String DEFAULT_SCAN_BASE_PACKAGE = "";

    private int port = DEFAULT_SOCKET_SERVER_PORT;
    private String scanBasePackage = DEFAULT_SCAN_BASE_PACKAGE;

    public SocketServerPropertiesRegistry port(int port) {
        this.port = port;
        return this;
    }

    public SocketServerPropertiesRegistry scanBasePackage(String scanBasePackage) {
        this.scanBasePackage = scanBasePackage;
        return this;
    }

    protected SocketServerProperties apply() {
        return new SocketServerProperties(port, scanBasePackage);
    }
}
