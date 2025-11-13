package com.phonepe.commons.columbus.elastic9.internal.search.query.geo;

import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.geo.GeoDistanceQuery;

import java.util.Objects;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

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
