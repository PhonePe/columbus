package com.phonepe.platform.columbus.core.services.impl;

import com.google.common.collect.Sets;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCacheKey;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCacheRegistry;
import com.phonepe.platform.columbus.core.commands.category.CategoryEntityCommands;
import com.phonepe.platform.columbus.core.commands.category.CategoryIndexManageCommands;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.forest.CategoryTree;
import com.phonepe.platform.columbus.core.services.CategoryService;
import com.phonepe.platform.columbus.core.utils.CategoryValidationUtils;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.category.Platform;
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
import java.util.List;
import java.util.Set;

@Slf4j
@Singleton
@AllArgsConstructor
public class CategoryRequestValidatorService implements CategoryService {
    private final CategoryService categoryService;
    private final CategoryEntityCommands categoryEntityCommands;
    private final CategoryIndexManageCommands categoryIndexManageCommands;
    private final ColumbusCategoryCacheRegistry columbusCategoryCacheRegistry;

    @Override
    public void create(CreateCategoryRequest createRequest) {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, createRequest.getTenantType().name());
        CategoryValidationUtils.validateIndexExists(categoryIndexManageCommands.existsIndex(indexName));
        CategoryValidationUtils.validateIfCategoryNotPresent(
            columbusCategoryCacheRegistry.getColumbusCategoryCache(createRequest.getTenantType())
                .get(ColumbusCategoryCacheKey.forTenant(createRequest.getTenantType())),
            createRequest.getId());
        categoryService.create(createRequest);
    }

    @Override
    public Category get(SearchCategoryRequest searchRequest) {
        return categoryService.get(searchRequest);
    }

    @Override
    public List<Category> search(SearchCategoryRequest searchRequest) {
        return categoryService.search(searchRequest);
    }

    @Override
    public void update(UpdateCategoryFieldsRequest categoryUpdateEntityFieldsRequest) {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, categoryUpdateEntityFieldsRequest.getTenantType().name());
        CategoryValidationUtils.validateIndexExists(categoryIndexManageCommands.existsIndex(indexName));
        CategoryValidationUtils.validateIfCategoriesExist(
            columbusCategoryCacheRegistry.getColumbusCategoryCache(categoryUpdateEntityFieldsRequest.getTenantType())
                .get(ColumbusCategoryCacheKey.forTenant(categoryUpdateEntityFieldsRequest.getTenantType())),
            Sets.newHashSet(categoryUpdateEntityFieldsRequest.getDocumentId()));
        categoryService.update(categoryUpdateEntityFieldsRequest);
    }

    @Override
    public void delete(DeleteCategoryRequest deleteRequest) {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, deleteRequest.getTenantType().name());
        CategoryValidationUtils.validateIndexExists(categoryIndexManageCommands.existsIndex(indexName));
        CategoryValidationUtils.validateIfCategoriesExist(
            columbusCategoryCacheRegistry.getColumbusCategoryCache(deleteRequest.getTenantType())
                .get(ColumbusCategoryCacheKey.forTenant(deleteRequest.getTenantType())),
            Sets.newHashSet(deleteRequest.getDocumentId()));

        categoryService.delete(deleteRequest);
    }

    @Override
    public void initialise(InitialiseServiceRequest categoryInitialiseRequest) {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, categoryInitialiseRequest.getTenantType().name());
        CategoryValidationUtils.validateIndexDoesNotExist(categoryIndexManageCommands.existsIndex(indexName));
        categoryService.initialise(categoryInitialiseRequest);
    }

    @Override
    public Boolean updateMapping(UpdateMappingServiceRequest updateRequest) {
        return categoryService.updateMapping(updateRequest);
    }

    @Override
    public void unLink(final UpdateCategoryLinkRequest categoryLinkUnlinkRequest) {
        validateCategoryUnLinking(categoryLinkUnlinkRequest);
        categoryService.unLink(categoryLinkUnlinkRequest);
    }

    @Override
    public void link(final UpdateCategoryLinkRequest categoryLinkUnlinkRequest) {
        validateCategoryLinking(categoryLinkUnlinkRequest);
        categoryService.link(categoryLinkUnlinkRequest);
    }

    private void validateCategoryLinking(final UpdateCategoryLinkRequest categoryLinkUnlinkRequest) {
        CategoryTree categoryTree = getServiceTree(categoryLinkUnlinkRequest.getTenantType());
        Set<String> categories = Sets.newHashSet(categoryLinkUnlinkRequest.getEdge().getSource(),
            categoryLinkUnlinkRequest.getEdge().getDestination());

        CategoryValidationUtils.validateIfCategoriesExist(categoryTree, categories);
        CategoryValidationUtils.validateIfEdgeAlreadyExists(categoryLinkUnlinkRequest.getEdge(), categoryTree);
        CategoryValidationUtils.validateCycleCreation(categoryLinkUnlinkRequest.getEdge(), categoryTree);
        // this check will prevent from creation of multiple parent nodes - if we have soe case we need to disable this check
        CategoryValidationUtils.validateIfParentExists(categoryTree,
            categoryLinkUnlinkRequest.getEdge().getDestination());
    }

    private void validateCategoryUnLinking(final UpdateCategoryLinkRequest categoryLinkUnlinkRequest) {
        CategoryTree categoryTree = getServiceTree(categoryLinkUnlinkRequest.getTenantType());
        Set<String> categories = Sets.newHashSet(categoryLinkUnlinkRequest.getEdge().getSource(),
            categoryLinkUnlinkRequest.getEdge().getDestination());

        CategoryValidationUtils.validateIfCategoriesExist(categoryTree, categories);
    }

    private CategoryTree getServiceTree(final ColumbusTenantType columbusTenantType) {
        return categoryEntityCommands.getTree(columbusTenantType, List.of(Platform.values()));
    }
}
