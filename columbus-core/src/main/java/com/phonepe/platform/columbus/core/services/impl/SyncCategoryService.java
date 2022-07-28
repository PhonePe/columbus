package com.phonepe.platform.columbus.core.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCacheKey;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCacheRegistry;
import com.phonepe.platform.columbus.core.commands.category.CategoryEntityCommands;
import com.phonepe.platform.columbus.core.commands.category.CategoryIndexManageCommands;
import com.phonepe.platform.columbus.core.constants.OperationConstants;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.forest.CategoryTree;
import com.phonepe.platform.columbus.core.models.operations.CreateIndexOperation;
import com.phonepe.platform.columbus.core.models.operations.CreateTemplateOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityDeleteOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityFieldsUpdateOperation;
import com.phonepe.platform.columbus.core.models.operations.EntitySaveOperation;
import com.phonepe.platform.columbus.core.services.CategoryService;
import com.phonepe.platform.columbus.core.utils.CategoryTransformUtils;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.category.StoredCategory;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusErrorCode;
import com.phonepe.platform.columbus.models.core.request.category.CreateCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.DeleteCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.InitialiseServiceRequest;
import com.phonepe.platform.columbus.models.core.request.category.SearchCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryFieldsRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryLinkRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateMappingServiceRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Singleton
@AllArgsConstructor
public class SyncCategoryService implements CategoryService {
    private final CategoryEntityCommands categoryEntityCommands;
    private final CategoryIndexManageCommands categoryIndexManageCommands;
    private final ColumbusCategoryCacheRegistry columbusCategoryCacheRegistry;

    @Override
    public void create(CreateCategoryRequest createRequest) {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, createRequest.getTenantType().name());
        categoryEntityCommands.entitySave(EntitySaveOperation.<StoredCategory>builder()
            .indexName(indexName)
            .entityType(EntityType.CATEGORY)
            .documentId(createRequest.getId())
            .value(CategoryTransformUtils.toStorageEntity(createRequest))
            .build());

        columbusCategoryCacheRegistry.getColumbusCategoryCache(createRequest.getTenantType())
            .refresh(ColumbusCategoryCacheKey.forTenant(createRequest.getTenantType()));
    }

    @Override
    public Category get(SearchCategoryRequest searchRequest) {
        List<Category> categoryEntities = search(searchRequest);
        return categoryEntities.isEmpty() ? null : categoryEntities.get(0);
    }

    @Override
    @SuppressWarnings("java:S3958") // SonarQube fix
    public List<Category> search(SearchCategoryRequest searchRequest) {
        CategoryTree categoryTree = columbusCategoryCacheRegistry.getColumbusCategoryCache(searchRequest.getTenantType())
            .get(ColumbusCategoryCacheKey.forTenant(searchRequest.getTenantType()));
        return searchRequest.getDocumentIds().stream().parallel()
            .map(categoryTree::getCategory)
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public void update(UpdateCategoryFieldsRequest categoryUpdateEntityFieldsRequest) {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            categoryUpdateEntityFieldsRequest.getTenantType().name());
        categoryEntityCommands.entityFieldsUpdate(EntityFieldsUpdateOperation.builder()
            .retryCount(OperationConstants.UPDATE_OPERATION_RETRY_COUNT)
            .documentId(categoryUpdateEntityFieldsRequest.getDocumentId())
            .indexName(indexName)
            .entityType(EntityType.CATEGORY)
            .entityFieldValueData(categoryUpdateEntityFieldsRequest.getFieldValueMap())
            .build());

        columbusCategoryCacheRegistry.getColumbusCategoryCache(categoryUpdateEntityFieldsRequest.getTenantType())
            .refresh(ColumbusCategoryCacheKey.forTenant(categoryUpdateEntityFieldsRequest.getTenantType()));
    }

    @Override
    public void delete(DeleteCategoryRequest deleteRequest) {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, deleteRequest.getTenantType().name());

        categoryEntityCommands.entityDelete(EntityDeleteOperation.builder()
            .documentId(deleteRequest.getDocumentId())
            .indexName(indexName)
            .entityType(EntityType.CATEGORY)
            .build());

        columbusCategoryCacheRegistry.getColumbusCategoryCache(deleteRequest.getTenantType())
            .delete(ColumbusCategoryCacheKey.forTenant(deleteRequest.getTenantType()));
    }

    @Override
    public void initialise(InitialiseServiceRequest categoryInitialiseRequest) {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, categoryInitialiseRequest.getTenantType().name());

        HashMap<String, Object> mappings = IndexUtils.getSource(
            "mappings/category_storage_entity.json",
            new TypeReference<>() {
            });
        HashMap<String, Object> analysis = IndexUtils.getSource(
            "analysers/category_index_analysis.json",
            new TypeReference<>() {
            });

        categoryIndexManageCommands.createIndexTemplate(CreateTemplateOperation.builder()
            .indexName(indexName)
            .entityType(EntityType.CATEGORY)
            .indexPattern(IndexUtils.getIndexPattern(indexName))
            .templateName(IndexUtils.getTemplateName(indexName))
            .analysis(analysis)
            .mappings(mappings)
            .noOfReplicas(categoryInitialiseRequest.getNoOfReplicas())
            .noOfRoutingShards(categoryInitialiseRequest.getNoOfRoutingShards())
            .noOfShards(categoryInitialiseRequest.getNoOfShards())
            .build());

        categoryIndexManageCommands.createIndex(CreateIndexOperation.builder()
            .entityType(EntityType.CATEGORY)
            .indexName(indexName)
            .build());

        columbusCategoryCacheRegistry.addCache(categoryInitialiseRequest.getTenantType());
    }

    @Override
    public Boolean updateMapping(UpdateMappingServiceRequest updateRequest) {
        throw ColumbusException.error(ColumbusErrorCode.BAD_REQUEST, "Operation not supported");
    }

    @Override
    public void unLink(final UpdateCategoryLinkRequest categoryLinkUnlinkRequest) {
        linkCategories(categoryLinkUnlinkRequest, false);
    }

    @Override
    public void link(final UpdateCategoryLinkRequest categoryLinkUnlinkRequest) {
        linkCategories(categoryLinkUnlinkRequest, true);
    }

    private void linkCategories(UpdateCategoryLinkRequest categoryLinkUnlinkRequest, boolean linkCategory) {
        String source = categoryLinkUnlinkRequest.getEdge().getSource();

        Category categoryEntity = categoryEntityCommands.getById(categoryLinkUnlinkRequest.getTenantType(), source)
            .orElseThrow(() -> ColumbusException.error(ColumbusCoreErrorCode.RECORD_NOT_FOUND,
                "Record not found"));
        //update map
        Map<String, Object> updateReq = new HashMap<>();

        // change timestamp
        updateReq.put("updateAt", System.currentTimeMillis());

        // change outgoing categories
        List<String> outgoingEdges = categoryEntity.getOutgoingCategories();
        if (linkCategory) {
            outgoingEdges.add(categoryLinkUnlinkRequest.getEdge().getDestination());
        } else {
            outgoingEdges.remove(categoryLinkUnlinkRequest.getEdge().getDestination());
        }
        updateReq.put("outgoingCategories", outgoingEdges);

        // call update
        update(UpdateCategoryFieldsRequest.builder()
            .tenantType(categoryLinkUnlinkRequest.getTenantType())
            .fieldValueMap(updateReq)
            .documentId(categoryEntity.getDocumentId())
            .build());
    }
}
