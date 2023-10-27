package com.phonepe.platform.columbus.search.query;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class FieldQuery extends BaseQuery {

    private String field;
}
