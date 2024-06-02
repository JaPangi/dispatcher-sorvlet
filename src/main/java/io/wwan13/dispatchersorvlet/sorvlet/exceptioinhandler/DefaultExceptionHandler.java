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

package io.wwan13.dispatchersorvlet.sorvlet.exceptioinhandler;

import io.wwan13.dispatchersorvlet.sorvlet.annotation.ExceptionHandler;
import io.wwan13.dispatchersorvlet.sorvlet.dto.response.ErrorResponse;
import io.wwan13.dispatchersorvlet.sorvlet.dto.response.SocketResponse;

public class DefaultExceptionHandler {

    @ExceptionHandler(support = Exception.class)
    public ErrorResponse handleDefaultException(Exception e) {
        return SocketResponse.error(e.getClass().getSimpleName(), e.getMessage());
    }
}
