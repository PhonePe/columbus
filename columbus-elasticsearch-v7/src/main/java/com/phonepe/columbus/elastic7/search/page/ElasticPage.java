package com.phonepe.columbus.elastic7.search.page;

import lombok.Data;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public class ElasticPage {

    private final Integer size;

    private final Integer from;

    @Singular(ignoreNullCollections = true)
    private final List<Object> afterKeys;
}
