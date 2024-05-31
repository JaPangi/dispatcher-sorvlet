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

public class RequestKeyMatcher {

    private static final String KEY_DELIMITER = "_";
    private static final String KEY_PARAMETER_PREFIX = "{";
    private static final String KEY_PARAMETER_SUFFIX = "}";

    public boolean matches(String registered, String entered) {
        String[] registeredElements = registered.split(KEY_DELIMITER);
        String[] enteredElements = entered.split(KEY_DELIMITER);

        if (registeredElements.length != enteredElements.length) {
            return false;
        }

        for (int i = 0; i < registeredElements.length; i++) {
            String registeredElement = registeredElements[i];
            String enteredElement = enteredElements[i];

            if (!registeredElement.equals(enteredElement) && !isKeyParameter(registeredElement)) {
                return false;
            }
        }

        return true;
    }

    private boolean isKeyParameter(String registeredElement) {
        return registeredElement.startsWith(KEY_PARAMETER_PREFIX) &&
                registeredElement.endsWith(KEY_PARAMETER_SUFFIX);
    }
}
