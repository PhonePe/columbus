package com.phonepe.platform.columbus.core.utils;

import lombok.experimental.UtilityClass;
import org.elasticsearch.search.sort.SortOrder;

@UtilityClass
public class EsUtil {
    public SortOrder esSortOrder(com.phonepe.platform.columbus.models.core.search.sorter.SortOrder order) {
        return switch (order) {
            case DESC -> SortOrder.DESC;
            case ASC -> SortOrder.ASC;
        };
    }
}
