package com.phonepe.commons.columbus.search.query;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class FieldQuery extends AbstractQuery {

    private String field;
}
