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

import com.phonepe.columbus.dao.id.Ids;
import com.phonepe.columbus.dao.id.ValueId;
import com.phonepe.columbus.test.annotations.WithUniqueTestId;
import com.phonepe.columbus.test.value.IndexedStringProviderExtension;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqueIdProviderExtension extends IndexedStringProviderExtension<ValueId> {

    public static final String DEFAULT_FORMAT = "columbus-id-%d";

    private static final Namespace NAMESPACE = Namespace.create(UniqueIdProviderExtension.class);

    @Builder.Default
    private String idFormat = DEFAULT_FORMAT;

    @Override
    protected Class<ValueId> getType() {
        return ValueId.class;
    }

    @Override
    protected ValueId createInstance(final String string) {
        return Ids.value(string);
    }

    @Override
    protected Namespace getNamespace(final ExtensionContext context) {
        return NAMESPACE;
    }

    @Override
    protected String getFormat(final ExtensionContext context) {
        return getAnnotation(context)
                .map(WithUniqueTestId::format)
                .orElse(idFormat);
    }

    private static Optional<WithUniqueTestId> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithUniqueTestId.class);
    }
}
