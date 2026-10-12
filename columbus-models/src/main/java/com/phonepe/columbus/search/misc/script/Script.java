package com.phonepe.columbus.search.misc.script;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Data
@Builder
@Jacksonized
public class Script {

    private String language;

    private String id;
    private String source;

    @Singular(ignoreNullCollections = true)
    private Map<@NonNull String, Object> parameters;
}
