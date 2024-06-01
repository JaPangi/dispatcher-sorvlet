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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class SocketHealthCheckHandler implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SocketHealthCheckHandler.class);
    private static final int HEALTH_CHECK_TERM = 30000;

    private final SocketConnectionPool connectionPool;

    public SocketHealthCheckHandler(SocketConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread healthCheckHandlingThread = new Thread(handleSocketHealthCheck());
        healthCheckHandlingThread.start();
    }

    private Runnable handleSocketHealthCheck() {
        return () -> {
            while (true) {
                waitForHealthCheckTerm();
                connectionPool.removeClosedConnections();
                connectionPool.checkAllConnectionsHealth();
            }
        };
    }

    private void waitForHealthCheckTerm() {
        try {
            Thread.sleep(HEALTH_CHECK_TERM);
        } catch (InterruptedException e) {
            throw new RuntimeException("cannot wait");
        }
    }
}
