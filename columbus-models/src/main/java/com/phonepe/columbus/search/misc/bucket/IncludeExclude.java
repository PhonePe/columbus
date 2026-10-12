package com.phonepe.columbus.search.misc.bucket;

import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
public class IncludeExclude {

    @Accessors(fluent = true)
    private boolean isRegex;

    @Singular(value = "include", ignoreNullCollections = true)
    private List<@NonNull String> include;

    @Singular(value = "exclude", ignoreNullCollections = true)
    private List<@NonNull String> exclude;
}
