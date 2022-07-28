package com.phonepe.platform.columbus.core.models.operations;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grookage.jelastic.core.models.index.IndexProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Jacksonized
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateTemplateOperation extends StorageOperation {
    @NotNull
    @NonNull
    private String templateName;

    @NotEmpty
    @NonNull
    private String indexPattern;

    // index properties
    @Min(1)
    private int noOfShards;

    @Min(1)
    private int noOfReplicas;

    @Min(1)
    private int noOfRoutingShards;

    // analyzer settings
    private HashMap<String, Object> analysis;

    // mapping elements
    private HashMap<String, Object> mappings;

    @JsonIgnore
    public IndexProperties getIndexProperties() {
        return IndexProperties.builder()
            .noOfReplicas(noOfReplicas)
            .noOfShards(noOfShards)
            .enableRequestCache(true)
            .noOfRoutingShards(noOfRoutingShards)
            .build();
    }

    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.CREATE_TEMPLATE;
    }
}
