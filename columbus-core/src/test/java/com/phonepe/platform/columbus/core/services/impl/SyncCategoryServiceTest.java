package com.phonepe.platform.columbus.core.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCache;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCacheKey;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCacheRegistry;
import com.phonepe.platform.columbus.core.commands.category.CategoryEntityCommands;
import com.phonepe.platform.columbus.core.commands.category.CategoryIndexManageCommands;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.core.models.forest.CategoryTree;
import com.phonepe.platform.columbus.core.models.operations.*;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.category.StoredCategory;
import com.phonepe.platform.columbus.models.core.dag.Edge;
import com.phonepe.platform.columbus.models.core.request.category.*;
import org.checkerframework.checker.units.qual.C;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncCategoryServiceTest {
    @Mock CategoryEntityCommands categoryEntityCommands;
    @Mock  CategoryIndexManageCommands categoryIndexManageCommands;
    @Mock ColumbusCategoryCacheRegistry columbusCategoryCacheRegistry;
    @Mock ColumbusCategoryCache columbusCategoryCache;
    @Mock ObjectMapper mapper;
    @Captor ArgumentCaptor<EntitySaveOperation<StoredCategory>> categorySaveCaptor;
    @Captor ArgumentCaptor<EntityFieldsUpdateOperation>categoryUpdateCaptor;
    @Captor ArgumentCaptor<EntityDeleteOperation>categoryDeleteCaptor;
    @Captor ArgumentCaptor<CreateTemplateOperation>createTemplateOperationCaptor;
    @Captor ArgumentCaptor<CreateIndexOperation>createIndexOperationCaptor;

    SyncCategoryService syncCategoryService;

    @BeforeEach
    void setUp(){
        syncCategoryService = new SyncCategoryService(categoryEntityCommands,categoryIndexManageCommands,columbusCategoryCacheRegistry);
        MapperUtils.initialise(mapper);
        lenient().when(columbusCategoryCacheRegistry.getColumbusCategoryCache(TestTenant.TEST_TENANT)).thenReturn(columbusCategoryCache);
        lenient().when(columbusCategoryCache.get(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT))).thenReturn(this.getCategoryTree());
        lenient().when(categoryEntityCommands.getById(TestTenant.TEST_TENANT,"ab")).
                thenReturn(Optional.of(Category.builder().
                        documentId("abcd").root(true)
                        .outgoingCategories(new ArrayList<>(List.of("ef"))).build()));
    }

    @Test
    void testCreate() {
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .tenantType(TestTenant.TEST_TENANT).id("abcd").build();
        syncCategoryService.create(createCategoryRequest);
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,TestTenant.TEST_TENANT.name());
        verify(categoryEntityCommands,times(1)).entitySave(categorySaveCaptor.capture());
        EntitySaveOperation<StoredCategory> operation = categorySaveCaptor.getValue();
        assertEquals("abcd",operation.getDocumentId());
        assertEquals(indexName,operation.getIndexName());
        verify(columbusCategoryCacheRegistry,times(1)).getColumbusCategoryCache(TestTenant.TEST_TENANT);
        verify(columbusCategoryCache,times(1)).refresh(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT));

    }

    @Test
    void testGet() {
        SearchCategoryRequest searchCategoryRequest = SearchCategoryRequest.builder()
                .documentIds(List.of("abcd")).tenantType(TestTenant.TEST_TENANT)
                .build();
        Category getRes = syncCategoryService.get(searchCategoryRequest);
        assertEquals("abcd",getRes.getDocumentId());
        assertEquals("wxyz",getRes.getName());
    }

    @Test
    void testSearch() {
        SearchCategoryRequest searchCategoryRequest = SearchCategoryRequest.builder()
                .documentIds(List.of("abcd")).tenantType(TestTenant.TEST_TENANT)
                .build();
        List<Category> searchRes = syncCategoryService.search(searchCategoryRequest);
        assertEquals("abcd",searchRes.get(0).getDocumentId());
    }

    @Test
    void testUpdate() {
        UpdateCategoryFieldsRequest updateCategoryFieldsRequest = UpdateCategoryFieldsRequest.builder().
                tenantType(TestTenant.TEST_TENANT).fieldValueMap(new HashMap<>()).documentId("abcd").build();
        syncCategoryService.update(updateCategoryFieldsRequest);
        verify(categoryEntityCommands,times(1)).entityFieldsUpdate(categoryUpdateCaptor.capture());
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,TestTenant.TEST_TENANT.name());
        EntityFieldsUpdateOperation entityFieldsUpdateOperation = categoryUpdateCaptor.getValue();
        assertEquals(indexName,entityFieldsUpdateOperation.getIndexName());
        assertEquals("abcd",entityFieldsUpdateOperation.getDocumentId());
        verify(columbusCategoryCacheRegistry,times(1)).getColumbusCategoryCache(TestTenant.TEST_TENANT);
        verify(columbusCategoryCache,times(1)).refresh(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT));

    }

    @Test
    void testDelete() {
        DeleteCategoryRequest deleteCategoryRequest = DeleteCategoryRequest.builder().
                tenantType(TestTenant.TEST_TENANT).documentId("abcd").build();
        syncCategoryService.delete(deleteCategoryRequest);
        verify(categoryEntityCommands,times(1)).entityDelete(categoryDeleteCaptor.capture());
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,TestTenant.TEST_TENANT.name());
        EntityDeleteOperation entityDeleteOperation = categoryDeleteCaptor.getValue();
        assertEquals("abcd",entityDeleteOperation.getDocumentId());
        assertEquals(indexName,entityDeleteOperation.getIndexName());
        verify(columbusCategoryCacheRegistry,times(1)).getColumbusCategoryCache(TestTenant.TEST_TENANT);
        verify(columbusCategoryCache,times(1)).delete(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT));

    }

    @Test
    void testInitialise() {
        InitialiseServiceRequest initialiseServiceRequest = InitialiseServiceRequest.builder().
                tenantType(TestTenant.TEST_TENANT).noOfReplicas(1).
                noOfRoutingShards(1).noOfShards(1).build();
        syncCategoryService.initialise(initialiseServiceRequest);
        verify(categoryIndexManageCommands,times(1)).createIndexTemplate(createTemplateOperationCaptor.capture());
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,TestTenant.TEST_TENANT.name());
        CreateTemplateOperation createTemplateOperation = createTemplateOperationCaptor.getValue();
        assertEquals(indexName,createTemplateOperation.getIndexName());
        assertEquals(1,createTemplateOperation.getNoOfShards());
        assertEquals(1,createTemplateOperation.getNoOfReplicas());
        assertEquals(1,createTemplateOperation.getNoOfRoutingShards());
        verify(categoryIndexManageCommands,times(1)).createIndex(createIndexOperationCaptor.capture());
        CreateIndexOperation createIndexOperation = createIndexOperationCaptor.getValue();
        assertEquals(indexName,createIndexOperation.getIndexName());
        assertEquals(EntityType.CATEGORY,createIndexOperation.getEntityType());
        verify(columbusCategoryCacheRegistry,times(1)).addCache(TestTenant.TEST_TENANT);
    }

    @Test
    void testUpdateMapping() {
        UpdateMappingServiceRequest updateMappingServiceRequest = UpdateMappingServiceRequest.builder().
                tenantType(TestTenant.TEST_TENANT).build();
        Throwable exception = Assert.assertThrows(ColumbusException.class,()->syncCategoryService.updateMapping(updateMappingServiceRequest));
        assertEquals("Operation not supported",exception.getMessage());
    }

    @Test
    void testUnLink() {
        UpdateCategoryLinkRequest updateCategoryLinkRequest = UpdateCategoryLinkRequest.builder()
                .edge(Edge.builder().source("ab").destination("ef")
                        .build()).tenantType(TestTenant.TEST_TENANT).build();
        syncCategoryService.unLink(updateCategoryLinkRequest);
        verify(categoryEntityCommands,times(1)).getById(TestTenant.TEST_TENANT,"ab");
        verify(categoryEntityCommands,times(1)).entityFieldsUpdate(categoryUpdateCaptor.capture());
        EntityFieldsUpdateOperation entityFieldsUpdateOperation = categoryUpdateCaptor.getValue();
        Map<String,Object> map = entityFieldsUpdateOperation.getEntityFieldValueData();
        //noinspection unchecked
        List<String> outgoingEdges = (List<String>) map.get("outgoingCategories");
        assertEquals(0,outgoingEdges.size());
        verify(columbusCategoryCacheRegistry,times(1)).getColumbusCategoryCache(TestTenant.TEST_TENANT);
        verify(columbusCategoryCache,times(1)).refresh(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT));
    }

    @Test
    void testLink() {
        UpdateCategoryLinkRequest updateCategoryLinkRequest = UpdateCategoryLinkRequest.builder()
                .edge(Edge.builder().source("ab").destination("cd")
                        .build()).tenantType(TestTenant.TEST_TENANT).build();
        syncCategoryService.link(updateCategoryLinkRequest);
        verify(categoryEntityCommands,times(1)).getById(TestTenant.TEST_TENANT,"ab");
        verify(categoryEntityCommands,times(1)).entityFieldsUpdate(categoryUpdateCaptor.capture());
        EntityFieldsUpdateOperation entityFieldsUpdateOperation = categoryUpdateCaptor.getValue();
        Map<String,Object> map = entityFieldsUpdateOperation.getEntityFieldValueData();
        //noinspection unchecked
        List<String> outgoingEdges = (List<String>) map.get("outgoingCategories");
        assertEquals(2,outgoingEdges.size());
        assertTrue(outgoingEdges.contains("cd"));
        assertTrue(outgoingEdges.contains("ef"));
        verify(columbusCategoryCacheRegistry,times(1)).getColumbusCategoryCache(TestTenant.TEST_TENANT);
        verify(columbusCategoryCache,times(1)).refresh(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT));
    }


    private CategoryTree getCategoryTree() {
        return CategoryTree.build(TestTenant.TEST_TENANT, List.of(
                Category.builder()
                        .documentId("abcd")
                        .name("wxyz")
                        .displayName("edgh")
                        .root(true)
                        .outgoingCategories(List.of())
                        .build()));
    }
}