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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SocketConnectionPool {

    private final List<SocketConnection> pool;

    public SocketConnectionPool() {
        this.pool = new ArrayList<>();
    }

    public void add(SocketConnection connection) {
        pool.add(connection);
        connection.start();
    }

    public void toAllConnections(Consumer<SocketConnection> action) {
        pool.forEach(action);
    }

    public void checkAllConnectionsHealth() {
        List<SocketConnection> unhealthyConnections = pool.stream()
                .filter(connection -> !connection.isHealthyConnection())
                .toList();

        unhealthyConnections.forEach(connection -> {
            connection.closeConnection(SocketCloseSignal.UNHEALTHY_CONNECTION);
            pool.remove(connection);
        });
    }

    public void removeClosedConnections() {
        List<SocketConnection> closedConnections = pool.stream()
                .filter(SocketConnection::isClosedConnection)
                .toList();

        pool.removeAll(closedConnections);
    }

    public void closeAllConnections(SocketCloseSignal signal) {
        pool.forEach(connection -> connection.closeConnection(signal));
    }
}
