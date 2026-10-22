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
package com.phonepe.columbus.elastic9.internal.search.utils;

import co.elastic.clients.elasticsearch._types.ErrorCause;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.phonepe.columbus.search.misc.geo.Location;
import com.phonepe.columbus.search.misc.query.Projection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MiscTransformsTest {

    @Nested
    class ToElasticTime {

        @Test
        @DisplayName("String value")
        void testStringTime() {
            final Time time = MiscTransforms.toElasticTime("30d");
            assertNotNull(time);
            assertEquals("30d", time.time());
        }

        @Test
        @DisplayName("Number value")
        void testNumberTime() {
            final Time time = MiscTransforms.toElasticTime(5000);
            assertNotNull(time);
            assertEquals(5000, time.offset());
        }

        @Test
        @DisplayName("Unsupported type throws")
        void testUnsupportedType() {
            final var time = new Object();
            assertThrows(IllegalArgumentException.class, () -> MiscTransforms.toElasticTime(time));
        }
    }

    @Nested
    class ToElasticOrigin {

        @Test
        @DisplayName("String lat,lon")
        void testStringOrigin() {
            final GeoLocation geo = MiscTransforms.toElasticOrigin("12.97, 77.59");
            assertNotNull(geo);
            assertNotNull(geo.latlon());
            assertEquals(12.97, geo.latlon().lat(), 0.01);
            assertEquals(77.59, geo.latlon().lon(), 0.01);
        }

        @Test
        @DisplayName("Location object")
        void testLocationOrigin() {
            final GeoLocation geo = MiscTransforms.toElasticOrigin(
                    Location.builder().lat(28.6).lon(77.2).build());
            assertNotNull(geo);
            assertNotNull(geo.latlon());
            assertEquals(28.6, geo.latlon().lat(), 0.01);
            assertEquals(77.2, geo.latlon().lon(), 0.01);
        }

        @Test
        @DisplayName("Invalid string throws")
        void testInvalidStringOrigin() {
            assertThrows(IllegalArgumentException.class, () -> MiscTransforms.toElasticOrigin("invalid"));
        }

        @Test
        @DisplayName("Unsupported type throws")
        void testUnsupportedTypeOrigin() {
            assertThrows(IllegalArgumentException.class, () -> MiscTransforms.toElasticOrigin(42));
        }
    }

    @Nested
    class FromElasticErrorCause {

        @Test
        @DisplayName("null error returns empty string")
        void testNullError() {
            assertEquals("", MiscTransforms.fromElastic((ErrorCause) null));
        }

        @Test
        @DisplayName("error with type and reason")
        void testErrorWithTypeAndReason() {
            final var error = ErrorCause.of(b -> b.type("index_not_found")
                    .reason("no such index [test]"));
            final var result = MiscTransforms.fromElastic(error);
            assertTrue(result.contains("Type: index_not_found"));
            assertTrue(result.contains("Reason: no such index [test]"));
        }

        @Test
        @DisplayName("error with only type (null reason)")
        void testErrorWithOnlyType() {
            final var error = ErrorCause.of(b -> b.type("parse_exception").reason(null));
            final var result = MiscTransforms.fromElastic(error);
            assertTrue(result.contains("Type: parse_exception"));
        }
    }

    @Nested
    class TotalCountTest {

        @Test
        @DisplayName("null TotalHits returns null")
        void testNullTotalHits() {
            assertNull(MiscTransforms.fromElastic((TotalHits) null));
        }

        @Test
        @DisplayName("Eq relation returns exact count")
        void testExactCount() {
            final var totalHits = new TotalHits.Builder()
                    .value(42)
                    .relation(TotalHitsRelation.Eq)
                    .build();
            final var result = MiscTransforms.fromElastic(totalHits);
            assertNotNull(result);
            assertEquals(42, result.getCount());
            assertTrue(result.exact());
        }

        @Test
        @DisplayName("Gte relation returns at-least count")
        void testAtLeastCount() {
            final var totalHits = new TotalHits.Builder()
                    .value(10000)
                    .relation(TotalHitsRelation.Gte)
                    .build();
            final var result = MiscTransforms.fromElastic(totalHits);
            assertNotNull(result);
            assertEquals(10000, result.getCount());
            assertFalse(result.exact());
        }
    }

    @Nested
    class ProjectionTest {

        @Test
        @DisplayName("disabled projection returns fetch=false")
        void testDisabledProjection() {
            final var projection = Projection.builder()
                    .disabled(true)
                    .build();
            final SourceConfig config = MiscTransforms.toElastic(projection);
            assertNotNull(config);
            assertFalse(config.fetch());
        }

        @Test
        @DisplayName("enabled projection with includes/excludes")
        void testEnabledProjection() {
            final var projection = Projection.builder()
                    .includeFields(List.of("name", "id"))
                    .excludeFields(List.of("secret"))
                    .build();
            final SourceConfig config = MiscTransforms.toElastic(projection);
            assertNotNull(config);
            assertNotNull(config.filter());
            assertEquals(List.of("name", "id"), config.filter().includes());
            assertEquals(List.of("secret"), config.filter().excludes());
        }
    }
}
