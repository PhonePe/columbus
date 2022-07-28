package com.phonepe.platform.columbus.core.utils;

import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.core.models.forest.CategoryTree;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.dag.Edge;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusError;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CategoryValidationUtilsFailureTest {
    private final CategoryTree tree = CategoryTree.build(
        TestTenant.TEST_TENANT,
        List.of(
            Category.builder()
                .documentId("category1")
                .outgoingCategories(List.of("category2"))
                .build(),
            Category.builder()
                .documentId("category2")
                .outgoingCategories(List.of())
                .build()
        )
    );

    @Test
    void testCategoryNotPresent() {
        expectException(ColumbusCoreErrorCode.CATEGORY_ALREADY_EXISTS, () ->
            CategoryValidationUtils.validateIfCategoryNotPresent(tree, "category1"));
    }

    @Test
    void testCategoriesExist() {
        expectException(ColumbusCoreErrorCode.INVALID_CATEGORY_ID, () ->
            CategoryValidationUtils.validateIfCategoriesExist(tree, Set.of("category1", "missingCategory")));
    }

    @Test
    void testCycleCreation() {
        expectException(ColumbusCoreErrorCode.CYCLIC_DEPENDENCY, () ->
            CategoryValidationUtils.validateCycleCreation(new Edge("category2", "category1"), tree));
    }

    @Test
    void testParentExists() {
        expectException(ColumbusCoreErrorCode.CATEGORY_VALIDATION_ERROR, () ->
            CategoryValidationUtils.validateIfParentExists(tree, "category2"));
    }

    @Test
    void testEdgeExists() {
        expectException(ColumbusCoreErrorCode.EDGE_ALREADY_EXIST, () ->
            CategoryValidationUtils.validateIfEdgeAlreadyExists(new Edge("category1", "category2"), tree));
    }

    @Test
    void testValidTenantType() {
        try {
            CategoryValidationUtils.tenantValidation(TestTenant.TEST_TENANT, TestTenant.TEST_TENANT, List.of(TestTenant.TEST_TENANT));
        } catch (ColumbusException e) {
            fail(e);
        }
    }

    @Test
    void testInvalidTenantType() {
        expectException(ColumbusCoreErrorCode.TENANT_TYPE_NOT_FOUND, () ->
            CategoryValidationUtils.validateTenantType(TestTenant.TEST_TENANT, List.of()));
    }

    @Test
    void testTenantTypesMismatch() {
        expectException(ColumbusCoreErrorCode.TENANT_TYPE_MISMATCH, () ->
            CategoryValidationUtils.validateTenantTypesMatch(TestTenant.TEST_TENANT, TestTenant.DUMMY_TENANT));
    }

    @Test
    void testIndexExists() {
        expectException(ColumbusCoreErrorCode.INDEX_DOES_NOT_EXIST, () ->
            CategoryValidationUtils.validateIndexExists(false));
    }

    @Test
    void testIndexDoesNotExist() {
        expectException(ColumbusCoreErrorCode.TENANT_ALREADY_PRESENT, () ->
            CategoryValidationUtils.validateIndexDoesNotExist(true));
    }

    void expectException(ColumbusError errorCode, Callable function) {
        try {
            function.call();
        } catch (ColumbusException e) {
            assertEquals(errorCode, e.getErrorCode());
            return;
        }
        fail("Expected exception '%s'".formatted(errorCode.name()));
    }

    private interface Callable {
        void call() throws ColumbusException;
    }
}
