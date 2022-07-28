package com.phonepe.platform.columbus.core.cache;

import java.io.Closeable;

public interface ColumbusCache<K, V> extends Closeable {
    void refresh(K cacheKey);

    V get(final K key);

    void put(final K key, final V value);

    void delete(final K lookupKey);
}
