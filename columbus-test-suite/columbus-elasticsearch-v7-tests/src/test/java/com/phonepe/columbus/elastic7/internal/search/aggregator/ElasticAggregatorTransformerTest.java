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
package com.phonepe.columbus.elastic7.internal.search.aggregator;

import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.columbus.search.aggregator.bucket.FilterAggregator;
import com.phonepe.columbus.search.aggregator.bucket.FiltersAggregator;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator.IntervalType;
import com.phonepe.columbus.search.aggregator.bucket.RangeAggregator;
import com.phonepe.columbus.search.aggregator.bucket.SamplerAggregator;
import com.phonepe.columbus.search.aggregator.bucket.TermsAggregator;
import com.phonepe.columbus.search.aggregator.metrics.CardinalityAggregator;
import com.phonepe.columbus.search.aggregator.metrics.StatsAggregator;
import com.phonepe.columbus.search.aggregator.metrics.TopHitsAggregator;
import com.phonepe.columbus.search.filter.general.EqualsFilter;
import com.phonepe.columbus.search.misc.bucket.BucketSortOrder;
import com.phonepe.columbus.search.misc.bucket.IncludeExclude;
import com.phonepe.columbus.search.misc.bucket.NamedRange;
import com.phonepe.columbus.search.misc.query.Projection;
import com.phonepe.columbus.search.sort.ScoreSort;
import com.phonepe.columbus.elastic7.test.annotations.WithElasticToolkit;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregator.SubAggCollectionMode;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator.KeyedFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.DoubleBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.LongBounds;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithElasticToolkit.All
class ElasticAggregatorTransformerTest {

    private static AggregatorTransformer<AggregationBuilder, Object> aggregatorTransformer;

    @Test
    @DisplayName("Terms")
    void testTermsAggregator() {
        final var aggregator = TermsAggregator.builder()
                .name("count")
                .field("field")
                .size(10)
                .shardSize(50)
                .includeExclude(IncludeExclude.builder()
                        .include("key1")
                        .include("key2")
                        .exclude("key11")
                        .build())
                .order(BucketSortOrder.count().build())
                .minDocCount(1)
                .shardMinDocCount(0)
                .missingValue("missing")
                .collectMode("depth_first")
                .showCountError(false)
                .subAggregator(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .order(BucketSortOrder.key().build())
                        .build())
                .build();

        final var expected = AggregationBuilders.terms("count")
                .field("field")
                .size(10)
                .shardSize(50)
                .includeExclude(new org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude(
                        new String[]{"key1", "key2"}, new String[]{"key11"}))
                .order(BucketOrder.count(false))
                .minDocCount(1)
                .shardMinDocCount(0)
                .missing("missing")
                .collectMode(SubAggCollectionMode.DEPTH_FIRST)
                .showTermDocCountError(false)
                .subAggregation(AggregationBuilders.terms("keys")
                        .field("other_field")
                        .order(BucketOrder.key(true)));

        final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

        assertEquals(expected, aggregationBuilder, "must return a terms aggregation");
    }

    @Test
    @DisplayName("Range")
    void testRangeAggregator() {
        final var aggregator = RangeAggregator.builder()
                .name("ranges")
                .field("field")
                .range(NamedRange.builder()
                        .name("low")
                        .to(10d)
                        .build())
                .range(NamedRange.builder()
                        .name("mid")
                        .from(10d)
                        .to(100d)
                        .build())
                .range(NamedRange.builder()
                        .name("high")
                        .from(100d)
                        .build())
                .subAggregator(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .order(BucketSortOrder.key().build())
                        .build())
                .build();

        final var expected = AggregationBuilders.range("ranges")
                .field("field")
                .addUnboundedTo("low", 10)
                .addRange("mid", 10, 100)
                .addUnboundedFrom("high", 100)
                .subAggregation(AggregationBuilders.terms("keys")
                        .field("other_field")
                        .order(BucketOrder.key(true)));

        final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

        assertEquals(expected, aggregationBuilder, "must return a range aggregation");
    }

    @Test
    @DisplayName("Sampler")
    void testSamplerAggregator() {
        final var aggregator = SamplerAggregator.builder()
                .name("sampler")
                .shardSize(10)
                .build();

        final var expected = AggregationBuilders.sampler("sampler")
                .shardSize(10);

        final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

        assertEquals(expected, aggregationBuilder, "must return a sampler aggregation");
    }

    @Test
    @DisplayName("Diversified Sampler")
    void testDiversifiedSamplerAggregator() {
        final var aggregator = SamplerAggregator.builder()
                .name("diversified-sampler")
                .shardSize(10)
                .diversifyField("field")
                .maxDuplicates(2)
                .build();

        final var expected = AggregationBuilders.diversifiedSampler("diversified-sampler")
                .shardSize(10)
                .field("field")
                .maxDocsPerValue(2);

        final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

        assertEquals(expected, aggregationBuilder, "must return a diversified sampler aggregation");
    }

    @Test
    @DisplayName("Stats")
    void testStatsAggregator() {
        final var aggregator = StatsAggregator.builder()
                .name("stats")
                .field("field")
                .missingValue(10)
                .build();

        final var expected = AggregationBuilders.stats("stats")
                .field("field")
                .missing(10);

        final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

        assertEquals(expected, aggregationBuilder, "must return a stats aggregation");
    }

    @Test
    @DisplayName("Top Hits")
    void testTopHitsAggregator() {
        final var aggregator = TopHitsAggregator.builder()
                .name("top_hits")
                .from(1)
                .size(10)
                .sort(ScoreSort.builder().build())
                .projection(Projection.builder()
                        .disabled(false)
                        .includeField("field_i")
                        .excludeField("field_e")
                        .build())
                .build();

        final var expected = AggregationBuilders.topHits("top_hits")
                .from(1)
                .size(10)
                .sort(SortBuilders.scoreSort())
                .fetchSource(true)
                .fetchSource("field_i", "field_e");

        final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

        assertEquals(expected, aggregationBuilder, "must return a top hits aggregation");
    }

    @Test
    @DisplayName("Cardinality")
    void testCardinalityAggregator() {
        final var aggregator = CardinalityAggregator.builder()
                .name("cardinality")
                .field("field")
                .missingValue(10)
                .build();

        final var expected = AggregationBuilders.cardinality("cardinality")
                .field("field")
                .missing(10);

        final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

        assertEquals(expected, aggregationBuilder, "must return a cardinality aggregation");
    }

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        aggregatorTransformer = toolkit.aggregatorTransformer();
    }

    @Nested
    class Filter {

        @Test
        @DisplayName("with filter")
        void testWithFilter() {
            final var aggregator = FilterAggregator.builder()
                    .name("filter")
                    .filter(EqualsFilter.builder()
                            .field("field")
                            .value(10)
                            .build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.filter("filter", QueryBuilders.termQuery("field", 10))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a filter aggregation");
        }

        @Test
        @DisplayName("without filter")
        void testWithoutFilter() {
            final var aggregator = FilterAggregator.builder()
                    .name("filter")
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.filter("filter", QueryBuilders.matchAllQuery())
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a filter aggregation with match all");
        }
    }

    @Nested
    class Filters {

        @Test
        @DisplayName("with other bucket")
        void testWithOtherBucket() {
            final var aggregator = FiltersAggregator.builder()
                    .name("filters")
                    .filter("bucket_1", EqualsFilter.builder()
                            .field("field")
                            .value(10)
                            .build())
                    .filter("bucket_2", EqualsFilter.builder()
                            .field("field")
                            .value(20)
                            .build())
                    .otherBucketKey("other_bucket")
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.filters(
                            "filters",
                            new KeyedFilter("bucket_1", QueryBuilders.termQuery("field", 10)),
                            new KeyedFilter("bucket_2", QueryBuilders.termQuery("field", 20)))
                    .otherBucket(true)
                    .otherBucketKey("other_bucket")
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a filters aggregation with other bucket");
        }

        @Test
        @DisplayName("without other bucket")
        void testWithoutOtherBucket() {
            final var aggregator = FiltersAggregator.builder()
                    .name("filters")
                    .filter("bucket_1", EqualsFilter.builder()
                            .field("field")
                            .value(10)
                            .build())
                    .filter("bucket_2", EqualsFilter.builder()
                            .field("field")
                            .value(20)
                            .build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.filters(
                            "filters",
                            new KeyedFilter("bucket_1", QueryBuilders.termQuery("field", 10)),
                            new KeyedFilter("bucket_2", QueryBuilders.termQuery("field", 20)))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a filters aggregation with other bucket");
        }
    }

    @Nested
    class Histogram {

        @Test
        @DisplayName("numeric, unbounded")
        void testNumericUnboundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("histogram")
                    .field("field")
                    .intervalType(IntervalType.NUMERIC)
                    .interval("10")
                    .offset("1")
                    .order(BucketSortOrder.count().build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.histogram("histogram")
                    .field("field")
                    .interval(10)
                    .offset(1)
                    .order(BucketOrder.count(false))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a histogram aggregation");
        }

        @Test
        @DisplayName("numeric, hard bounded")
        void testNumericHardBoundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("histogram")
                    .field("field")
                    .intervalType(IntervalType.NUMERIC)
                    .interval("10")
                    .offset("1")
                    .minValue(10d)
                    .maxValue(50d)
                    .softBounds(false)
                    .order(BucketSortOrder.count().build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.histogram("histogram")
                    .field("field")
                    .interval(10)
                    .offset(1)
                    .hardBounds(new DoubleBounds(10d, 50d))
                    .order(BucketOrder.count(false))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a histogram aggregation");
        }

        @Test
        @DisplayName("numeric, partially soft bounded")
        void testNumericPartiallySoftBoundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("histogram")
                    .field("field")
                    .intervalType(IntervalType.NUMERIC)
                    .interval("10")
                    .offset("1")
                    .minValue(10d)
                    .softBounds(true)
                    .order(BucketSortOrder.count().build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.histogram("histogram")
                    .field("field")
                    .interval(10)
                    .offset(1)
                    .extendedBounds(new DoubleBounds(10d, null))
                    .order(BucketOrder.count(false))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a histogram aggregation");
        }

        @Test
        @DisplayName("fixed date, unbounded")
        void testFixedDateUnboundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("date_histogram")
                    .field("field")
                    .intervalType(IntervalType.FIXED_DATE)
                    .interval("10d")
                    .offset("1d")
                    .order(BucketSortOrder.count().build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.dateHistogram("date_histogram")
                    .field("field")
                    .fixedInterval(DateHistogramInterval.days(10))
                    .offset("1d")
                    .order(BucketOrder.count(false))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a date histogram aggregation");
        }

        @Test
        @DisplayName("fixed date, hard bounded")
        void testFixedDateHardBoundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("date_histogram")
                    .field("field")
                    .intervalType(IntervalType.FIXED_DATE)
                    .interval("10d")
                    .offset("1d")
                    .minValue(170000000000L)
                    .maxValue(180000000000L)
                    .order(BucketSortOrder.count().build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.dateHistogram("date_histogram")
                    .field("field")
                    .fixedInterval(DateHistogramInterval.days(10))
                    .offset("1d")
                    .hardBounds(new LongBounds(170000000000L, 180000000000L))
                    .order(BucketOrder.count(false))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a date histogram aggregation");
        }

        @Test
        @DisplayName("fixed date, partially soft bounded")
        void testFixedDatePartiallySoftBoundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("date_histogram")
                    .field("field")
                    .intervalType(IntervalType.FIXED_DATE)
                    .interval("10d")
                    .offset("1d")
                    .minValue(170000000000L)
                    .softBounds(true)
                    .order(BucketSortOrder.count().build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.dateHistogram("date_histogram")
                    .field("field")
                    .fixedInterval(DateHistogramInterval.days(10))
                    .offset("1d")
                    .extendedBounds(new LongBounds(170000000000L, null))
                    .order(BucketOrder.count(false))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a date histogram aggregation");
        }

        @Test
        @DisplayName("calendar date, unbounded")
        void testCalendarDateUnboundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("date_histogram")
                    .field("field")
                    .intervalType(IntervalType.CALENDAR_DATE)
                    .interval("1M")
                    .offset("1d")
                    .order(BucketSortOrder.count().build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.dateHistogram("date_histogram")
                    .field("field")
                    .calendarInterval(DateHistogramInterval.MONTH)
                    .offset("1d")
                    .order(BucketOrder.count(false))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a date histogram aggregation");
        }

        @Test
        @DisplayName("calendar date, hard bounded")
        void testCalendarDateHardBoundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("date_histogram")
                    .field("field")
                    .intervalType(IntervalType.CALENDAR_DATE)
                    .interval("1y")
                    .offset("1d")
                    .minValue(170000000000L)
                    .maxValue(180000000000L)
                    .order(BucketSortOrder.count().build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.dateHistogram("date_histogram")
                    .field("field")
                    .calendarInterval(DateHistogramInterval.YEAR)
                    .offset("1d")
                    .hardBounds(new LongBounds(170000000000L, 180000000000L))
                    .order(BucketOrder.count(false))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a date histogram aggregation");
        }

        @Test
        @DisplayName("calendar date, partially soft bounded")
        void testCalendarDatePartiallySoftBoundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("date_histogram")
                    .field("field")
                    .intervalType(IntervalType.CALENDAR_DATE)
                    .interval("1q")
                    .offset("1d")
                    .minValue(170000000000L)
                    .softBounds(true)
                    .order(BucketSortOrder.count().build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = AggregationBuilders.dateHistogram("date_histogram")
                    .field("field")
                    .calendarInterval(DateHistogramInterval.QUARTER)
                    .offset("1d")
                    .extendedBounds(new LongBounds(170000000000L, null))
                    .order(BucketOrder.count(false))
                    .subAggregation(AggregationBuilders.terms("keys")
                            .field("other_field")
                            .order(BucketOrder.key(true)));

            final var aggregationBuilder = aggregatorTransformer.transform(aggregator);

            assertEquals(expected, aggregationBuilder, "must return a date histogram aggregation");
        }
    }
}
