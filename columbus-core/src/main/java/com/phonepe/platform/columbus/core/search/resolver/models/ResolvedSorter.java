package com.phonepe.platform.columbus.core.search.resolver.models;

import com.phonepe.platform.columbus.core.search.value.ReferenceMapEntry;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResolvedSorter {
    @Singular
    private List<Sorter> sorters;

    @Singular
    private List<ReferenceMapEntry> references;
}
