package com.phonepe.platform.columbus.core;

import com.codahale.metrics.MetricRegistry;
import com.phonepe.platform.columbus.core.cache.CacheConfig;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCache;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCacheKey;
import com.phonepe.platform.columbus.core.commands.category.CategoryEntityCommands;
import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.core.models.forest.CategoryTree;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.category.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryCacheTest {

    @Mock
    private CategoryEntityCommands categoryEntityCommands;
    private ColumbusCategoryCache categoryCache;

    @BeforeEach
    void setup() {
        categoryCache = new ColumbusCategoryCache(
            TestTenant.TEST_TENANT,
            CacheConfig.builder().build(),
            new MetricRegistry(),
            categoryEntityCommands
        );

        var columbusTree = getCategoryTree();

        lenient().when(categoryEntityCommands.getTreeForCache(TestTenant.TEST_TENANT))
            .thenReturn(columbusTree);
    }

    @Test
    void testGet() {
        var response = categoryCache.get(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT));

        verify(categoryEntityCommands, timeout(5000).times(1))
            .getTreeForCache(TestTenant.TEST_TENANT);

        var categoryEntity = response.getCategory("entityId1");

        assertEquals("name12", categoryEntity.getName());

        var response2 = categoryCache.get(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT));

        verify(categoryEntityCommands, timeout(5000).times(1))
            .getTreeForCache(TestTenant.TEST_TENANT);

        var categoryEntity2 = response2.getCategory("entityId1");

        assertEquals(categoryEntity, categoryEntity2);
    }

    @Test
    void testRefresh() {
        categoryCache.refresh(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT));

        verify(categoryEntityCommands, timeout(5000).times(1))
            .getTreeForCache(TestTenant.TEST_TENANT);

        var response = categoryCache.get(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT));

        verify(categoryEntityCommands, timeout(5000).times(1))
            .getTreeForCache(TestTenant.TEST_TENANT);

        assertEquals("name12", response.getCategory("entityId1").getName());
    }

    @Test
    void testPut() {
        categoryCache.put(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT), getCategoryTree());

        var response = categoryCache.get(ColumbusCategoryCacheKey.forTenant(TestTenant.TEST_TENANT));

        verify(categoryEntityCommands, timeout(5000).times(0))
            .getTreeForCache(TestTenant.TEST_TENANT);

        assertEquals("name12", response.getCategory("entityId1").getName());
    }

    private CategoryTree getCategoryTree() {
        return CategoryTree.build(TestTenant.TEST_TENANT, List.of(
            Category.builder()
                .documentId("entityId1")
                .name("name12")
                .displayName("displayName")
                .root(true)
                .outgoingCategories(List.of())
                .build()));
    }
}
