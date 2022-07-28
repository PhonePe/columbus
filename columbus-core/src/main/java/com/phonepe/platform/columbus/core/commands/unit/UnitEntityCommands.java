package com.phonepe.platform.columbus.core.commands.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grookage.jelastic.core.repository.ElasticRepository;
import com.phonepe.platform.columbus.core.commands.EntityCommands;
import com.phonepe.platform.columbus.core.models.operations.EntitiesSearchOperation;
import com.phonepe.platform.columbus.core.models.operations.UnitSearchOperation;
import com.phonepe.platform.columbus.core.search.resolver.SearchQueryBuilder;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.models.core.search.value.ReferenceMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UnitEntityCommands extends EntityCommands {
    private final SearchQueryBuilder searchQueryBuilder;

    public UnitEntityCommands(ElasticRepository elasticRepository, ObjectMapper mapper, SearchQueryBuilder searchQueryBuilder) {
        super(elasticRepository, mapper);
        this.searchQueryBuilder = searchQueryBuilder;
    }

    public <T> List<T> search(final UnitSearchOperation<T> operation) {
        return search(processSelection(operation));
    }

    public <T> List<T> elasticSearch(final UnitSearchOperation<T> operation) {
        return elasticSearch(processSelection(operation));
    }

    public <T> List<T> jElasticSearch(final UnitSearchOperation<T> operation) {
        return jElasticSearch(processSelection(operation));
    }

    private <T> EntitiesSearchOperation<T> processSelection(final UnitSearchOperation<T> operation) {
        ResolvedFilter filter = searchQueryBuilder.buildFilter(operation.getFilterSelections(), operation.getQueryFilter());
        ReferenceMap references = new ReferenceMap();
        filter.getReferences().forEach(entry -> references.put(entry.getName(), entry.getValue()));
        return EntitiesSearchOperation.<T>builder()
            .entityType(operation.getEntityType())
            .indexName(operation.getIndexName())
            .valueClassType(operation.getValueClassType())
            .routingKeys(operation.getRoutingKeys())
            .filter(filter.getFilter())
            .sorters(searchQueryBuilder.buildSorters(operation.getSorterSelections()))
            .references(references)
            .preprocessors(operation.getPreprocessors())
            .query(operation.getQuery())
            .pageNumber(operation.getPageNumber())
            .pageSize(operation.getPageSize())
            .build();
    }
}
