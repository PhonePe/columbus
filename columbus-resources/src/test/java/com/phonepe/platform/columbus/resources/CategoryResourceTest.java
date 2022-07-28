package com.phonepe.platform.columbus.resources;

import com.phonepe.models.response.GenericResponse;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.services.CategoryService;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.dag.Edge;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.request.category.CreateCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.DeleteCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.InitialiseServiceRequest;
import com.phonepe.platform.columbus.models.core.request.category.SearchCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryFieldsRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryLinkRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryResourceTest {
    @Mock
    private CategoryService categoryService;
    @Captor
    private ArgumentCaptor<SearchCategoryRequest> searchRequestCaptor;

    private CategoryResource categoryResource;

    @BeforeEach
    void setUp() {
        categoryResource = new CategoryResource(categoryService, List.of(TestTenant.TEST_TENANT));
    }

    @Test
    void testInitialize() {
        var request = InitialiseServiceRequest.builder().
            tenantType(TestTenant.TEST_TENANT)
            .build();

        categoryResource.initialise(request, TestTenant.TEST_TENANT);

        verify(categoryService, times(1)).initialise(request);
    }

    @Test
    void testInitializeTenantMismatch() {
        var request = InitialiseServiceRequest.builder().
            tenantType(TestTenant.TEST_TENANT)
            .build();

        try {
            categoryResource.initialise(request, TestTenant.DUMMY_TENANT);
            fail();
        } catch (ColumbusException e) {
            assertEquals(ColumbusCoreErrorCode.TENANT_TYPE_MISMATCH, e.getErrorCode());
        }
        verifyNoInteractions(categoryService);
    }

    @Test
    void testInitializeInvalidTenant() {
        var request = InitialiseServiceRequest.builder().
            tenantType(TestTenant.DUMMY_TENANT)
            .build();

        try {
            categoryResource.initialise(request, TestTenant.DUMMY_TENANT);
            fail();
        } catch (ColumbusException e) {
            assertEquals(ColumbusCoreErrorCode.TENANT_TYPE_NOT_FOUND, e.getErrorCode());
        }
        verifyNoInteractions(categoryService);
    }

    @Test
    void testLink() {
        var request = UpdateCategoryLinkRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .edge(new Edge("source", "destination"))
            .build();

        categoryResource.link(request, TestTenant.TEST_TENANT);

        verify(categoryService, times(1)).link(request);
    }

    @Test
    void testUnlink() {
        var request = UpdateCategoryLinkRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .edge(new Edge("source", "destination"))
            .build();

        categoryResource.unlink(request, TestTenant.TEST_TENANT);

        verify(categoryService, times(1)).unLink(request);
    }

    @Test
    void testCreateEntity() {
        var request = CreateCategoryRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .build();

        categoryResource.createEntity(request, TestTenant.TEST_TENANT);

        verify(categoryService, times(1)).create(request);
    }

    @Test
    void testDeleteEntity() {
        var request = DeleteCategoryRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .build();

        categoryResource.deleteEntity(request, TestTenant.TEST_TENANT);

        verify(categoryService, times(1)).delete(request);
    }

    @Test
    void testUpdateEntity() {
        var request = UpdateCategoryFieldsRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .build();

        categoryResource.updateEntity(request, TestTenant.TEST_TENANT);

        verify(categoryService, times(1)).update(request);
    }

    @Test
    void testSearch() {
        var request = SearchCategoryRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .build();

        var category = new Category();

        when(categoryService.search(request)).thenReturn(List.of(category));
        GenericResponse<List<Category>> response = categoryResource.multiGet(request, TestTenant.TEST_TENANT);

        assertTrue(response.isSuccess());
        List<Category> categories = response.getData();
        assertEquals(1, categories.size());
        assertEquals(category, categories.get(0));
    }

    @Test
    void testSearchEmptyResponse() {
        var request = SearchCategoryRequest.builder()
            .tenantType(TestTenant.TEST_TENANT)
            .build();

        when(categoryService.search(request)).thenReturn(List.of());
        GenericResponse<List<Category>> response = categoryResource.multiGet(request, TestTenant.TEST_TENANT);

        assertFalse(response.isSuccess());
        assertTrue(response.getData().isEmpty());
    }

    @Test
    void testGetEntity() {
        var doc = "categoryId";
        var category = Category.builder()
            .documentId(doc)
            .build();

        when(categoryService.get(searchRequestCaptor.capture())).thenReturn(category);
        GenericResponse<Category> response = categoryResource.getEntity(doc, TestTenant.TEST_TENANT);

        assertTrue(response.isSuccess());
        assertEquals(category, response.getData());
        var request = searchRequestCaptor.getValue();
        assertInstanceOf(SearchCategoryRequest.class, request);
        assertEquals(List.of(doc), request.getDocumentIds());
        assertEquals(TestTenant.TEST_TENANT, request.getTenantType());
    }

    @Test
    void testGetEntityFail() {
        var doc = "categoryId";

        when(categoryService.get(searchRequestCaptor.capture())).thenReturn(null);
        GenericResponse<Category> response = categoryResource.getEntity(doc, TestTenant.TEST_TENANT);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        var request = searchRequestCaptor.getValue();
        assertInstanceOf(SearchCategoryRequest.class, request);
        assertEquals(List.of(doc), request.getDocumentIds());
        assertEquals(TestTenant.TEST_TENANT, request.getTenantType());
    }
}