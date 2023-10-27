package com.phonepe.platform.columbus.search.query;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class BaseQuery implements Query {

    private String name;

    private Float boost;
}
