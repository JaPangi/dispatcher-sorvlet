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

package io.wwan13.dispatchersorvlet.sorvlet.processor;

import io.wwan13.dispatchersorvlet.sorvlet.SocketControllerScanner;
import io.wwan13.dispatchersorvlet.sorvlet.annotation.SocketController;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Set;

public class ReflectionSocketControllerScanner implements SocketControllerScanner {

    @Override
    public Set<Class<?>> scanControllerClasses(String scanBasePackages) {
        Reflections reflections = new Reflections(
                scanBasePackages,
                new TypeAnnotationsScanner(),
                new SubTypesScanner()
        );

        return reflections.getTypesAnnotatedWith(SocketController.class);
    }
}
