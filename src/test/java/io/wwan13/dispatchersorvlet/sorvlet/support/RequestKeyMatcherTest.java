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

package io.wwan13.dispatchersorvlet.sorvlet.support;

import io.wwan13.dispatchersorvlet.UnitTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class RequestKeyMatcherTest extends UnitTest {

    private static final RequestKeyMatcher requestKeyMatcher = new RequestKeyMatcher();

    @ParameterizedTest
    @CsvSource({
            "KEY_ITEM_GET_{itemId}, KEY_ITEM_GET_3, true",
            "KEY_ITEM_GET_{itemId}_{productId}, KEY_ITEM_GET_3_4, true",
            "KEY_ITEM_GET_{itemId}_DO, KEY_ITEM_GET_3_DO, true",
            "KEY_ITEM_GET_itemId, KEY_ITEM_GET_3, false",
            "KEY_ITEM_GET, KEY_ITEM_GET_3, false"
    })
    void should_JudgeIsRegisteredKey(
            final String registered,
            final String entered,
            final boolean expected
    ) {
        // given, when
        boolean result = requestKeyMatcher.matches(registered, entered);

        // then
        assertThat(result).isEqualTo(expected);
    }
}