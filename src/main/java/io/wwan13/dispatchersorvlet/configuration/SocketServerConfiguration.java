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

import io.wwan13.dispatchersorvlet.socket.SocketConnectionHandler;
import io.wwan13.dispatchersorvlet.socket.SocketConnectionPool;
import io.wwan13.dispatchersorvlet.socket.SocketHealthCheckHandler;
import io.wwan13.dispatchersorvlet.sorvlet.DispatcherSorvlet;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketServerConfiguration {

    @Bean
    public ServerSocket serverSocket() {
        try {
            return new ServerSocket(8070);
        } catch (IOException e) {
            throw new RuntimeException("Port 8070 is already allocated");
        }
    }

    @Bean
    public SocketConnectionPool socketConnectionPool() {
        return new SocketConnectionPool();
    }

    @Bean
    public SocketConnectionHandler socketConnectionHandler(
            ServerSocket serverSocket,
            SocketConnectionPool socketConnectionPool,
            DispatcherSorvlet dispatcherSorvlet
    ) {
        return new SocketConnectionHandler(
                serverSocket,
                socketConnectionPool,
                dispatcherSorvlet
        );
    }

    @Bean
    public SocketHealthCheckHandler socketHealthCheckHandler(
            SocketConnectionPool socketConnectionPool
    ) {
        return new SocketHealthCheckHandler(socketConnectionPool);
    }
}
