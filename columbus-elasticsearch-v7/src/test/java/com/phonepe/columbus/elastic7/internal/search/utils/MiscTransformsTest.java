package com.phonepe.columbus.elastic7.internal.search.utils;

import com.phonepe.columbus.search.misc.bucket.BucketSortOrder;
import com.phonepe.columbus.search.misc.bucket.IncludeExclude;
import com.phonepe.columbus.search.misc.result.TotalCount;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.TotalHits.Relation;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiscTransformsTest {

    @Test
    @DisplayName("custom bucket order")
    void testBucketOrder() {
        final var bucketSortOrder = BucketSortOrder.builder()
                .name("custom")
                .build();

        final var expected = BucketOrder.aggregation("custom", true);

        final var actual = MiscTransforms.toElastic(bucketSortOrder);

        assertEquals(expected, actual, "bucket order must retain name");
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
            final var expected = new org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude(
                    new String[]{"i1", "i2"}, new String[]{"e1", "e2"});

            final var actual = MiscTransforms.toElastic(includeExclude);

            assertEquals(expected, actual, "include exclude with strings must match");
        }

        @Test
        @DisplayName("regex")
        void testRegex() {
            final var includeExclude = IncludeExclude.builder()
                    .isRegex(true)
                    .include("i1")
                    .exclude("e1")
                    .build();
            final var expected = new org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude("i1", "e1");

            final var actual = MiscTransforms.toElastic(includeExclude);

            assertEquals(expected, actual, "include exclude with regex must match");
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
            final var expected = new org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude("i1", "e1");

            final var actual = MiscTransforms.toElastic(includeExclude);

            assertEquals(expected, actual, "include exclude with multiple regex only takes first");
        }
    }

    @Nested
    class TotalCountTest {

        @Test
        @DisplayName("exact")
        void testExact() {
            final var totalHits = new TotalHits(100, Relation.EQUAL_TO);

            final var expected = TotalCount.exact(100);

            final var actual = MiscTransforms.fromElastic(totalHits);

            assertEquals(expected, actual, "total count must match total hits");
        }

        @Test
        @DisplayName("lower bound")
        void testLowerBound() {
            final var totalHits = new TotalHits(10, Relation.GREATER_THAN_OR_EQUAL_TO);

            final var expected = TotalCount.atLeast(10);

            final var actual = MiscTransforms.fromElastic(totalHits);

            assertEquals(expected, actual, "total count must match total hits");
        }
    }
}
