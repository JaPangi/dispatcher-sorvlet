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

package io.wwan13.dispatchersorvlet.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UuidGenerator {

    private static final String SHA_256_ALGORITHM = "SHA-256";

    private UuidGenerator() {
        throw new IllegalStateException("Cannot instantiate a utility class!");
    }

    public static String shortUuid() {
        String uuidString = UUID.randomUUID().toString();

        byte[] uuidStringBytes = uuidString.getBytes(StandardCharsets.UTF_8);
        byte[] hashBytes;

        MessageDigest messageDigest = getSha256Instance();
        hashBytes = messageDigest.digest(uuidStringBytes);

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 4; j++) {
            sb.append(String.format("%02x", hashBytes[j]));
        }

        return sb.toString();
    }

    private static MessageDigest getSha256Instance() {
        try {
            return MessageDigest.getInstance(SHA_256_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("cannot get sha-256 instance");
        }
    }
}
