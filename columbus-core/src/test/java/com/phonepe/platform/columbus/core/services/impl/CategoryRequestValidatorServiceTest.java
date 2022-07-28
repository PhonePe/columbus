package com.phonepe.platform.columbus.core.services.impl;

import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCache;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCacheKey;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCacheRegistry;
import com.phonepe.platform.columbus.core.commands.category.CategoryEntityCommands;
import com.phonepe.platform.columbus.core.commands.category.CategoryIndexManageCommands;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.core.models.forest.CategoryTree;
import com.phonepe.platform.columbus.core.services.CategoryService;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.category.Platform;
import com.phonepe.platform.columbus.models.core.dag.Edge;
import com.phonepe.platform.columbus.models.core.request.category.CreateCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.DeleteCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.InitialiseServiceRequest;
import com.phonepe.platform.columbus.models.core.request.category.SearchCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryFieldsRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryLinkRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateMappingServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRequestValidatorServiceTest {
    @Mock
    CategoryService categoryService;
    @Mock
    CategoryEntityCommands categoryEntityCommands;
    @Mock
    CategoryIndexManageCommands categoryIndexManageCommands;
    @Mock
    ColumbusCategoryCacheRegistry columbusCategoryCacheRegistry;
    @Mock
    ColumbusCategoryCache columbusCategoryCache;

    CategoryRequestValidatorService categoryRequestValidatorService;

    private final String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());

    @BeforeEach
    void setUp() {
        categoryRequestValidatorService = new CategoryRequestValidatorService(categoryService, categoryEntityCommands, categoryIndexManageCommands, columbusCategoryCacheRegistry);
        lenient().when(categoryIndexManageCommands.existsIndex(indexName))
            .thenReturn(true);
        lenient().when(columbusCategoryCacheRegistry.getColumbusCategoryCache(TestTenant.TEST_TENANT))
            .thenReturn(columbusCategoryCache);
        ColumbusCategoryCacheKey columbusCategoryCacheKey = ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT);
        lenient().when(columbusCategoryCache.get(columbusCategoryCacheKey))
            .thenReturn(this.getCategoryTree());
        lenient().when(categoryEntityCommands.getTree(TestTenant.TEST_TENANT, List.of(Platform.values())))
            .thenReturn(this.getCategoryTree());

    }


    @Test
    void testCreate() {
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .id("jk")
            .name("ab")
            .build();
        categoryRequestValidatorService.create(createCategoryRequest);
        verify(categoryService, times(1)).create(createCategoryRequest);
    }

    @Test
    void testGet() {
        SearchCategoryRequest searchCategoryRequest = SearchCategoryRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .documentIds(List.of("abcd"))
            .build();
        categoryRequestValidatorService.get(searchCategoryRequest);
        verify(categoryService, times(1)).get(searchCategoryRequest);
    }

    @Test
    void testSearch() {
        SearchCategoryRequest searchCategoryRequest = SearchCategoryRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .documentIds(List.of("abcd"))
            .build();
        categoryRequestValidatorService.search(searchCategoryRequest);
        verify(categoryService, times(1)).search(searchCategoryRequest);
    }

    @Test
    void testUpdate() {
        UpdateCategoryFieldsRequest updateCategoryFieldsRequest = UpdateCategoryFieldsRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .documentId("ab").
            fieldValueMap(new HashMap<>())
            .build();
        categoryRequestValidatorService.update(updateCategoryFieldsRequest);
        verify(categoryService, times(1)).update(updateCategoryFieldsRequest);
    }

    @Test
    void testDelete() {
        DeleteCategoryRequest deleteCategoryRequest = DeleteCategoryRequest.builder()
            .documentId("ab")
            .tenantType(TestTenant.TEST_TENANT)
            .build();
        categoryRequestValidatorService.delete(deleteCategoryRequest);
        verify(categoryService, times(1)).delete(deleteCategoryRequest);
    }

    @Test
    void testInitialise() {
        InitialiseServiceRequest initialiseServiceRequest = InitialiseServiceRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .build();
        lenient().when(categoryIndexManageCommands.existsIndex(indexName)).thenReturn(false);
        categoryRequestValidatorService.initialise(initialiseServiceRequest);
        verify(categoryService, times(1)).initialise(initialiseServiceRequest);

    }

    @Test
    void testUpdateMapping() {
        UpdateMappingServiceRequest updateMappingServiceRequest = UpdateMappingServiceRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .build();
        categoryRequestValidatorService.updateMapping(updateMappingServiceRequest);
        verify(categoryService, times(1)).updateMapping(updateMappingServiceRequest);
    }

    @Test
    void testUnLink() {
        UpdateCategoryLinkRequest updateCategoryLinkRequest = UpdateCategoryLinkRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .edge(Edge.builder()
                .source("ab")
                .destination("cd")
                .build())
            .build();
        categoryRequestValidatorService.unLink(updateCategoryLinkRequest);
        verify(categoryService, times(1)).unLink(updateCategoryLinkRequest);
    }

    @Test
    void testLink() {
        UpdateCategoryLinkRequest updateCategoryLinkRequest = UpdateCategoryLinkRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .edge(Edge.builder()
                .source("cd")
                .destination("ef")
                .build())
            .build();
        categoryRequestValidatorService.link(updateCategoryLinkRequest);
        verify(categoryService, times(1)).link(updateCategoryLinkRequest);

    }

    private CategoryTree getCategoryTree() {
        return CategoryTree.build(TestTenant.TEST_TENANT, List.of(
            Category.builder()
                .documentId("ab")
                .name("ab")
                .displayName("edgh")
                .root(true)
                .outgoingCategories(List.of("cd"))
                .build(),
            Category.builder()
                .documentId("cd").name("cd")
                .displayName("ijkl")
                .root(false)
                .outgoingCategories(List.of())
                .build(),
            Category.builder()
                .documentId("ef")
                .name("ef")
                .displayName("wxyz")
                .root(true)
                .outgoingCategories(List.of())
                .build()));
    }
}