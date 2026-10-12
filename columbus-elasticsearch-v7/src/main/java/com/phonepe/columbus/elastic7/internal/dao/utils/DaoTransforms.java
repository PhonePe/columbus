package com.phonepe.columbus.elastic7.internal.dao.utils;

import com.phonepe.columbus.dao.index.IndexProperties;
import com.phonepe.columbus.elastic7.common.Constants.Properties;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.phonepe.columbus.common.Predicates.applyIfNonNegative;
import static com.phonepe.columbus.common.Predicates.applyIfPositive;

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
