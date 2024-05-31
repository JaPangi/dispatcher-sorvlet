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
import io.wwan13.dispatchersorvlet.socket.enums.SocketConnectionStatus;
import io.wwan13.dispatchersorvlet.sorvlet.DispatcherSorvlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.Socket;

public class SocketConnection extends Thread {

    private static final Logger log = LoggerFactory.getLogger(SocketConnection.class);
    private static final String SYSTEM_EXIST_SIGNAL = "SYSTEM_EXIT";

    private final SocketClient client;
    private final DispatcherSorvlet dispatcherSorvlet;
    private SocketConnectionStatus status;

    public SocketConnection(
            SocketClient client,
            DispatcherSorvlet dispatcherSorvlet,
            SocketConnectionStatus status
    ) {
        this.client = client;
        this.dispatcherSorvlet = dispatcherSorvlet;
        this.status = status;
    }

    public static SocketConnection of(
            Socket socket,
            DispatcherSorvlet dispatcherSorvlet
    ) {
        return new SocketConnection(
                SocketClient.of(socket),
                dispatcherSorvlet,
                SocketConnectionStatus.CONNECTED
        );
    }

    @Override
    public void run() {
        afterConnectionEstablished();
        while (status == SocketConnectionStatus.CONNECTED) {
            socketAction();
        }
        afterConnectionClosed();
        this.interrupt();
    }

    private void afterConnectionEstablished() {
        log.info("[{}] Client connected", client.getClientId());
        MDC.put("client_id", client.getClientId());
    }

    private void socketAction() {
        String message = client.readMessage();
        if (message != null) {
            checkSystemExitSignal(message);
            String result = dispatcherSorvlet.doService(message);
            client.writeMessage(result);
        }
    }

    private void checkSystemExitSignal(String message) {
        if (message.contains(SYSTEM_EXIST_SIGNAL)) {
            closeConnection(SocketCloseSignal.EXIT_SIGNAL);
        }
    }

    private void afterConnectionClosed() {
        MDC.clear();
    }

    public void write(String message) {
        client.writeMessage(message);
    }

    public void closeConnection(SocketCloseSignal signal) {
        this.status = SocketConnectionStatus.CLOSED;
        client.close();
        log.info("[{}] Disconnect from client (interrupted by signal : {})",
                client.getClientId(), signal.name());
    }

    public boolean isHealthyConnection() {
        return client.isHealthyClient();
    }

    public boolean isClosedConnection() {
        return status.equals(SocketConnectionStatus.CLOSED);
    }

    public boolean isSameConnection(String targetId) {
        return client.getClientId().equals(targetId);
    }
}
