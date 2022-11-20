package com.phonepe.platform.columbus.core.cache.category;

import com.codahale.metrics.MetricRegistry;
import com.phonepe.platform.columbus.core.cache.CacheConfig;
import com.phonepe.platform.columbus.core.cache.ColumbusBaseCache;
import com.phonepe.platform.columbus.core.commands.category.CategoryEntityCommands;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.forest.CategoryTree;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import com.phonepe.platform.columbus.models.core.category.Platform;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ColumbusCategoryCache extends ColumbusBaseCache<ColumbusCategoryCacheKey, CategoryTree> {
    public ColumbusCategoryCache(
        final ColumbusTenantType columbusTenantType,
        final CacheConfig cacheConfig,
        final MetricRegistry metricRegistry,
        final CategoryEntityCommands categoryEntityCommands
    ) {
        super(
            columbusTenantType.name(),
            cacheConfig,
            metricRegistry,
            IndexUtils.getCacheName(EntityType.CATEGORY, columbusTenantType.name()),
            key -> categoryEntityCommands.getTreeForCache(columbusTenantType)
        );
    }
}
