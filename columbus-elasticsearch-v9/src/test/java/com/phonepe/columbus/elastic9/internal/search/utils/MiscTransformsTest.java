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

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.TermsExclude;
import co.elastic.clients.elasticsearch._types.aggregations.TermsInclude;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.util.NamedValue;
import com.phonepe.columbus.search.misc.bucket.BucketSortOrder;
import com.phonepe.columbus.search.misc.bucket.IncludeExclude;
import com.phonepe.columbus.search.misc.result.TotalCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiscTransformsTest {

    @Test
    @DisplayName("custom bucket order")
    void testBucketOrder() {
        final var bucketSortOrder = BucketSortOrder.builder()
                .name("custom")
                .build();

        final var expected = NamedValue.of("custom", SortOrder.Asc);

        final var actual = MiscTransforms.toElastic(bucketSortOrder);

        assertEquals(expected.name(), actual.name(), "bucket order must retain name");
        assertEquals(expected.value(), actual.value(), "bucket order must retain name");
    }

    @Nested
    class IncludeExcludeTest {

        @Test
        @DisplayName("strings")
        void testStrings() {
            final var includeExclude = IncludeExclude.builder()
                    .include("i1")
                    .include("i2")
                    .exclude("e1")
                    .exclude("e2")
                    .build();
            final var expectedInclude = new TermsInclude.Builder()
                    .terms(List.of("i1", "i2"))
                    .build();
            final var expectedExclude = new TermsExclude.Builder()
                    .terms(List.of("e1", "e2"))
                    .build();

            final var actualInclude = MiscTransforms.toElasticTermsInclude(includeExclude);
            final var actualExclude = MiscTransforms.toElasticTermsExclude(includeExclude);

            assertEquals(expectedInclude.toString(), actualInclude.toString(), "include with strings must match");
            assertEquals(expectedExclude.toString(), actualExclude.toString(), "exclude with strings must match");
        }

        @Test
        @DisplayName("regex")
        void testRegex() {
            final var includeExclude = IncludeExclude.builder()
                    .isRegex(true)
                    .include("i1")
                    .exclude("e1")
                    .build();

            final var expectedInclude = new TermsInclude.Builder()
                    .regexp("i1")
                    .build();
            final var expectedExclude = new TermsExclude.Builder()
                    .regexp("e1")
                    .build();

            final var actualInclude = MiscTransforms.toElasticTermsInclude(includeExclude);
            final var actualExclude = MiscTransforms.toElasticTermsExclude(includeExclude);

            assertEquals(expectedInclude.toString(), actualInclude.toString(), "include with regex must match");
            assertEquals(expectedExclude.toString(), actualExclude.toString(), "exclude with regex must match");
        }

        @Test
        @DisplayName("regex multiple")
        void testRegexMultiple() {
            final var includeExclude = IncludeExclude.builder()
                    .isRegex(true)
                    .include("i1")
                    .include("i2")
                    .exclude("e1")
                    .exclude("e2")
                    .build();

            final var expectedInclude = new TermsInclude.Builder()
                    .regexp("i1")
                    .build();
            final var expectedExclude = new TermsExclude.Builder()
                    .regexp("e1")
                    .build();

            final var actualInclude = MiscTransforms.toElasticTermsInclude(includeExclude);
            final var actualExclude = MiscTransforms.toElasticTermsExclude(includeExclude);

            assertEquals(expectedInclude.toString(), actualInclude.toString(), "include with multiple regex only "
                    + "takes first");
            assertEquals(expectedExclude.toString(), actualExclude.toString(), "exclude with multiple regex only "
                    + "takes first");
        }
    }

    @Nested
    class TotalCountTest {

        @Test
        @DisplayName("exact")
        void testExact() {
            final var totalHits = new TotalHits.Builder()
                    .value(100)
                    .relation(TotalHitsRelation.Eq)
                    .build();

            final var expected = TotalCount.exact(100);

            final var actual = MiscTransforms.fromElastic(totalHits);

            assertEquals(expected, actual, "total count must match total hits");
        }

        @Test
        @DisplayName("lower bound")
        void testLowerBound() {
            final var totalHits = new TotalHits.Builder()
                    .value(100)
                    .relation(TotalHitsRelation.Gte)
                    .build();

            final var expected = TotalCount.atLeast(100);

            final var actual = MiscTransforms.fromElastic(totalHits);

            assertEquals(expected, actual, "total count must match total hits");
        }
    }
}
