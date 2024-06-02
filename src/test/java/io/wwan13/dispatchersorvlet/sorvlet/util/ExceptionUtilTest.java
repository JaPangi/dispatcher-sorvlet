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

import io.wwan13.dispatchersorvlet.UnitTest;
import io.wwan13.dispatchersorvlet.exception.HandlerNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionUtilTest extends UnitTest {

    @Test
    void should_CalculateExceptionExtendedLevel() {
        // given
        final Class<? extends Exception> exception = Exception.class;
        final Class<? extends Exception> runtimeException = RuntimeException.class;
        final Class<? extends Exception> illegalStateException = IllegalStateException.class;
        final Class<? extends Exception> handlerNotFoundException = HandlerNotFoundException.class;

        // when
        int exceptionLevel = ExceptionUtil.getExceptionExtendedLevel(exception);
        int runtimeExceptionLevel = ExceptionUtil.getExceptionExtendedLevel(runtimeException);
        int illegalStateExceptionLevel = ExceptionUtil.getExceptionExtendedLevel(illegalStateException);
        int handlerNotFoundExceptionLevel = ExceptionUtil.getExceptionExtendedLevel(handlerNotFoundException);

        // then
        assertThat(exceptionLevel).isEqualTo(0);
        assertThat(runtimeExceptionLevel).isEqualTo(1);
        assertThat(illegalStateExceptionLevel).isEqualTo(2);
        assertThat(handlerNotFoundExceptionLevel).isEqualTo(2);
    }
}