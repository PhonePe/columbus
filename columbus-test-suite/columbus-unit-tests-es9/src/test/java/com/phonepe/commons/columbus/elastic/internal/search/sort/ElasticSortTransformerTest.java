package com.phonepe.commons.columbus.elastic.internal.search.sort;

import co.elastic.clients.elasticsearch._types.DistanceUnit;
import co.elastic.clients.elasticsearch._types.FieldSort.Builder;
import co.elastic.clients.elasticsearch._types.FieldSortNumericType;
import co.elastic.clients.elasticsearch._types.GeoDistanceType;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.ScriptSortType;
import co.elastic.clients.elasticsearch._types.SortMode;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.search.misc.geo.Location;
import com.phonepe.commons.columbus.search.misc.script.Script;
import com.phonepe.commons.columbus.search.sort.FieldSort;
import com.phonepe.commons.columbus.search.sort.GeoDistanceSort;
import com.phonepe.commons.columbus.search.sort.ScoreSort;
import com.phonepe.commons.columbus.search.sort.ScriptSort;
import com.phonepe.commons.columbus.search.sort.SortOrder;
import com.phonepe.commons.columbus.search.sort.SortTransformer;
import com.phonepe.commons.columbus.es9.test.annotations.WithElasticToolkit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithElasticToolkit.All
class ElasticSortTransformerTest {

    private static SortTransformer<SortOptions, Object> sortTransformer;

    @Test
    @DisplayName("Field")
    void testFieldSort() {
        final var sort = FieldSort.builder()
                .order(SortOrder.ASC)
                .field("field")
                .sortMode("Max")
                .numericType("Long")
                .fallbackFieldType("Long")
                .missingValue(0L)
                .build();

        final var expected = new SortOptions.Builder().field(
                new Builder()
                        .field("field")
                        .order(co.elastic.clients.elasticsearch._types.SortOrder.Asc)
                        .mode(SortMode.Max)
                        .numericType(FieldSortNumericType.Long)
                        .unmappedType(FieldType.Long)
                        .missing(0L)
                        .build())
                .build();

        final var sortBuilder = sortTransformer.transform(sort);

        assertEquals(expected.toString(), sortBuilder.toString(), "must return a field sort");
    }

    @Test
    @DisplayName("Score")
    void testScoreSort() {
        final var sort = ScoreSort.builder()
                .order(SortOrder.DESC)
                .build();

        final var expected = new SortOptions.Builder()
                .score(new co.elastic.clients.elasticsearch._types.ScoreSort.Builder()
                        .order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)
                        .build())
                .build();

        final var sortBuilder = sortTransformer.transform(sort);

        assertEquals(expected.toString(), sortBuilder.toString(), "must return a score sort");
    }

    @Test
    @DisplayName("Script")
    void testScriptSort() {
        final var sort = ScriptSort.builder()
                .script(Script.builder()
                        .id("id")
                        .build())
                .valueType("Number")
                .sortMode("Min")
                .build();

        final var script = new co.elastic.clients.elasticsearch._types.Script.Builder()
                .id("id")
                .params(Map.of())
                .build();

        final var expected = new SortOptions.Builder()
                .script(new co.elastic.clients.elasticsearch._types.ScriptSort.Builder()
                        .script(script)
                        .mode(SortMode.Min)
                        .type(ScriptSortType.Number)
                        .build())
                .build();

        final var sortBuilder = sortTransformer.transform(sort);

        assertEquals(expected.toString(), sortBuilder.toString(), "must return a script sort");
    }

    @Test
    @DisplayName("Geo Distance")
    void testGeoDistanceSort() {
        final var sort = GeoDistanceSort.builder()
                .order(SortOrder.ASC)
                .field("location")
                .point(Location.builder()
                        .lat(10d)
                        .lon(20d)
                        .build())
                .distanceType("arc")
                .distanceUnit("km")
                .sortMode("min")
                .ignoreMissingField(true)
                .build();

        final var expected = new SortOptions.Builder()
                .geoDistance(new co.elastic.clients.elasticsearch._types.GeoDistanceSort.Builder()
                        .field("location")
                        .order(co.elastic.clients.elasticsearch._types.SortOrder.Asc)
                        .distanceType(GeoDistanceType.Arc)
                        .unit(DistanceUnit.Kilometers)
                        .mode(SortMode.Min)
                        .ignoreUnmapped(true)
                        .location(List.of(new GeoLocation.Builder()
                                        .latlon(new LatLonGeoLocation.Builder()
                                                .lat(10d)
                                                .lon(20d)
                                                .build())
                                .build()))
                        .build())
                .build();

        final var sortBuilder = sortTransformer.transform(sort);

        assertEquals(expected.toString(), sortBuilder.toString(), "must return a geo distance sort");
    }

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        sortTransformer = toolkit.sortTransformer();
    }
}
