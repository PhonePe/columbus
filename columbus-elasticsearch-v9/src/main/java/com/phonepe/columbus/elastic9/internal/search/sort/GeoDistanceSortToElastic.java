package com.phonepe.columbus.elastic9.internal.search.sort;

import co.elastic.clients.elasticsearch._types.GeoDistanceSort.Builder;
import co.elastic.clients.elasticsearch._types.SortOptions;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.elastic9.search.sort.SortToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.sort.GeoDistanceSort;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = GeoDistanceSort.TYPE, type = GeoDistanceSort.class)
public class GeoDistanceSortToElastic<C extends ElasticSortTransformationContext<C>>
        implements SortToElastic<GeoDistanceSort, C> {

    @Override
    public SortOptions visit(final GeoDistanceSort sort, final Object props, final C context) {
        final var points = sort.getPoints().stream()
                .map(MiscTransforms::toElasticGeoPoint)
                .toList();

        final var builder = new Builder().field(sort.getField()).location(points);
        applyIfNotNull(sort.getDistanceType(), SearchEnums::toGeoDistance, builder::distanceType);
        applyIfNotNull(sort.getDistanceUnit(), SearchEnums::toDistanceUnit, builder::unit);
        applyIfNotNull(sort.getSortMode(), SearchEnums::toSortMode, builder::mode);
        applyIfNotNull(sort.getIgnoreMissingField(), builder::ignoreUnmapped);
        applyIfNotNull(sort.getOrder(), SearchEnums::toSortOrder, builder::order);
        return new SortOptions.Builder().geoDistance(builder.build()).build();
    }
}
