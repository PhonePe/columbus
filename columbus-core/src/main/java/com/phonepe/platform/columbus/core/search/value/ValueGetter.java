package com.phonepe.platform.columbus.core.search.value;

import com.phonepe.platform.columbus.models.core.search.value.EmbeddedValue;
import com.phonepe.platform.columbus.models.core.search.value.ExactValue;
import com.phonepe.platform.columbus.models.core.search.value.ReferencedValue;
import com.phonepe.platform.columbus.models.core.search.value.Value.ValueVisitor;

public class ValueGetter<T> implements ValueVisitor<T, T> {
    @Override
    public T visit(ExactValue<T> value) {
        return value.getValue();
    }

    @Override
    public T visit(ReferencedValue<T> value) {
        return null;
    }

    @Override
    public T visit(EmbeddedValue<T> value) {
        return value.getValue();
    }
}
