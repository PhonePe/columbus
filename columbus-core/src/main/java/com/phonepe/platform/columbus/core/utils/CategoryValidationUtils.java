package com.phonepe.platform.columbus.core.utils;

import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.models.forest.CategoryTree;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import com.phonepe.platform.columbus.models.core.dag.Edge;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

@Slf4j
@UtilityClass
public class CategoryValidationUtils {
    public void validateIfCategoryNotPresent(CategoryTree categoryTree, String categoryId) {
        if (Objects.nonNull(categoryTree.getCategory(categoryId))) {
            throw ColumbusException.error(ColumbusCoreErrorCode.CATEGORY_ALREADY_EXISTS,
                "Category already exists: '%s'".formatted(categoryId));
        }
    }

    @SuppressWarnings("java:S3958") // SonarQube fix
    public void validateIfCategoriesExist(CategoryTree categoryTree, Set<String> categoryList) {
        var persistingCategories = categoryTree.getNodes().keySet();

        List<String> invalidCategories = categoryList.stream()
            .filter(Predicate.not(persistingCategories::contains))
            .toList();

        if (!invalidCategories.isEmpty()) {
            throw ColumbusException.error(ColumbusCoreErrorCode.INVALID_CATEGORY_ID,
                "Categories Not Found: %s".formatted(invalidCategories));
        }
    }

    public void validateCycleCreation(Edge edge, CategoryTree categoryTree) {
        if (categoryTree.createsCycle(edge)) {
            throw ColumbusException.error(ColumbusCoreErrorCode.CYCLIC_DEPENDENCY,
                "Edge %s creates cycle".formatted(edge));
        }
    }

    public void validateIfParentExists(CategoryTree categoryTree, String destinationCategory) {
        if (!categoryTree.isRoot(destinationCategory)
            && categoryTree.getParentNode(destinationCategory) != null) {
            throw ColumbusException.error(ColumbusCoreErrorCode.CATEGORY_VALIDATION_ERROR,
                "Parent category already exists for '%s'".formatted(destinationCategory));
        }
    }

    public void validateIfEdgeAlreadyExists(Edge edge, CategoryTree categoryTree) {
        if (categoryTree.containsEdge(edge)) {
            throw ColumbusException.error(ColumbusCoreErrorCode.EDGE_ALREADY_EXIST,
                "Edge %s already present".formatted(edge));
        }
    }

    public void validateTenantType(final ColumbusTenantType tenantType, final List<ColumbusTenantType> validTenantTypes) {
        if (!validTenantTypes.contains(tenantType)) {
            throw ColumbusException.error(ColumbusCoreErrorCode.TENANT_TYPE_NOT_FOUND,
                "Tenant type '%s' not found".formatted(tenantType));
        }
    }

    public void validateTenantTypesMatch(final ColumbusTenantType tenant1, final ColumbusTenantType tenant2) {
        if (tenant1 == null || tenant2 == null || !tenant1.name().equalsIgnoreCase(tenant2.name())) {
            throw ColumbusException.error(ColumbusCoreErrorCode.TENANT_TYPE_MISMATCH,
                "Tenant type mismatch: %s != %s".formatted(tenant1, tenant2));
        }
    }

    public void tenantValidation(
        final ColumbusTenantType tenant1,
        final ColumbusTenantType tenant2,
        final List<ColumbusTenantType> validTenantTypes
    ) {
        validateTenantTypesMatch(tenant1, tenant2);
        validateTenantType(tenant1, validTenantTypes);
    }

    public void validateIndexExists(boolean indexExists) {
        if (!indexExists) {
            throw ColumbusException.error(ColumbusCoreErrorCode.INDEX_DOES_NOT_EXIST, "Index does not exist");
        }
    }

    public void validateIndexDoesNotExist(boolean indexExists) {
        if (indexExists) {
            throw ColumbusException.error(ColumbusCoreErrorCode.TENANT_ALREADY_PRESENT, "Index already exists");
        }
    }
}

