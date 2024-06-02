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

package io.wwan13.dispatchersorvlet.sorvlet.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wwan13.dispatchersorvlet.exception.InvalidRequestException;
import io.wwan13.dispatchersorvlet.sorvlet.dto.request.SocketRequest;

public class SocketMessageSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private SocketMessageSerializer() {
        throw new IllegalStateException("Cannot instantiate a utility class!");
    }

    public static SocketRequest deserialize(String message) {
        try {
            return objectMapper.readValue(message, SocketRequest.class);
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException();
        }
    }

    public static String serialize(Object response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize response");
        }
    }
}
