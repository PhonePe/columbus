package com.phonepe.platform.columbus.core.search.value;

import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.search.value.ReferenceMap;
import com.phonepe.platform.columbus.models.core.search.value.ReferencedValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValueExtractor<T> extends ValueGetter<T> {
    private final ReferenceMap references;

    @Override
    public T visit(ReferencedValue<T> value) {
        if (references == null || !references.containsKey(value.getName())) {
            throw ColumbusException.error(
                ColumbusCoreErrorCode.NO_REFERENCE_FOUND,
                "No value found for reference: %s".formatted(value.getName())
            );
        }
        return references.getTyped(value.getName());
    }
}
