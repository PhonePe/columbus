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
package com.phonepe.columbus.elastic7.internal.search.sort;

import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.misc.geo.Location;
import com.phonepe.columbus.search.misc.script.Script;
import com.phonepe.columbus.search.sort.FieldSort;
import com.phonepe.columbus.search.sort.GeoDistanceSort;
import com.phonepe.columbus.search.sort.ScoreSort;
import com.phonepe.columbus.search.sort.ScriptSort;
import com.phonepe.columbus.search.sort.SortOrder;
import com.phonepe.columbus.search.sort.SortTransformer;
import com.phonepe.columbus.elastic7.test.annotations.WithElasticToolkit;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.sort.ScriptSortBuilder.ScriptSortType;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortMode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithElasticToolkit.All
class ElasticSortTransformerTest {

    private static SortTransformer<SortBuilder<?>, Object> sortTransformer;

    @Test
    @DisplayName("Field")
    void testFieldSort() {
        final var sort = FieldSort.builder()
                .order(SortOrder.ASC)
                .field("field")
                .sortMode("MAX")
                .numericType("long")
                .fallbackFieldType("long")
                .missingValue(0L)
                .build();

        final var expected = SortBuilders.fieldSort("field")
                .order(org.elasticsearch.search.sort.SortOrder.ASC)
                .sortMode(SortMode.MAX)
                .setNumericType("long")
                .unmappedType("long")
                .missing(0L);

        final var sortBuilder = sortTransformer.transform(sort);

        assertEquals(expected, sortBuilder, "must return a field sort");
    }

    @Test
    @DisplayName("Score")
    void testScoreSort() {
        final var sort = ScoreSort.builder()
                .order(SortOrder.DESC)
                .build();

        final var expected = SortBuilders.scoreSort();

        final var sortBuilder = sortTransformer.transform(sort);

        assertEquals(expected, sortBuilder, "must return a score sort");
    }

    @Test
    @DisplayName("Script")
    void testScriptSort() {
        final var sort = ScriptSort.builder()
                .script(Script.builder()
                        .id("id")
                        .build())
                .valueType("number")
                .sortMode("min")
                .build();

        final var script = new org.elasticsearch.script.Script(
                ScriptType.STORED, null, "id", Map.of());
        final var expected = SortBuilders.scriptSort(script, ScriptSortType.NUMBER)
                .sortMode(SortMode.MIN);

        final var sortBuilder = sortTransformer.transform(sort);

        assertEquals(expected, sortBuilder, "must return a script sort");
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

        final var expected = SortBuilders.geoDistanceSort("location", 10d, 20d)
                .geoDistance(GeoDistance.ARC)
                .unit(DistanceUnit.KILOMETERS)
                .sortMode(SortMode.MIN)
                .ignoreUnmapped(true);

        final var sortBuilder = sortTransformer.transform(sort);

        assertEquals(expected, sortBuilder, "must return a geo distance sort");
    }

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        sortTransformer = toolkit.sortTransformer();
    }
}
