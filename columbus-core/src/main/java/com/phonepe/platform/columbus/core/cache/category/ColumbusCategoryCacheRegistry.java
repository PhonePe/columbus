package com.phonepe.platform.columbus.core.cache.category;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.Maps;
import com.phonepe.platform.columbus.core.cache.CacheConfig;
import com.phonepe.platform.columbus.core.cache.ColumbusBaseCache;
import com.phonepe.platform.columbus.core.commands.category.CategoryEntityCommands;
import com.phonepe.platform.columbus.core.commands.category.CategoryIndexManageCommands;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import io.dropwizard.lifecycle.Managed;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;


@Slf4j
@Builder
@Singleton
public class ColumbusCategoryCacheRegistry implements Managed {
    @Builder.Default
    private final Map<ColumbusTenantType, ColumbusCategoryCache> cacheProcessor = Maps.newConcurrentMap();
    private final MetricRegistry metricRegistry;
    private final Map<ColumbusTenantType, CacheConfig> tenantTypeCacheConfigMap;
    private final List<ColumbusTenantType> tenantNames;
    private final CategoryEntityCommands categoryEntityCommands;
    private final CategoryIndexManageCommands categoryIndexManageCommands;

    public void addCache(
        final ColumbusTenantType columbusTenantType,
        final CacheConfig cacheConfig,
        final MetricRegistry metricRegistry,
        final CategoryEntityCommands categoryEntityCommands
    ) {
        addCache(columbusTenantType, new ColumbusCategoryCache(
            columbusTenantType,
            cacheConfig,
            metricRegistry,
            categoryEntityCommands
        ));
    }

    public void addCache(final ColumbusTenantType columbusTenantType) {
        addCache(columbusTenantType, new ColumbusCategoryCache(
            columbusTenantType,
            tenantTypeCacheConfigMap.getOrDefault(columbusTenantType, CacheConfig.builder().build()),
            metricRegistry,
            categoryEntityCommands
        ));
    }

    public void addCache(final ColumbusTenantType columbusTenantType, final ColumbusCategoryCache columbusCategoryCache) {
        cacheProcessor.putIfAbsent(columbusTenantType, columbusCategoryCache);
    }

    public ColumbusCategoryCache getColumbusCategoryCache(final ColumbusTenantType columbusTenantType) {
        if (!cacheProcessor.containsKey(columbusTenantType)) {
            throw ColumbusException.error(ColumbusCoreErrorCode.TENANT_CATEGORY_CACHE_NOT_FOUND,
                "Can't find a cache factory associated with the tenant id : %s".formatted(columbusTenantType));
        }

        return cacheProcessor.get(columbusTenantType);
    }

    @Override
    public void start() {
        log.info("Starting cache manager");

        tenantNames.forEach(tenantName -> {
            String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, tenantName.name());
            if (categoryIndexManageCommands.existsIndex(indexName)) {
                addCache(
                    tenantName,
                    tenantTypeCacheConfigMap.getOrDefault(tenantName, CacheConfig.builder().build()),
                    metricRegistry,
                    categoryEntityCommands
                );
            }
        });

        cacheProcessor.values()
            .forEach(ColumbusCategoryCache::initialize);

        log.info("Started Cache Manager successfully");
    }

    @Override
    public void stop() {
        log.info("Stopping the cache manager");
        cacheProcessor.values().forEach(ColumbusBaseCache::close);
    }
}
