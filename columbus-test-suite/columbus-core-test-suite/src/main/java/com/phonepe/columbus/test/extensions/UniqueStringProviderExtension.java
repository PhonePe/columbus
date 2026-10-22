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

import com.phonepe.columbus.test.annotations.WithUniqueString;
import com.phonepe.columbus.test.value.IndexedStringProviderExtension;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqueStringProviderExtension extends IndexedStringProviderExtension<String> {

    public static final String DEFAULT_FORMAT = "string-%d";

    private static final Namespace NAMESPACE = Namespace.create(UniqueStringProviderExtension.class);

    @Builder.Default
    private String format = DEFAULT_FORMAT;

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return super.supportsParameter(parameterContext, extensionContext)
                && parameterContext.isAnnotated(WithUniqueString.Inject.class);
    }

    @Override
    protected Class<String> getType() {
        return String.class;
    }

    @Override
    protected String createInstance(final String string) {
        return string;
    }

    @Override
    protected Namespace getNamespace(final ExtensionContext context) {
        return NAMESPACE;
    }

    @Override
    protected String getFormat(final ExtensionContext context) {
        return getAnnotation(context)
                .map(WithUniqueString::format)
                .orElse(format);
    }

    private static Optional<WithUniqueString> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithUniqueString.class);
    }
}
