package com.phonepe.columbus.elastic7.internal.search.query.geo;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.geo.GeoPointQuery;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.Objects;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = GeoPointQuery.TYPE, type = GeoPointQuery.class)
public class GeoPointQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<GeoPointQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final GeoPointQuery query, final Object props, final C context) {
        final var point = Objects.requireNonNull(query.getPoint(), "point must not be null");
        final var builder = new GeoShapeQueryBuilder(query.getField(), MiscTransforms.toElasticGeometry(point));

        applyIfNotNull(query.getIgnoreMissingField(), builder::ignoreUnmapped);

        return builder;
    }
}
