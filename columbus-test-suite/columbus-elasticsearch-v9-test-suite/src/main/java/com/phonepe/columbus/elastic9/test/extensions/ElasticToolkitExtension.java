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
package com.phonepe.columbus.elastic9.test.extensions;

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkitHelpers;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.columbus.test.value.ValueProps;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static com.phonepe.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

public class ElasticToolkitExtension extends AnnotatedValueExtension<ElasticToolkit> {

    @Override
    protected ElasticToolkit initialize(final ExtensionContext context) {
        final var store = getValueStore(context);
        final var toolkitProps = getAnnotation(context).map(WithElasticToolkit::toolkit).orElse(null);
        final var toolkit = getRequiredValue(store, null, toolkitProps, Toolkit.class);
        return ElasticToolkitHelpers.defaultInstance(toolkit);
    }

    @Override
    protected Class<ElasticToolkit> getType() {
        return ElasticToolkit.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context).map(WithElasticToolkit::value);
    }

    private static Optional<WithElasticToolkit> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithElasticToolkit.class);
    }
}
