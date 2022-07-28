package com.phonepe.platform.columbus.core.models.operations;

import com.phonepe.platform.columbus.core.search.preprocessor.Preprocessor;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UnitSearchOperation<T> extends StorageOperation {
    private Class<T> valueClassType;

    @Singular("routing")
    private Set<String> routingKeys;

    @Singular
    private List<FilterSelection> filterSelections;

    @Singular
    private List<SorterSelection> sorterSelections;

    private Filter queryFilter;

    private String query;

    @Singular
    private List<Preprocessor> preprocessors;

    @Min(0)
    private int pageNumber;

    @Min(1)
    private int pageSize;

    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.UNIT_SEARCH;
    }
}
