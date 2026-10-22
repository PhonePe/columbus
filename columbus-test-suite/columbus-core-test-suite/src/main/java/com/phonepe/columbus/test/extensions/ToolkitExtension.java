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
package com.phonepe.columbus.test.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.core.toolkit.ToolkitHelpers;
import com.phonepe.columbus.core.toolkit.ToolkitSettings;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.test.annotations.WithToolkit;
import com.phonepe.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.columbus.test.value.ValueProps;
import com.phonepe.columbus.type.Types;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static com.phonepe.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

public class ToolkitExtension extends AnnotatedValueExtension<Toolkit> {

    @Override
    protected Toolkit initialize(final ExtensionContext context) {
        final var store = getValueStore(context);
        final var mapperProps = getAnnotation(context).map(WithToolkit::mapper).orElse(null);
        final var mapper = getRequiredValue(store, null, mapperProps, ObjectMapper.class);
        final var initializer = ToolkitHelpers.initializer();
        initializer.use(mapper);
        initializer.use(ToolkitSettings.defaults());
        initializer.use(Types.typeRegistry(Registries.empty()));
        return initializer.get();
    }

    @Override
    protected Class<Toolkit> getType() {
        return Toolkit.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context).map(WithToolkit::value);
    }

    private static Optional<WithToolkit> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithToolkit.class);
    }
}
