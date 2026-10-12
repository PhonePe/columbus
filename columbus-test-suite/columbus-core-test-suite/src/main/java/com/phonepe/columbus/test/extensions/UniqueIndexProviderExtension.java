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
