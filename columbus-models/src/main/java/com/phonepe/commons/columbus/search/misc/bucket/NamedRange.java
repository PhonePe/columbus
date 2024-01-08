package com.phonepe.commons.columbus.search.misc.bucket;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class NamedRange {

    private String name;

    private Double from;
    private Double to;
}
