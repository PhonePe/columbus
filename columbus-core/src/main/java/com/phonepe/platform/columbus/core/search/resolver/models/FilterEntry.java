package com.phonepe.platform.columbus.core.search.resolver.models;

import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterEntry {
    @Valid
    @NotNull
    private Filter filter;

    @NotBlank
    private String reference;
}
