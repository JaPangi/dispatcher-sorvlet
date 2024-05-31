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

package io.wwan13.dispatchersorvlet.socket;

import io.wwan13.dispatchersorvlet.socket.enums.SocketCloseSignal;
import io.wwan13.dispatchersorvlet.sorvlet.DispatcherSorvlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnectionHandler implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SocketConnection.class);

    private final ServerSocket serverSocket;
    private final SocketConnectionPool connectionPool;
    private final DispatcherSorvlet dispatcherSorvlet;

    public SocketConnectionHandler(
            ServerSocket serverSocket,
            SocketConnectionPool connectionPool,
            DispatcherSorvlet dispatcherSorvlet
    ) {
        this.serverSocket = serverSocket;
        this.connectionPool = connectionPool;
        this.dispatcherSorvlet = dispatcherSorvlet;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread connectionHandlingThread = new Thread(handleSocketConnection());
        connectionHandlingThread.start();
    }

    private Runnable handleSocketConnection() {
        return () -> {
            try {
                log.info("Socket started on port(s) {} (tcp/ip)", serverSocket.getLocalPort());
                while (true) {
                    Socket socket = serverSocket.accept();
                    SocketConnection connection =
                            SocketConnection.of(socket, dispatcherSorvlet);
                    connectionPool.add(connection);
                }
            } catch (IOException e) {
                connectionPool.closeAllConnections(SocketCloseSignal.SERVER_SHUT_DOWN);
                log.info("Socket shut downed ...");
            }
        };
    }
}
