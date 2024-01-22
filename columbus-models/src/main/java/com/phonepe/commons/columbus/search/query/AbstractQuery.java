package com.phonepe.commons.columbus.search.query;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractQuery implements Query {

    private String name;

    private Float boost;
}
