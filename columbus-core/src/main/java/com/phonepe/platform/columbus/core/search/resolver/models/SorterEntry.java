package com.phonepe.platform.columbus.core.search.resolver.models;

import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SorterEntry {
    @Valid
    @NotNull
    @Singular
    private List<Sorter> sorters;

    private String reference;
}
