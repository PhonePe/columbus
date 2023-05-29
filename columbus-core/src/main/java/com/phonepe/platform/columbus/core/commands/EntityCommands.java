package com.phonepe.platform.columbus.core.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grookage.jelastic.core.models.search.IdSearchRequest;
import com.grookage.jelastic.core.models.search.SearchRequest;
import com.grookage.jelastic.core.models.source.DeleteEntityRequest;
import com.grookage.jelastic.core.models.source.EntitySaveRequest;
import com.grookage.jelastic.core.models.source.GetSourceRequest;
import com.grookage.jelastic.core.models.source.UpdateEntityRequest;
import com.grookage.jelastic.core.models.source.UpdateFieldRequest;
import com.grookage.jelastic.core.repository.ElasticRepository;
import com.phonepe.platform.columbus.core.constants.SearchConstants;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.models.operations.EntitiesDeleteOperation;
import com.phonepe.platform.columbus.core.models.operations.EntitiesSearchOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityDeleteOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityFieldsUpdateOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityGetOperation;
import com.phonepe.platform.columbus.core.models.operations.EntitySaveOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityUpdateOperation;
import com.phonepe.platform.columbus.core.models.operations.MultiGetOperation;
import com.phonepe.platform.columbus.core.utils.ElasticUtils;
import com.phonepe.platform.columbus.core.utils.JElasticUtils;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.search.Page;
import com.phonepe.platform.columbus.models.core.search.value.ReferenceMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;

@Slf4j
@Getter
@SuperBuilder
@AllArgsConstructor
public abstract class EntityCommands implements ColumbusCommands {
    private final ElasticRepository elasticRepository;
    private final ObjectMapper mapper;

    public <T> void entitySave(final EntitySaveOperation<T> entitySaveOperation) {
        try {
            EntitySaveRequest entitySaveRequest = EntitySaveRequest.builder()
                .indexName(entitySaveOperation.getIndexName())
                .referenceId(entitySaveOperation.getDocumentId())
                .value(MapperUtils.writeValueAsString(entitySaveOperation.getValue()))
                .routingKey(entitySaveOperation.getRoutingKey())
                .build();

            this.getElasticRepository().save(entitySaveRequest);
        } catch (Exception ex) {
            log.error("Error while creating template for {}", entitySaveOperation.getIndexName(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ENTITY_CREATE_ERROR, ex);
        }
    }

    public <T> List<T> search(final EntitiesSearchOperation<T> entitiesSearchOperation) {
        preprocess(entitiesSearchOperation);
        try {
            return requestJElasticSearch(entitiesSearchOperation);
        } catch (Exception e) {
            return requestElasticSearch(entitiesSearchOperation);
        }
    }

    public <T> List<T> elasticSearch(final EntitiesSearchOperation<T> entitiesSearchOperation) {
        preprocess(entitiesSearchOperation);
        return requestElasticSearch(entitiesSearchOperation);
    }

    public <T> Page<T> paginatedElasticSearch(final EntitiesSearchOperation<T> entitiesSearchOperation){
        preprocess(entitiesSearchOperation);
        return requestPaginatedElasticSearch(entitiesSearchOperation);
    }

    public <T> List<T> jElasticSearch(final EntitiesSearchOperation<T> entitiesSearchOperation) {
        preprocess(entitiesSearchOperation);
        return requestJElasticSearch(entitiesSearchOperation);
    }

    private <T> void preprocess(final EntitiesSearchOperation<T> operation) {
        if (operation.getReferences() == null) {
            operation.setReferences(new ReferenceMap());
        }
        ReferenceMap references = operation.getReferences();
        if (operation.getQuery() != null) {
            references.put(SearchConstants.DEFAULT_QUERY_REFERENCE, operation.getQuery());
        }
        try {
            operation.getPreprocessors().forEach(preprocessor -> preprocessor.process(references));
        } catch (Exception e) {
            log.error("Error while preprocessing for {}", operation.getIndexName(), e);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.PREPROCESS_ERROR, e);
        }
    }

    private <T> List<T> requestJElasticSearch(final EntitiesSearchOperation<T> entitiesSearchOperation) {
        try {
            SearchRequest<T> searchRequest = JElasticUtils.toJElasticSearchRequest(entitiesSearchOperation, mapper);
            return this.getElasticRepository().enumeratedSearch(searchRequest).getEntities();
        } catch (Exception ex) {
            log.error("Error while executing search operation for {}", entitiesSearchOperation.getIndexName(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, ex);
        }
    }

    private <T> List<T> requestElasticSearch(final EntitiesSearchOperation<T> entitiesSearchOperation) {
        org.elasticsearch.action.search.SearchRequest searchRequest = ElasticUtils.toElasticSearchRequest(entitiesSearchOperation, mapper);
        try {
            return ElasticUtils.getResponse(
                this.getElasticRepository()
                    .getElasticClient()
                    .getClient()
                    .search(searchRequest, RequestOptions.DEFAULT),
                entitiesSearchOperation.getValueClassType()
            );
        } catch (IOException e) {
            log.error("Failed to query ElasticSearch", e);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ELASTIC_SEARCH_ERROR, e);
        } catch (Exception ex) {
            log.error("Error while executing search operation for {}", entitiesSearchOperation.getIndexName(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, ex);
        }
    }

    private <T> Page<T> requestPaginatedElasticSearch(final EntitiesSearchOperation<T> entitiesSearchOperation){
        org.elasticsearch.action.search.SearchRequest searchRequest = ElasticUtils.toElasticSearchRequest(entitiesSearchOperation, mapper);
        try {
            var response = this.getElasticRepository().getElasticClient().getClient().search(searchRequest, RequestOptions.DEFAULT);
            return ElasticUtils.getPaginatedResponse(response,entitiesSearchOperation.getValueClassType());
        } catch (IOException e) {
            log.error("Failed to query ElasticSearch", e);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ELASTIC_SEARCH_ERROR, e);
        } catch (Exception ex){
            log.error("Error while executing search operation for {}", entitiesSearchOperation.getIndexName(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, ex);
        }
    }

    public <T> List<T> multiGetByIds(final MultiGetOperation<T> multiGetOperation) {
        try {
            IdSearchRequest<T> idSearchRequest = IdSearchRequest.<T>builder()
                .ids(multiGetOperation.getDocumentIds())
                .index(multiGetOperation.getIndexName())
                .klass(multiGetOperation.getEntityClassType())
                .build();

            return this.getElasticRepository().searchByIds(idSearchRequest);
        } catch (Exception ex) {
            log.error("Error while doing multiGetByIds for {}", multiGetOperation.getIndexName(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, ex);
        }
    }


    public void entityFieldsUpdate(final EntityFieldsUpdateOperation entityFieldsUpdateOperation) {
        try {
            UpdateFieldRequest updateFieldRequest = UpdateFieldRequest.builder()
                .indexName(entityFieldsUpdateOperation.getIndexName())
                .referenceId(entityFieldsUpdateOperation.getDocumentId())
                .fieldValueMap(entityFieldsUpdateOperation.getEntityFieldValueData())
                .retryCount(entityFieldsUpdateOperation.getRetryCount())
                .routingKey(entityFieldsUpdateOperation.getRoutingKey())
                .build();

            this.getElasticRepository().updateField(updateFieldRequest);
        } catch (Exception ex) {
            log.error("Error while updating entity for index {} and documentId {}", entityFieldsUpdateOperation.getIndexName(),
                entityFieldsUpdateOperation.getDocumentId(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ENTITY_UPDATE_ERROR, ex);
        }
    }

    public <T> void entityUpdate(final EntityUpdateOperation<T> entityUpdateOperation) {
        try {
            UpdateEntityRequest updateEntityRequest = UpdateEntityRequest.builder()
                .indexName(entityUpdateOperation.getIndexName())
                .referenceId(entityUpdateOperation.getDocumentId())
                .value(MapperUtils.writeValueAsString(entityUpdateOperation.getValue()))
                .routingKey(entityUpdateOperation.getRoutingKey())
                .build();

            this.getElasticRepository().update(updateEntityRequest);
        } catch (Exception ex) {
            log.error("Error while updating entity for {} and documentId {}", entityUpdateOperation.getIndexName(),
                entityUpdateOperation.getDocumentId(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ENTITY_UPDATE_ERROR, ex);
        }
    }

    public <T> Optional<T> entityGet(final EntityGetOperation<T> entityGetOperation) {
        try {
            GetSourceRequest<T> getSourceRequest = GetSourceRequest.<T>builder()
                .referenceId(entityGetOperation.getDocumentId())
                .indexName(entityGetOperation.getIndexName())
                .routingKey(entityGetOperation.getRoutingKey())
                .klass(entityGetOperation.getEntityClassType())
                .build();

            return this.getElasticRepository().get(getSourceRequest);
        } catch (Exception ex) {
            log.error("Error while fetching entity for {} and documentId {}", entityGetOperation.getIndexName(),
                entityGetOperation.getDocumentId(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, ex);
        }
    }

    public void entityDelete(final EntityDeleteOperation entityDeleteOperation) {
        try {
            DeleteEntityRequest deleteEntityRequest = DeleteEntityRequest.builder()
                .referenceId(entityDeleteOperation.getDocumentId())
                .indexName(entityDeleteOperation.getIndexName())
                .routingKey(entityDeleteOperation.getRoutingKey())
                .build();

            this.getElasticRepository().delete(deleteEntityRequest);
        } catch (Exception ex) {
            log.error("Error while deleting entity for {} and documentId {}", entityDeleteOperation.getIndexName(),
                entityDeleteOperation.getDocumentId(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ENTITY_DELETE_ERROR, ex);
        }
    }

    public BulkByScrollResponse deleteEntities( final EntitiesDeleteOperation entityDeleteOperation) {
        try {
            var deleteByQueryRequest = new DeleteByQueryRequest(
                entityDeleteOperation.getIndexName());

            deleteByQueryRequest.setQuery(
                QueryBuilders
                    .idsQuery()
                    .addIds(entityDeleteOperation.getDocumentId().toArray(new String[]{}))
                    .queryName("deleteUnitsRequest")
            );

            return this.getElasticRepository().getElasticClient()
                .getClient().deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (Exception ex) {
            log.error("Error while deleting entities for {} and documentIds {}",
                entityDeleteOperation.getIndexName(),
                entityDeleteOperation.getDocumentId(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.ENTITY_DELETE_ERROR, ex);
        }
    }
}
