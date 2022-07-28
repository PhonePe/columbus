package com.phonepe.platform.columbus.core.models.operations;

import com.phonepe.platform.columbus.core.search.preprocessor.Preprocessor;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import com.phonepe.platform.columbus.models.core.search.value.ReferenceMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class EntitiesSearchOperation<T> extends StorageOperation {
    private Class<T> valueClassType;

    @Singular("routing")
    private Set<String> routingKeys;

    private Filter filter;

    @Singular
    private Set<Sorter> sorters;

    private ReferenceMap references;

    @Singular
    private List<Preprocessor> preprocessors;

    private String query;

    @Min(0)
    private int pageNumber;

    @Min(1)
    private int pageSize;

    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.ENTITIES_SEARCH;
    }
}
