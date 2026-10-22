/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.elastic9.internal.dao.utils;

import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.json.JsonData;
import com.phonepe.columbus.dao.index.IndexProperties;
import com.phonepe.columbus.serde.SerDe;
import lombok.experimental.UtilityClass;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static com.phonepe.columbus.common.Predicates.applyIfNonNegative;
import static com.phonepe.columbus.common.Predicates.applyIfPositive;

/**
 * A collection of helper methods to transform entity objects between Columbus and ElasticSearch.
 */
@UtilityClass
public class DaoTransforms {

    public static IndexSettings getSettings(final IndexProperties properties) {
        final var builder = new IndexSettings.Builder();

        applyIfPositive(properties.getNumberOfShards(),
                value -> builder.numberOfShards(String.valueOf(value)));
        applyIfNonNegative(properties.getNumberOfReplicas(),
                value -> builder.numberOfReplicas(String.valueOf(value)));

        if (properties.getSettings() == null) {
            return builder.build();
        }

        final Map<String, JsonData> otherSettings = new HashMap<>();

        for (final var entry : properties.getSettings().entrySet()) {
            otherSettings.put(entry.getKey(), JsonData.of(entry.getValue()));
        }

        return builder.otherSettings(otherSettings).build();
    }

    public static Map<String, Property> getProperties(final Map<String, Object> schema, final SerDe serde) {
        final Map<String, Property> properties = new HashMap<>();

        if (schema == null || schema.get("properties") == null) {
            return properties;
        }

        @SuppressWarnings("unchecked")
        final var schemaProperties = (Map<String, Object>) schema.get("properties");
        for (final var entry : schemaProperties.entrySet()) {
            properties.put(entry.getKey(),
                    new Property.Builder().withJson(new StringReader(serde.toString(entry.getValue()))).build());
        }

        return properties;
    }

    public static TypeMapping getTypeMappings(final Map<String, Object> schema, final SerDe serde) {
        final var builder = new TypeMapping.Builder();
        return builder.properties(getProperties(schema, serde)).build();
    }
}
