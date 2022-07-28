package com.phonepe.platform.columbus.core.cache;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.caffeine.MetricsStatsCounter;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public abstract class ColumbusBaseCache<K, V> implements ColumbusCache<K, V> {
    private final String cacheName;
    private final AsyncLoadingCache<K, V> asyncLoadingCache;

    protected ColumbusBaseCache(
        final String cacheName,
        final CacheConfig cacheConfig,
        final MetricRegistry metricRegistry,
        final String cacheMetricsName,
        final CacheLoader<K, V> cacheLoader
    ) {
        this.cacheName = cacheName;
        asyncLoadingCache = Caffeine.newBuilder()
            .recordStats(() -> new MetricsStatsCounter(metricRegistry, cacheMetricsName))
            .maximumSize(cacheConfig.getMaxElements())
            .refreshAfterWrite(cacheConfig.getRefreshInSeconds(), TimeUnit.SECONDS)
            .expireAfterWrite(cacheConfig.getExpiryInSeconds(), TimeUnit.SECONDS)
            .buildAsync(cacheLoader);
        log.info("Created cache config: {}", cacheName);
    }

    public void initialize() {
        log.info("{} Cache implementation is now set", cacheName);
    }

    @Override
    public V get(final K key) {
        return asyncLoadingCache.synchronous().get(key);
    }

    @Override
    public void put(final K key, final V value) {
        asyncLoadingCache.synchronous().put(key, value);
    }

    @Override
    public void delete(final K lookupKey) {
        asyncLoadingCache.synchronous().refresh(lookupKey);
    }

    @Override
    public void refresh(final K lookupKey) {
        asyncLoadingCache.synchronous().refresh(lookupKey);
    }

    @Override
    public void close() {
        log.info("{} Cache implementation is closing", cacheName);
    }
}