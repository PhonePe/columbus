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
package com.phonepe.columbus.elastic9.internal.search.query.geo;

import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.geo.GeoDistanceQuery;

import java.util.Objects;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = GeoDistanceQuery.TYPE, type = GeoDistanceQuery.class)
public class GeoDistanceQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<GeoDistanceQuery, C> {

    @Override
    public QueryVariant visit(final GeoDistanceQuery query, final Object props, final C context) {
        final var center = Objects.requireNonNull(query.getCenter(), "center must not be null");

        final var builder = new Builder()
                .field(query.getField())
                .location(MiscTransforms.toElasticGeoPoint(center));
        applyCommonProperties(query, builder);
        applyIfNotNull(query.getDistance(), builder::distance);
        applyIfNotNull(query.getDistanceType(), SearchEnums::toGeoDistance, builder::distanceType);
        applyIfNotNull(query.getIgnoreMissingField(), builder::ignoreUnmapped);

        return builder.build();
    }
}
