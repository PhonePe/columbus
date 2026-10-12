package com.phonepe.columbus.elastic9.storage.configuration;

import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@Jacksonized
@SuperBuilder
public class IndexConfiguration {

    @Valid
    @Singular(ignoreNullCollections = true, value = "indexMapping")
    private final Map<@NonNull @NotBlank String, @NonNull @NotBlank String> registry;
}
