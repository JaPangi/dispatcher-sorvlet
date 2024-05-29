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

import io.wwan13.dispatchersorvlet.util.UuidGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {

    private static final String PING_SIGNAL = "PING";

    private final String clientId;
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public SocketClient(
            String clientId,
            Socket socket,
            BufferedReader reader,
            PrintWriter writer
    ) {
        this.clientId = clientId;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    public static SocketClient of(Socket socket) {
        try {
            return new SocketClient(
                    UuidGenerator.shortUuid(),
                    socket,
                    new BufferedReader(new InputStreamReader(socket.getInputStream())),
                    new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))
            );
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialize socket session");
        }
    }

    public String readMessage() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read message");
        }
    }

    public void writeMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public boolean isHealthyClient() {
        sendPing();
        return !writer.checkError();
    }

    public void sendPing() {
        writeMessage(PING_SIGNAL);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot close socket session");
        }
    }

    public String getClientId() {
        return clientId;
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }
}
