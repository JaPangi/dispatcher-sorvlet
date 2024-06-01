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

package io.wwan13.dispatchersorvlet.sorvlet.processor.stub;

import io.wwan13.dispatchersorvlet.sorvlet.ComponentScanner;
import io.wwan13.dispatchersorvlet.sorvlet.container.ControllerContainer;

import java.lang.annotation.Annotation;
import java.util.Set;

public class StubComponentScanner implements ComponentScanner {

    @Override
    public Set<Class<?>> scanComponentsWithAnnotation(
            Class<? extends Annotation> targetAnnotation,
            String scanBasePackages
    ) {
        return Set.of(
                ControllerContainer.TestController.class,
                ControllerContainer.Test2Controller.class
        );
    }
}
