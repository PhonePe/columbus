package com.phonepe.platform.columbus.search.filter.range;

import com.phonepe.platform.columbus.search.filter.FieldFilter;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class RangeFilter extends FieldFilter {

    private Object value;
}
