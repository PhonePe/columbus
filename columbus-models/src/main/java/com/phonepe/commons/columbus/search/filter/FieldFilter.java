package com.phonepe.commons.columbus.search.filter;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class FieldFilter implements Filter {

    private String field;
}
