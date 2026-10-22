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

import com.phonepe.columbus.dao.index.IndexName;
import com.phonepe.columbus.dao.index.Indices;
import com.phonepe.columbus.test.annotations.WithUniqueTestIndex;
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
public class UniqueIndexProviderExtension extends IndexedStringProviderExtension<IndexName> {

    public static final String DEFAULT_FORMAT = "columbus.test.v%d.index";

    private static final Namespace NAMESPACE = Namespace.create(UniqueIndexProviderExtension.class);

    @Builder.Default
    private String nameFormat = DEFAULT_FORMAT;

    @Override
    protected Class<IndexName> getType() {
        return IndexName.class;
    }

    @Override
    protected IndexName createInstance(final String string) {
        return Indices.name(string);
    }

    @Override
    protected Namespace getNamespace(final ExtensionContext context) {
        return NAMESPACE;
    }

    @Override
    protected String getFormat(final ExtensionContext context) {
        return getAnnotation(context)
                .map(WithUniqueTestIndex::format)
                .orElse(nameFormat);
    }

    private static Optional<WithUniqueTestIndex> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithUniqueTestIndex.class);
    }
}
