package com.phonepe.platform.columbus.search.filter;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class FieldFilter implements Filter {

    private String field;
}
