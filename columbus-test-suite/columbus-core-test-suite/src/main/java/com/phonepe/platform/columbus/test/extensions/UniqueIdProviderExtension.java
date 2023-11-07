package com.phonepe.platform.columbus.test.extensions;

import com.phonepe.platform.columbus.dao.id.Ids;
import com.phonepe.platform.columbus.dao.id.ValueId;
import com.phonepe.platform.columbus.test.annotations.WithUniqueTestId;
import com.phonepe.platform.columbus.test.value.IndexedStringProviderExtension;
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
