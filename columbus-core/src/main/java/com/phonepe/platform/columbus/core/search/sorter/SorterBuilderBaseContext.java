package com.phonepe.platform.columbus.core.search.sorter;

import com.phonepe.platform.columbus.core.search.value.ValueExtractor;
import com.phonepe.platform.columbus.core.visitors.Context;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import com.phonepe.platform.columbus.models.core.search.value.ReferenceMap;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class SorterBuilderBaseContext<R, C extends SorterBuilderBaseContext<R, C>>
    extends Context<R, Sorter, C> {
    @Getter(AccessLevel.PROTECTED)
    private final ReferenceMap references;

    public <T> T extractValue(Value<T> value) {
        return value.accept(new ValueExtractor<>(references));
    }
}
