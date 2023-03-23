package com.phonepe.platform.columbus.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.models.operations.EntitiesSearchOperation;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.core.search.sorter.elastic.ElasticSorterBuilderContext;
import com.phonepe.platform.columbus.models.core.search.Page;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import com.phonepe.platform.columbus.models.core.search.value.ReferenceMap;
import lombok.experimental.UtilityClass;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

@UtilityClass
public class ElasticUtils {
    public <T> SearchRequest toElasticSearchRequest(EntitiesSearchOperation<T> operation, ObjectMapper mapper) {
        SearchRequest request = new SearchRequest(new String[]{operation.getIndexName()}, toElasticSearchSource(operation, mapper));
        request.routing(operation.getRoutingKeys().toArray(new String[0]));
        return request;
    }

    public <T> SearchSourceBuilder toElasticSearchSource(EntitiesSearchOperation<T> operation, ObjectMapper mapper) {
        SearchSourceBuilder source = new SearchSourceBuilder()
            .query(toElasticQuery(operation.getFilter(), operation.getReferences(), mapper))
            .from(operation.getPageNumber() * operation.getPageSize())
            .size(operation.getPageSize());
        toElasticSorters(operation.getSorters(), mapper, operation.getReferences()).forEach(source::sort);
        return source;
    }

    @SuppressWarnings({"java:S3958", "java:S1905"}) // SonarQube fix
    private List<SortBuilder<?>> toElasticSorters(Set<Sorter> sorters, ObjectMapper mapper, ReferenceMap references) {
        ElasticSorterBuilderContext sorterBuilder = ElasticSorterBuilderContext.withDefaults(mapper)
            .references(references)
            .build();
        return sorters.stream()
            .sorted()
            .map((Function<Sorter, SortBuilder<?>>) sorterBuilder::visit)
            .toList();
    }

    public QueryBuilder toElasticQuery(Filter filter, ReferenceMap references, ObjectMapper mapper) {
        return ElasticQueryBuilderContext.withDefaults(mapper)
            .references(references)
            .build()
            .visit(filter);
    }

    public <T> List<T> getResponse(SearchResponse response, Class<T> resultType) {
        return com.grookage.jelastic.core.utils.ElasticUtils.getResponse(response, resultType);
    }

    public  <T> Page<T> getPaginatedResponse(SearchResponse response, Class<T> resultType){
        var entities = com.grookage.jelastic.core.utils.ElasticUtils.getResponse(response, resultType);
        long totalCount = response.getHits().getTotalHits().value;
        return Page.<T>builder()
            .count(totalCount)
            .entities(entities)
            .build();
    }
}
