package com.phonepe.platform.columbus.elastic.internal.search.query.geo;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.platform.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.geo.GeoDistanceQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Objects;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = GeoDistanceQuery.TYPE, type = GeoDistanceQuery.class)
public class GeoDistanceQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<GeoDistanceQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final GeoDistanceQuery query, final Object props, final C context) {
        final var center = Objects.requireNonNull(query.getCenter(), "center must not be null");
        final var builder = QueryBuilders.geoDistanceQuery(query.getField())
                .point(MiscTransforms.toElasticGeoPoint(center));

        applyIfNotNull(query.getDistance(), builder::distance);
        applyIfNotNull(query.getDistanceType(), SearchEnums::toGeoDistance, builder::geoDistance);
        applyIfNotNull(query.getIgnoreMissingField(), builder::ignoreUnmapped);

        return builder;
    }
}
