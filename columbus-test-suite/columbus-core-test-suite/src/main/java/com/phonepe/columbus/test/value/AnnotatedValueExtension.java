/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.test.value;

import com.phonepe.columbus.test.utils.AnnotatedValueUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AnnotatedValueExtension<T> extends ValueExtension<T> {

    @Override
    protected boolean shouldUseRoot(final ExtensionContext context) {
        return getProps(context)
                .map(ValueProps::useRootContext)
                .orElse(false);
    }

    @Override
    protected Object getKey(final ExtensionContext context, final @Nullable ParameterContext parameterContext) {
        return Optional.ofNullable(parameterContext)
                .flatMap(parameter -> parameter.findAnnotation(ValueProps.Key.class))
                .flatMap(AnnotatedValueUtils::getKey)
                .or(() -> getProps(context)
                        .map(ValueProps::key)
                        .flatMap(AnnotatedValueUtils::getKey))
                .orElseGet(this::getType);
    }

    @Override
    protected boolean shouldCleanUp(final ExtensionContext context) {
        return getProps(context)
                .map(ValueProps::cleanUp)
                .orElse(true);
    }

    protected abstract Optional<ValueProps> getProps(final ExtensionContext context);
}
