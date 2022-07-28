package com.phonepe.platform.columbus.core.utils;

import com.grookage.jelastic.core.models.query.sorter.FieldSorter;
import com.grookage.jelastic.core.models.query.sorter.SortOrder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JElasticSorterUtils {
    public FieldSorter fieldSorter(String field, SortOrder order, int priority) {
        return new FieldSorter(priority, order, field);
    }

    public SortOrder sortOrder(com.phonepe.platform.columbus.models.core.search.sorter.SortOrder order) {
        return switch (order) {
            case DESC -> SortOrder.DESC;
            case ASC -> SortOrder.ASC;
        };
    }
}
