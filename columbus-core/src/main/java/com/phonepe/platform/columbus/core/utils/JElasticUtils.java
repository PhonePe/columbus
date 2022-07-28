package com.phonepe.platform.columbus.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grookage.jelastic.core.models.query.Query;
import com.grookage.jelastic.core.models.query.filter.Filter;
import com.grookage.jelastic.core.models.query.paged.PageWindow;
import com.grookage.jelastic.core.models.query.sorter.Sorter;
import com.grookage.jelastic.core.models.search.SearchRequest;
import com.phonepe.platform.columbus.core.models.operations.EntitiesSearchOperation;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilderContext;
import com.phonepe.platform.columbus.core.search.sorter.jelastic.JElasticSorterBuilderContext;
import com.phonepe.platform.columbus.models.core.search.value.ReferenceMap;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@UtilityClass
public class JElasticUtils {
    public Filter toJElasticFilter(com.phonepe.platform.columbus.models.core.search.filter.Filter filter, ReferenceMap references, ObjectMapper mapper) {
        return JElasticFilterBuilderContext.withDefaults(mapper)
            .references(references)
            .build()
            .visit(filter);
    }

    public Set<Sorter> toJElasticSorters(Set<com.phonepe.platform.columbus.models.core.search.sorter.Sorter> sorters, ObjectMapper mapper) {
        final JElasticSorterBuilderContext sorterBuilder = JElasticSorterBuilderContext.withDefaults(mapper).build();
        return sorters.stream()
            .map(sorterBuilder::visit)
            .collect(Collectors.toCollection(TreeSet::new));
    }

    public PageWindow toJElasticPageWindow(int pageNumber, int pageSize) {
        return PageWindow.builder()
            .pageNumber(pageNumber)
            .pageSize(pageSize)
            .build();
    }

    public <T> Query toJElasticQuery(EntitiesSearchOperation<T> operation, ObjectMapper mapper) {
        return Query.builder()
            .filters(Collections.singleton(toJElasticFilter(operation.getFilter(), operation.getReferences(), mapper)))
            .sorters(toJElasticSorters(operation.getSorters(), mapper))
            .pageWindow(toJElasticPageWindow(operation.getPageNumber(), operation.getPageSize()))
            .build();
    }

    public <T> SearchRequest<T> toJElasticSearchRequest(EntitiesSearchOperation<T> operation, ObjectMapper mapper) {
        return SearchRequest.<T>builder()
            .index(operation.getIndexName())
            .klass(operation.getValueClassType())
            .query(toJElasticQuery(operation, mapper))
            .routingKeys(operation.getRoutingKeys())
            .build();
    }
}
