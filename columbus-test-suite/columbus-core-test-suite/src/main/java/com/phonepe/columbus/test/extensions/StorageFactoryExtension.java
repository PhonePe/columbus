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

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.internal.storage.factory.ColumbusStorageClientFactory;
import com.phonepe.columbus.storage.Storages;
import com.phonepe.columbus.storage.settings.StorageSettings;
import com.phonepe.columbus.test.annotations.WithStorageFactory;
import com.phonepe.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.columbus.test.value.ValueProps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static com.phonepe.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageFactoryExtension extends AnnotatedValueExtension<ColumbusStorageClientFactory> {

    @NonNull
    @Builder.Default
    private final StorageSettings settings = StorageSettings.defaults();

    private Object toolkitKey;

    @Override
    protected ColumbusStorageClientFactory initialize(final ExtensionContext context) {
        final var store = getValueStore(context);
        final var toolkitProps = getAnnotation(context).map(WithStorageFactory::toolkit).orElse(null);
        final var toolkit = getRequiredValue(store, toolkitKey, toolkitProps, Toolkit.class);
        return Storages.storageClientFactory(settings, toolkit);
    }

    @Override
    protected Class<ColumbusStorageClientFactory> getType() {
        return ColumbusStorageClientFactory.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context).map(WithStorageFactory::value);
    }

    private static Optional<WithStorageFactory> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithStorageFactory.class);
    }
}
