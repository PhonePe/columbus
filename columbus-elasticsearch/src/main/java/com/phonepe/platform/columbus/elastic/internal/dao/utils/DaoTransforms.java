package com.phonepe.platform.columbus.elastic.internal.dao.utils;

import com.phonepe.platform.columbus.dao.index.IndexProperties;
import com.phonepe.platform.columbus.elastic.common.Constants.Properties;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNonNegative;
import static com.phonepe.platform.columbus.common.Predicates.applyIfPositive;

/**
 * A collection of helper methods to transform entity objects between Columbus and ElasticSearch.
 */
@UtilityClass
public class DaoTransforms {

    public static Map<String, Object> getSettings(final IndexProperties properties) {
        final var settings = new HashMap<>(Objects.requireNonNullElseGet(properties.getSettings(), Map::of));
        applyIfPositive(properties.getNumberOfShards(), value -> settings.put(Properties.SHARD_COUNT, value));
        applyIfNonNegative(properties.getNumberOfReplicas(), value -> settings.put(Properties.REPLICA_COUNT, value));
        return settings;
    }
}
