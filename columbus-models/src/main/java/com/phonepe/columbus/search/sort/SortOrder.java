package com.phonepe.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonClassDescription;

@JsonClassDescription("Enum defining the sort order direction for ordering search results, aggregations, or buckets. ASC for ascending order and DESC for descending order.")
public enum SortOrder {
    ASC,
    DESC,
}
