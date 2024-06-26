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

public class SuccessResponse<T> extends SocketResponse {

    private static final String DEFAULT_SUCCESS_MESSAGE = "request success";

    private T data;

    public SuccessResponse() {
    }

    public SuccessResponse(T data, String message) {
        super(ResponseStatus.SUCCESS, message);
        this.data = data;
    }

    public SuccessResponse(T data) {
        super(ResponseStatus.SUCCESS, DEFAULT_SUCCESS_MESSAGE);
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
