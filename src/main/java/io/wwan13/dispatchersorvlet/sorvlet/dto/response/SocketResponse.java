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

package io.wwan13.dispatchersorvlet.sorvlet.dto.response;

public abstract class SocketResponse {

    private ResponseStatus status;
    private String message;

    public SocketResponse() {
    }

    protected SocketResponse(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public static <T> SocketResponse success(T data) {
        return new SuccessResponse<T>(data);
    }

    public static <T> SocketResponse success(T data, String message) {
        return new SuccessResponse<T>(data, message);
    }

    public static ErrorResponse error(String errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse error(String errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

