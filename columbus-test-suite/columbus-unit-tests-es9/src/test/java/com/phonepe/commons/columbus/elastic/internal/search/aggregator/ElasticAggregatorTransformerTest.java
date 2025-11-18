package com.phonepe.commons.columbus.elastic.internal.search.aggregator;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.DiversifiedSamplerAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.ExtendedBounds;
import co.elastic.clients.elasticsearch._types.aggregations.FieldDateMath;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.RangeAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.SamplerAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StatsAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregationCollectMode;
import co.elastic.clients.elasticsearch._types.aggregations.TermsExclude;
import co.elastic.clients.elasticsearch._types.aggregations.TermsInclude;
import co.elastic.clients.elasticsearch._types.aggregations.TopHitsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.elasticsearch.core.search.SourceFilter;
import co.elastic.clients.util.NamedValue;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.commons.columbus.search.aggregator.bucket.FilterAggregator;
import com.phonepe.commons.columbus.search.aggregator.bucket.FiltersAggregator;
import com.phonepe.commons.columbus.search.aggregator.bucket.HistogramAggregator;
import com.phonepe.commons.columbus.search.aggregator.bucket.HistogramAggregator.IntervalType;
import com.phonepe.commons.columbus.search.aggregator.bucket.RangeAggregator;
import com.phonepe.commons.columbus.search.aggregator.bucket.SamplerAggregator;
import com.phonepe.commons.columbus.search.aggregator.bucket.TermsAggregator;
import com.phonepe.commons.columbus.search.aggregator.metrics.CardinalityAggregator;
import com.phonepe.commons.columbus.search.aggregator.metrics.StatsAggregator;
import com.phonepe.commons.columbus.search.aggregator.metrics.TopHitsAggregator;
import com.phonepe.commons.columbus.search.filter.general.EqualsFilter;
import com.phonepe.commons.columbus.search.misc.bucket.BucketSortOrder;
import com.phonepe.commons.columbus.search.misc.bucket.IncludeExclude;
import com.phonepe.commons.columbus.search.misc.bucket.NamedRange;
import com.phonepe.commons.columbus.search.misc.query.Projection;
import com.phonepe.commons.columbus.search.sort.ScoreSort;
import com.phonepe.commons.columbus.es9.test.annotations.WithElasticToolkit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithElasticToolkit.All
class ElasticAggregatorTransformerTest {

    private static AggregatorTransformer<Aggregation, Object> aggregatorTransformer;

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

        final var expected = new Aggregation.Builder()
                .terms(new TermsAggregation.Builder()
                        .field("field")
                        .size(10)
                        .shardSize(50)
                        .include(new TermsInclude.Builder()
                                .terms(List.of("key1", "key2"))
                                .build())
                        .exclude(new TermsExclude.Builder()
                                .terms(List.of("key11"))
                                .build())
                        .order(NamedValue.of("_count", SortOrder.Desc))
                        .minDocCount(1)
                        .shardMinDocCount(0L)
                        .missing("missing")
                        .collectMode(TermsAggregationCollectMode.DepthFirst)
                        .showTermDocCountError(false)
                        .build())
                .aggregations(Map.of("keys", new Aggregation.Builder()
                        .terms(new TermsAggregation.Builder()
                                .field("other_field")
                                .order(NamedValue.of("_key", SortOrder.Asc))
                                .build())
                        .build()))
                .build();

        final var aggregation = aggregatorTransformer.transform(aggregator);

        assertEquals(expected.toString(), aggregation.toString(), "must return a terms aggregation");
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
                        .to(100d)
                        .build())
                .subAggregator(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .order(BucketSortOrder.key().build())
                        .build())
                .build();

        final var expected = new Aggregation.Builder()
                .range(new RangeAggregation.Builder()
                        .field("field")
                        .ranges(new AggregationRange.Builder()
                                .key("low")
                                .to(10d)
                                .build())
                        .ranges(new AggregationRange.Builder()
                                .key("mid")
                                .from(10d)
                                .to(100d)
                                .build())
                        .ranges(new AggregationRange.Builder()
                                .key("high")
                                .to(100d)
                                .build())
                        .build())
                .aggregations(Map.of("keys", new Aggregation.Builder()
                        .terms(new TermsAggregation.Builder()
                                .field("other_field")
                                .order(NamedValue.of("_key", SortOrder.Asc))
                                .build())
                        .build()))
                .build();

        final var aggregation = aggregatorTransformer.transform(aggregator);

        assertEquals(expected.toString(), aggregation.toString(), "must return a range aggregation");
    }

    @Test
    @DisplayName("Sampler")
    void testSamplerAggregator() {
        final var aggregator = SamplerAggregator.builder()
                .name("sampler")
                .shardSize(10)
                .build();

        final var expected = new Aggregation.Builder()
                .sampler(new SamplerAggregation.Builder()
                        .shardSize(10)
                        .build())
                .build();

        final var aggregation = aggregatorTransformer.transform(aggregator);

        assertEquals(expected.toString(), aggregation.toString(), "must return a sampler aggregation");
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

        final var expected = new Aggregation.Builder()
                .diversifiedSampler(new DiversifiedSamplerAggregation.Builder()
                        .field("field")
                        .maxDocsPerValue(2)
                        .shardSize(10)
                        .build())
                .build();

        final var aggregation = aggregatorTransformer.transform(aggregator);

        assertEquals(expected.toString(), aggregation.toString(), "must return a diversified sampler aggregation");
    }

    @Test
    @DisplayName("Stats")
    void testStatsAggregator() {
        final var aggregator = StatsAggregator.builder()
                .name("stats")
                .field("field")
                .missingValue(10)
                .build();

        final var expected = new Aggregation.Builder()
                .stats(new StatsAggregation.Builder()
                        .field("field")
                        .missing(10l)
                        .build())
                .build();

        final var aggregation = aggregatorTransformer.transform(aggregator);

        assertEquals(expected.toString(), aggregation.toString(), "must return a stats aggregation");
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

        final var expected = new Aggregation.Builder()
                .topHits(new TopHitsAggregation.Builder()
                        .from(1)
                        .size(10)
                        .sort(new SortOptions.Builder()
                                .score(new co.elastic.clients.elasticsearch._types.ScoreSort.Builder()
                                        .build())
                                .build())
                        .source(new SourceConfig.Builder()
                                .filter(new SourceFilter.Builder()
                                        .includes("field_i")
                                        .excludes("field_e")
                                        .excludeVectors(false)
                                        .build())
                                .build())
                        .build())
                .build();

        final var aggregation = aggregatorTransformer.transform(aggregator);

        assertEquals(expected.toString(), aggregation.toString(), "must return a top hits aggregation");
    }

    @Test
    @DisplayName("Cardinality")
    void testCardinalityAggregator() {
        final var aggregator = CardinalityAggregator.builder()
                .name("cardinality")
                .field("field")
                .missingValue(10)
                .build();

        final var expected = new Aggregation.Builder()
                .cardinality(new co.elastic.clients.elasticsearch._types.aggregations.CardinalityAggregation.Builder()
                        .field("field")
                        .missing(10l)
                        .build())
                .build();

        final var aggregation = aggregatorTransformer.transform(aggregator);

        assertEquals(expected.toString(), aggregation.toString(), "must return a cardinality aggregation");
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

            final var expected = new Aggregation.Builder()
                    .filter(new Query.Builder()
                            .term(new TermQuery.Builder()
                                    .field("field")
                                    .value(10)
                                    .build())
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a filter aggregation");
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

            final var expected = new Aggregation.Builder()
                    .filter(new Query.Builder()
                            .matchAll(new MatchAllQuery.Builder().build())
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a filter aggregation with match all");
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
                    .otherBucketKey("other_bucket")
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = new Aggregation.Builder()
                    .filters(new FiltersAggregation.Builder()
                            .filters(new Buckets.Builder<Query>()
                                    .keyed(Map.of("bucket_1", new Query.Builder()
                                            .term(new TermQuery.Builder()
                                                    .field("field")
                                                    .value(10)
                                                    .build())
                                            .build()))
                                    .build())
                            .otherBucket(true)
                            .otherBucketKey("other_bucket")
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a filters aggregation with other bucket");
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
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = new Aggregation.Builder()
                    .filters(new FiltersAggregation.Builder()
                            .filters(new Buckets.Builder<Query>()
                                    .keyed(Map.of("bucket_1", new Query.Builder()
                                                    .term(new TermQuery.Builder()
                                                            .field("field")
                                                            .value(10)
                                                            .build())
                                                    .build()))
                                    .build())
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a filters aggregation with other bucket");
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

            final var expected = new Aggregation.Builder()
                    .histogram(new HistogramAggregation.Builder()
                            .field("field")
                            .interval(10d)
                            .offset(1d)
                            .order(NamedValue.of("_count", SortOrder.Desc))
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a histogram aggregation");
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

            final var expected = new Aggregation.Builder()
                    .histogram(new HistogramAggregation.Builder()
                            .field("field")
                            .interval(10d)
                            .offset(1d)
                            .order(NamedValue.of("_count", SortOrder.Desc))
                            .hardBounds(new ExtendedBounds.Builder<Double>()
                                    .min(10d)
                                    .max(50d)
                                    .build())
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a histogram aggregation");
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

            final var expected = new Aggregation.Builder()
                    .histogram(new HistogramAggregation.Builder()
                            .field("field")
                            .interval(10d)
                            .offset(1d)
                            .order(NamedValue.of("_count", SortOrder.Desc))
                            .extendedBounds(new ExtendedBounds.Builder<Double>()
                                    .min(10d)
                                    .build())
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a histogram aggregation");
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

            final var expected = new Aggregation.Builder()
                    .dateHistogram(new DateHistogramAggregation.Builder()
                            .field("field")
                            .fixedInterval(new Time.Builder()
                                    .time("10d")
                                    .build())
                            .offset(new Time.Builder()
                                    .time("1d")
                                    .build())
                            .order(NamedValue.of("_count", SortOrder.Desc))
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a date histogram aggregation");
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

            final var expected = new Aggregation.Builder()
                    .dateHistogram(new DateHistogramAggregation.Builder()
                            .field("field")
                            .fixedInterval(new Time.Builder()
                                    .time("10d")
                                    .build())
                            .offset(new Time.Builder()
                                    .time("1d")
                                    .build())
                            .hardBounds(new ExtendedBounds.Builder<FieldDateMath>()
                                    .min(new FieldDateMath.Builder()
                                            .value(170000000000d)
                                            .build())
                                    .max(new FieldDateMath.Builder()
                                            .value(180000000000d)
                                            .build())
                                    .build())
                            .order(NamedValue.of("_count", SortOrder.Desc))
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a date histogram aggregation");
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

            final var expected = new Aggregation.Builder()
                    .dateHistogram(new DateHistogramAggregation.Builder()
                            .field("field")
                            .fixedInterval(new Time.Builder()
                                    .time("10d")
                                    .build())
                            .offset(new Time.Builder()
                                    .time("1d")
                                    .build())
                            .extendedBounds(new ExtendedBounds.Builder<FieldDateMath>()
                                    .min(new FieldDateMath.Builder()
                                            .value(170000000000d)
                                            .build())
                                    .build())
                            .order(NamedValue.of("_count", SortOrder.Desc))
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a date histogram aggregation");
        }

        @Test
        @DisplayName("calendar date, unbounded")
        void testCalendarDateUnboundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("date_histogram")
                    .field("field")
                    .intervalType(IntervalType.CALENDAR_DATE)
                    .interval("Month")
                    .offset("1d")
                    .order(BucketSortOrder.count().build())
                    .subAggregator(TermsAggregator.builder()
                            .name("keys")
                            .field("other_field")
                            .order(BucketSortOrder.key().build())
                            .build())
                    .build();

            final var expected = new Aggregation.Builder()
                    .dateHistogram(new DateHistogramAggregation.Builder()
                            .field("field")
                            .calendarInterval(CalendarInterval.Month)
                            .offset(new Time.Builder()
                                    .time("1d")
                                    .build())
                            .order(NamedValue.of("_count", SortOrder.Desc))
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a date histogram aggregation");
        }

        @Test
        @DisplayName("calendar date, hard bounded")
        void testCalendarDateHardBoundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("date_histogram")
                    .field("field")
                    .intervalType(IntervalType.CALENDAR_DATE)
                    .interval("Year")
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

            final var expected = new Aggregation.Builder()
                    .dateHistogram(new DateHistogramAggregation.Builder()
                            .field("field")
                            .calendarInterval(CalendarInterval.Year)
                            .offset(new Time.Builder()
                                    .time("1d")
                                    .build())
                            .hardBounds(new ExtendedBounds.Builder<FieldDateMath>()
                                    .min(new FieldDateMath.Builder()
                                            .value(170000000000d)
                                            .build())
                                    .max(new FieldDateMath.Builder()
                                            .value(180000000000d)
                                            .build())
                                    .build())
                            .order(NamedValue.of("_count", SortOrder.Desc))
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a date histogram aggregation");
        }

        @Test
        @DisplayName("calendar date, partially soft bounded")
        void testCalendarDatePartiallySoftBoundedAggregator() {
            final var aggregator = HistogramAggregator.builder()
                    .name("date_histogram")
                    .field("field")
                    .intervalType(IntervalType.CALENDAR_DATE)
                    .interval("Quarter")
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

            final var expected = new Aggregation.Builder()
                    .dateHistogram(new DateHistogramAggregation.Builder()
                            .field("field")
                            .calendarInterval(CalendarInterval.Quarter)
                            .offset(new Time.Builder()
                                    .time("1d")
                                    .build())
                            .extendedBounds(new ExtendedBounds.Builder<FieldDateMath>()
                                    .min(new FieldDateMath.Builder()
                                            .value(170000000000d)
                                            .build())
                                    .build())
                            .order(NamedValue.of("_count", SortOrder.Desc))
                            .build())
                    .aggregations(Map.of("keys", new Aggregation.Builder()
                            .terms(new TermsAggregation.Builder()
                                    .field("other_field")
                                    .order(NamedValue.of("_key", SortOrder.Asc))
                                    .build())
                            .build()))
                    .build();

            final var aggregation = aggregatorTransformer.transform(aggregator);

            assertEquals(expected.toString(), aggregation.toString(), "must return a date histogram aggregation");
        }
    }
}
