package com.phonepe.platform.columbus.core.services.impl;

import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.core.services.CategoryService;
import com.phonepe.platform.columbus.core.worker.producer.ColumbusCategoryProducerRegistry;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.category.EntityStatus;
import com.phonepe.platform.columbus.models.core.dag.Edge;
import com.phonepe.platform.columbus.models.core.request.category.*;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncCategoryServiceTest {

    @Mock CategoryService syncCategoryService;
    @Mock ColumbusCategoryProducerRegistry producer;

    AsyncCategoryService asyncCategoryService;

    @BeforeEach
    void setUp() {
        asyncCategoryService = new AsyncCategoryService(syncCategoryService,producer);
        List<String>ids = new ArrayList<>();
        ids.add("abcdf");
        lenient().when(syncCategoryService.get(SearchCategoryRequest.builder().tenantType(TestTenant.TEST_TENANT).documentIds(ids).build())).thenReturn(Category.builder().documentId("abcdf").build());
        lenient().when(syncCategoryService.search(SearchCategoryRequest.builder().tenantType(TestTenant.TEST_TENANT).documentIds(ids).build())).thenReturn(List.of(Category.builder().documentId("abcdf").build()));
    }


    @Test
    void testCreate() {
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder().
                tenantType(TestTenant.TEST_TENANT).
                name("abcv").id("abdsabdf").
                createdBy("test").status(EntityStatus.ACTIVE).
                displayName("randomObject").build();
        asyncCategoryService.create(createCategoryRequest);
        verify(producer,times(1)).publish(createCategoryRequest);
    }

    @Test
    void testGet() {
        List<String>ids = new ArrayList<>();
        ids.add("abcdf");
        SearchCategoryRequest searchCategoryRequest = SearchCategoryRequest.builder().
                tenantType(TestTenant.TEST_TENANT).
                documentIds(ids).build();
        Category category = asyncCategoryService.get(searchCategoryRequest);
        assertEquals("abcdf",category.getDocumentId());
    }

    @Test
    void testSearch() {
        List<String>ids = new ArrayList<>();
        ids.add("abcdf");
        SearchCategoryRequest searchCategoryRequest = SearchCategoryRequest.builder().
                tenantType(TestTenant.TEST_TENANT).
                documentIds(ids).build();
        List<Category>res = asyncCategoryService.search(searchCategoryRequest);
        assertEquals("abcdf",res.get(0).getDocumentId());

    }

    @Test
    void testUpdate() {
        UpdateCategoryFieldsRequest updateCategoryFieldsRequest = UpdateCategoryFieldsRequest.
                builder().tenantType(TestTenant.TEST_TENANT).documentId("abcdf").build();
        asyncCategoryService.update(updateCategoryFieldsRequest);
        verify(producer,times(1)).publish(updateCategoryFieldsRequest);

    }

    @Test
    void testDelete() {
        DeleteCategoryRequest deleteCategoryRequest = DeleteCategoryRequest.builder().
                tenantType(TestTenant.TEST_TENANT).documentId("abcdf").build();
        asyncCategoryService.delete(deleteCategoryRequest);
        verify(producer,times(1)).publish(deleteCategoryRequest);
    }

    @Test
    void testInitialise() {
        InitialiseServiceRequest initialiseServiceRequest = InitialiseServiceRequest.builder().
                tenantType(TestTenant.TEST_TENANT).noOfShards(1).noOfReplicas(1)
                .noOfRoutingShards(1).build();
        asyncCategoryService.initialise(initialiseServiceRequest);
        verify(producer,times(1)).publish(initialiseServiceRequest);
    }

    @Test
    void testUpdateMapping() {
        UpdateMappingServiceRequest updateMappingServiceRequest = UpdateMappingServiceRequest
                .builder().tenantType(TestTenant.TEST_TENANT).build();
        asyncCategoryService.updateMapping(updateMappingServiceRequest);
        verify(syncCategoryService,times(1)).updateMapping(updateMappingServiceRequest);
    }

    @Test
    void testUnLink() {
        UpdateCategoryLinkRequest updateCategoryLinkRequest = UpdateCategoryLinkRequest.builder()
                .tenantType(TestTenant.TEST_TENANT).edge(Edge.builder().source("abcd").destination("wxyz").build()).build();
        asyncCategoryService.unLink(updateCategoryLinkRequest);
        verify(syncCategoryService,times(1)).unLink(updateCategoryLinkRequest);

    }

    @Test
    void testLink() {
        UpdateCategoryLinkRequest updateCategoryLinkRequest = UpdateCategoryLinkRequest.builder()
                .tenantType(TestTenant.TEST_TENANT).edge(Edge.builder().source("abcd").destination("wxzy").build()).build();
        asyncCategoryService.link(updateCategoryLinkRequest);
        verify(syncCategoryService,times(1)).link(updateCategoryLinkRequest);
    }
}