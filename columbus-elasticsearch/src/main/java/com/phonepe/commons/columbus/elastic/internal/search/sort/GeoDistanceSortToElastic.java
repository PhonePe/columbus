package com.phonepe.commons.columbus.elastic.internal.search.sort;

import com.phonepe.commons.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic.search.sort.ElasticSortTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.sort.GeoDistanceSort;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = GeoDistanceSort.TYPE, type = GeoDistanceSort.class)
public class GeoDistanceSortToElastic<C extends ElasticSortTransformationContext<C>>
        extends BaseSortToElastic<GeoDistanceSort, C> {

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
