package com.phonepe.platform.columbus.core.search.value;

import com.phonepe.platform.columbus.models.core.search.value.EmbeddedValue;
import com.phonepe.platform.columbus.models.core.search.value.ExactValue;
import com.phonepe.platform.columbus.models.core.search.value.ReferencedValue;
import com.phonepe.platform.columbus.models.core.search.value.Value;

public class ValueIsEnclosed<T> implements Value.ValueVisitor<T, Boolean> {
    @Override
    public Boolean visit(final ExactValue<T> value) {
        return true;
    }

    @Override
    public Boolean visit(final ReferencedValue<T> value) {
        return false;
    }

    @Override
    public Boolean visit(final EmbeddedValue<T> value) {
        return true;
    }
}
