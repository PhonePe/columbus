package com.phonepe.columbus.elastic7.internal.search.sort;

import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic7.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.sort.GeoDistanceSort;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = GeoDistanceSort.TYPE, type = GeoDistanceSort.class)
public class GeoDistanceSortToElastic<C extends ElasticSortTransformationContext<C>>
        extends AbstractSortToElastic<GeoDistanceSort, C> {

    @Override
    protected SortBuilder<?> buildSort(final GeoDistanceSort sort, final Object props, final C context) {
        final var points = sort.getPoints().stream()
                .map(MiscTransforms::toElasticGeoPoint)
                .toArray(GeoPoint[]::new);

        final var builder = SortBuilders.geoDistanceSort(sort.getField(), points);
        applyIfNotNull(sort.getDistanceType(), SearchEnums::toGeoDistance, builder::geoDistance);
        applyIfNotNull(sort.getDistanceUnit(), SearchEnums::toDistanceUnit, builder::unit);
        applyIfNotNull(sort.getSortMode(), SearchEnums::toSortMode, builder::sortMode);
        applyIfNotNull(sort.getIgnoreMissingField(), builder::ignoreUnmapped);
        return builder;
    }
}
