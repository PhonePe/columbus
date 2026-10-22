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
package com.phonepe.columbus.elastic9.internal.search.aggregation;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.CardinalityAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramBucket;
import co.elastic.clients.elasticsearch._types.aggregations.FilterAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersBucket;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import co.elastic.clients.elasticsearch._types.aggregations.RangeAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.RangeBucket;
import co.elastic.clients.elasticsearch._types.aggregations.SamplerAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StatsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.FilterAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.FiltersAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.HistogramAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.RangeAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.SamplerAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.TermsAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.metrics.CardinalityAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.metrics.StatsAggregationParser;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.columbus.search.aggregation.bucket.FilterAggregation;
import com.phonepe.columbus.search.aggregation.bucket.FiltersAggregation;
import com.phonepe.columbus.search.aggregation.bucket.HistogramAggregation;
import com.phonepe.columbus.search.aggregation.bucket.RangeAggregation;
import com.phonepe.columbus.search.aggregation.bucket.SamplerAggregation;
import com.phonepe.columbus.search.aggregation.bucket.TermsAggregation;
import com.phonepe.columbus.search.aggregation.metrics.CardinalityAggregation;
import com.phonepe.columbus.search.aggregation.metrics.StatsAggregation;
import com.phonepe.columbus.search.aggregator.bucket.FilterAggregator;
import com.phonepe.columbus.search.aggregator.bucket.FiltersAggregator;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator.IntervalType;
import com.phonepe.columbus.search.aggregator.bucket.RangeAggregator;
import com.phonepe.columbus.search.aggregator.bucket.SamplerAggregator;
import com.phonepe.columbus.search.aggregator.bucket.TermsAggregator;
import com.phonepe.columbus.search.aggregator.metrics.CardinalityAggregator;
import com.phonepe.columbus.search.aggregator.metrics.StatsAggregator;
import com.phonepe.columbus.search.misc.bucket.NamedRange;
import com.phonepe.columbus.search.query.simple.MatchAllQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@WithElasticToolkit.All
class ElasticAggregationParserResolverTest {

    private static AggregationParserResolver<Aggregate, Object> aggregationParserResolver;

    @Test
    @DisplayName("Terms")
    void testTermsAggregation() {
        final var aggregator = TermsAggregator.builder()
                .name("count")
                .field("field")
                .subAggregator(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();

        final var subTerms = Aggregate.of(a -> a.sterms(StringTermsAggregate.of(st -> st
                .buckets(Buckets.of(b -> b.array(List.of(
                        StringTermsBucket.of(sb -> sb.key(k -> k.stringValue("key_1_1")).docCount(53L))
                ))))
                .sumOtherDocCount(0L)
                .docCountErrorUpperBound(0L))));

        final var result = Aggregate.of(a -> a.sterms(StringTermsAggregate.of(st -> st
                .buckets(Buckets.of(b -> b.array(List.of(
                        StringTermsBucket.of(sb -> sb
                                .key(k -> k.stringValue("key_1"))
                                .docCount(1000L)
                                .docCountErrorUpperBound(0L)
                                .aggregations("keys", subTerms)),
                        StringTermsBucket.of(sb -> sb
                                .key(k -> k.stringValue("key_2"))
                                .docCount(100L)
                                .docCountErrorUpperBound(0L)
                                .aggregations(
                                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st2 -> st2
                                                .buckets(Buckets.of(b2 -> b2.array(List.of(
                                                        StringTermsBucket.of(
                                                                sb2 -> sb2.key(k -> k.stringValue("key_2_1"))
                                                                        .docCount(15L))
                                                ))))
                                                .sumOtherDocCount(0L)
                                                .docCountErrorUpperBound(0L)))))),
                        StringTermsBucket.of(sb -> sb
                                .key(k -> k.stringValue("key_3"))
                                .docCount(10L)
                                .docCountErrorUpperBound(0L)
                                .aggregations(
                                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st2 -> st2
                                                .buckets(Buckets.of(b2 -> b2.array(List.of(
                                                        StringTermsBucket.of(
                                                                sb2 -> sb2.key(k -> k.stringValue("key_3_1"))
                                                                        .docCount(5L))
                                                ))))
                                                .sumOtherDocCount(0L)
                                                .docCountErrorUpperBound(0L))))))
                ))))
                .docCountErrorUpperBound(0L)
                .sumOtherDocCount(10L))));

        final var expected = TermsAggregation.builder()
                .name("count")
                .bucket(TermsAggregation.Bucket.builder()
                        .key("key_1")
                        .count(1000)
                        .countError(0)
                        .aggregation(
                                "keys", TermsAggregation.builder()
                                        .name("keys")
                                        .bucket(TermsAggregation.Bucket.builder()
                                                .key("key_1_1")
                                                .count(53)
                                                .build())
                                        .build())
                        .build())
                .bucket(TermsAggregation.Bucket.builder()
                        .key("key_2")
                        .count(100)
                        .countError(0)
                        .aggregation(
                                "keys", TermsAggregation.builder()
                                        .name("keys")
                                        .bucket(TermsAggregation.Bucket.builder()
                                                .key("key_2_1")
                                                .count(15)
                                                .build())
                                        .build())
                        .build())
                .bucket(TermsAggregation.Bucket.builder()
                        .key("key_3")
                        .count(10)
                        .countError(0)
                        .aggregation(
                                "keys", TermsAggregation.builder()
                                        .name("keys")
                                        .bucket(TermsAggregation.Bucket.builder()
                                                .key("key_3_1")
                                                .count(5)
                                                .build())
                                        .build())
                        .build())
                .countError(0)
                .otherDocCount(10)
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);
        final var aggregation = aggregationParser.parse(result, "count");

        assertInstanceOf(TermsAggregationParser.class, aggregationParser, "must return parser for terms");
        assertEquals(TermsAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a terms aggregation");
    }

    @Test
    @DisplayName("Range")
    void testRangeAggregation() {
        final var aggregator = RangeAggregator.builder()
                .name("range")
                .field("field")
                .range(NamedRange.builder().name("low").to(100d).build())
                .range(NamedRange.builder().name("high").from(100d).build())
                .subAggregator(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();

        final var result = Aggregate.of(a -> a.range(RangeAggregate.rangeAggregateOf(ra -> ra
                .buckets(Buckets.of(b -> b.array(List.of(
                        RangeBucket.of(rb -> rb
                                .key("low")
                                .from(Double.NEGATIVE_INFINITY)
                                .to(100.0)
                                .docCount(100L)
                                .aggregations(
                                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st -> st
                                                .buckets(Buckets.of(b2 -> b2.array(List.of(
                                                        StringTermsBucket.of(
                                                                sb -> sb.key(k -> k.stringValue("low_1")).docCount(15L))
                                                ))))
                                                .sumOtherDocCount(0L)
                                                .docCountErrorUpperBound(0L)))))),
                        RangeBucket.of(rb -> rb
                                .key("high")
                                .from(100.0)
                                .to(Double.POSITIVE_INFINITY)
                                .docCount(10L)
                                .aggregations(
                                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st -> st
                                                .buckets(Buckets.of(b2 -> b2.array(List.of(
                                                        StringTermsBucket.of(
                                                                sb -> sb.key(k -> k.stringValue("high_1")).docCount(5L))
                                                ))))
                                                .sumOtherDocCount(0L)
                                                .docCountErrorUpperBound(0L))))))
                )))))));

        final var expected = RangeAggregation.builder()
                .name("range")
                .bucket(
                        "low", RangeAggregation.Bucket.builder()
                                .from(Double.NEGATIVE_INFINITY)
                                .to(100d)
                                .count(100)
                                .aggregation(
                                        "keys", TermsAggregation.builder()
                                                .name("keys")
                                                .bucket(TermsAggregation.Bucket.builder()
                                                        .key("low_1")
                                                        .count(15)
                                                        .build())
                                                .build())
                                .build())
                .bucket(
                        "high", RangeAggregation.Bucket.builder()
                                .from(100d)
                                .to(Double.POSITIVE_INFINITY)
                                .count(10)
                                .aggregation(
                                        "keys", TermsAggregation.builder()
                                                .name("keys")
                                                .bucket(TermsAggregation.Bucket.builder()
                                                        .key("high_1")
                                                        .count(5)
                                                        .build())
                                                .build())
                                .build())
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);
        final var aggregation = aggregationParser.parse(result, "range");

        assertInstanceOf(RangeAggregationParser.class, aggregationParser, "must return parser for range");
        assertEquals(RangeAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a range aggregation");
    }

    @Test
    @DisplayName("Filter")
    void testFilterAggregation() {
        final var aggregator = FilterAggregator.builder()
                .name("filter")
                .filter(MatchAllQuery.builder().build())
                .subAggregator(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();

        final var result = Aggregate.of(a -> a.filter(FilterAggregate.of(fa -> fa
                .docCount(50L)
                .aggregations(
                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st -> st
                                .buckets(Buckets.of(b -> b.array(List.of(
                                        StringTermsBucket.of(sb -> sb.key(k -> k.stringValue("filtered")).docCount(15L))
                                ))))
                                .sumOtherDocCount(0L)
                                .docCountErrorUpperBound(0L))))))));

        final var expected = FilterAggregation.builder()
                .name("filter")
                .count(50)
                .aggregation(
                        "keys", TermsAggregation.builder()
                                .name("keys")
                                .bucket(TermsAggregation.Bucket.builder()
                                        .key("filtered")
                                        .count(15)
                                        .build())
                                .build())
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);
        final var aggregation = aggregationParser.parse(result, "filter");

        assertInstanceOf(FilterAggregationParser.class, aggregationParser, "must return parser for filter");
        assertEquals(FilterAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a filter aggregation");
    }

    @Test
    @DisplayName("Filters")
    void testFiltersAggregation() {
        final var aggregator = FiltersAggregator.builder()
                .name("filters")
                .filter("all", MatchAllQuery.builder().build())
                .subAggregator(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();

        final var result = Aggregate.of(a -> a.filters(FiltersAggregate.of(fa -> fa
                .buckets(Buckets.of(b -> b.keyed(Map.of(
                        "all", FiltersBucket.of(fb -> fb
                                .key("all")
                                .docCount(100L)
                                .aggregations(
                                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st -> st
                                                .buckets(Buckets.of(b2 -> b2.array(List.of(
                                                        StringTermsBucket.of(
                                                                sb -> sb.key(k -> k.stringValue("filtered"))
                                                                        .docCount(15L))
                                                ))))
                                                .sumOtherDocCount(0L)
                                                .docCountErrorUpperBound(0L))))))
                )))))));

        final var expected = FiltersAggregation.builder()
                .name("filters")
                .bucket(
                        "all", FiltersAggregation.Bucket.builder()
                                .count(100)
                                .aggregation(
                                        "keys", TermsAggregation.builder()
                                                .name("keys")
                                                .bucket(TermsAggregation.Bucket.builder()
                                                        .key("filtered")
                                                        .count(15)
                                                        .build())
                                                .build())
                                .build())
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);
        final var aggregation = aggregationParser.parse(result, "filters");

        assertInstanceOf(FiltersAggregationParser.class, aggregationParser, "must return parser for filters");
        assertEquals(FiltersAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a filters aggregation");
    }

    @Test
    @DisplayName("Histogram")
    void testHistogramAggregation() {
        final var aggregator = HistogramAggregator.builder()
                .name("histogram")
                .field("field")
                .intervalType(IntervalType.NUMERIC)
                .interval("10")
                .minValue(10)
                .subAggregator(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();

        final var result = Aggregate.of(a -> a.histogram(HistogramAggregate.of(ha -> ha
                .buckets(Buckets.of(b -> b.array(List.of(
                        HistogramBucket.of(hb -> hb
                                .key(10d)
                                .keyAsString("10.0")
                                .docCount(100L)
                                .aggregations(
                                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st -> st
                                                .buckets(Buckets.of(b2 -> b2.array(List.of(
                                                        StringTermsBucket.of(
                                                                sb -> sb.key(k -> k.stringValue("10_1")).docCount(15L))
                                                ))))
                                                .sumOtherDocCount(0L)
                                                .docCountErrorUpperBound(0L)))))),
                        HistogramBucket.of(hb -> hb
                                .key(20d)
                                .keyAsString("20.0")
                                .docCount(10L)
                                .aggregations(
                                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st -> st
                                                .buckets(Buckets.of(b2 -> b2.array(List.of(
                                                        StringTermsBucket.of(
                                                                sb -> sb.key(k -> k.stringValue("20_1")).docCount(5L))
                                                ))))
                                                .sumOtherDocCount(0L)
                                                .docCountErrorUpperBound(0L))))))
                )))))));

        final var expected = HistogramAggregation.builder()
                .name("histogram")
                .bucket(
                        "10.0", HistogramAggregation.Bucket.builder()
                                .key(10d)
                                .count(100)
                                .aggregation(
                                        "keys", TermsAggregation.builder()
                                                .name("keys")
                                                .bucket(TermsAggregation.Bucket.builder()
                                                        .key("10_1")
                                                        .count(15)
                                                        .build())
                                                .build())
                                .build())
                .bucket(
                        "20.0", HistogramAggregation.Bucket.builder()
                                .key(20d)
                                .count(10)
                                .aggregation(
                                        "keys", TermsAggregation.builder()
                                                .name("keys")
                                                .bucket(TermsAggregation.Bucket.builder()
                                                        .key("20_1")
                                                        .count(5)
                                                        .build())
                                                .build())
                                .build())
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);
        final var aggregation = aggregationParser.parse(result, "histogram");

        assertInstanceOf(HistogramAggregationParser.class, aggregationParser, "must return parser for histogram");
        assertEquals(HistogramAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a histogram aggregation");
    }

    @Test
    @DisplayName("FixedDateHistogram")
    void testFixedDateHistogramAggregation() {
        final var aggregator = HistogramAggregator.builder()
                .name("date_histogram")
                .field("field")
                .intervalType(IntervalType.FIXED_DATE)
                .interval("100s")
                .minValue(1700000000000L)
                .subAggregator(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();

        final var result = Aggregate.of(a -> a.dateHistogram(DateHistogramAggregate.of(dha -> dha
                .buckets(Buckets.of(b -> b.array(List.of(
                        DateHistogramBucket.of(dhb -> dhb
                                .key(1700000000000L)
                                .keyAsString("1700000000000")
                                .docCount(100L)
                                .aggregations(
                                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st -> st
                                                .buckets(Buckets.of(b2 -> b2.array(List.of(
                                                        StringTermsBucket.of(
                                                                sb -> sb.key(k -> k.stringValue("1_1")).docCount(15L))
                                                ))))
                                                .sumOtherDocCount(0L)
                                                .docCountErrorUpperBound(0L)))))),
                        DateHistogramBucket.of(dhb -> dhb
                                .key(1700000100000L)
                                .keyAsString("1700000100000")
                                .docCount(10L)
                                .aggregations(
                                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st -> st
                                                .buckets(Buckets.of(b2 -> b2.array(List.of(
                                                        StringTermsBucket.of(
                                                                sb -> sb.key(k -> k.stringValue("2_1")).docCount(5L))
                                                ))))
                                                .sumOtherDocCount(0L)
                                                .docCountErrorUpperBound(0L))))))
                )))))));

        final var expected = HistogramAggregation.builder()
                .name("date_histogram")
                .bucket(
                        "1700000000000", HistogramAggregation.Bucket.builder()
                                .key(1700000000000L)
                                .count(100)
                                .aggregation(
                                        "keys", TermsAggregation.builder()
                                                .name("keys")
                                                .bucket(TermsAggregation.Bucket.builder()
                                                        .key("1_1")
                                                        .count(15)
                                                        .build())
                                                .build())
                                .build())
                .bucket(
                        "1700000100000", HistogramAggregation.Bucket.builder()
                                .key(1700000100000L)
                                .count(10)
                                .aggregation(
                                        "keys", TermsAggregation.builder()
                                                .name("keys")
                                                .bucket(TermsAggregation.Bucket.builder()
                                                        .key("2_1")
                                                        .count(5)
                                                        .build())
                                                .build())
                                .build())
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);
        final var aggregation = aggregationParser.parse(result, "date_histogram");

        assertInstanceOf(HistogramAggregationParser.class, aggregationParser, "must return parser for histogram");
        assertEquals(HistogramAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a histogram aggregation");
    }

    @Test
    @DisplayName("Sampler")
    void testSamplerAggregation() {
        final var aggregator = SamplerAggregator.builder()
                .name("sampler")
                .shardSize(1)
                .subAggregator(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();

        final var result = Aggregate.of(a -> a.sampler(SamplerAggregate.of(sa -> sa
                .docCount(2L)
                .aggregations(
                        "keys", Aggregate.of(a2 -> a2.sterms(StringTermsAggregate.of(st -> st
                                .buckets(Buckets.of(b -> b.array(List.of(
                                        StringTermsBucket.of(sb -> sb.key(k -> k.stringValue("filtered")).docCount(2L))
                                ))))
                                .sumOtherDocCount(0L)
                                .docCountErrorUpperBound(0L))))))));

        final var expected = SamplerAggregation.builder()
                .name("sampler")
                .count(2)
                .aggregation(
                        "keys", TermsAggregation.builder()
                                .name("keys")
                                .bucket(TermsAggregation.Bucket.builder()
                                        .key("filtered")
                                        .count(2)
                                        .build())
                                .build())
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);
        final var aggregation = aggregationParser.parse(result, "sampler");

        assertInstanceOf(SamplerAggregationParser.class, aggregationParser, "must return parser for sampler");
        assertEquals(SamplerAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a sampler aggregation");
    }

    @Test
    @DisplayName("Stats")
    void testStatsAggregation() {
        final var aggregator = StatsAggregator.builder()
                .name("stats")
                .field("field")
                .build();

        final var result = Aggregate.of(a -> a.stats(StatsAggregate.statsAggregateOf(sa -> sa
                .count(100L)
                .min(0d)
                .max(100d)
                .sum(5000d)
                .avg(50d))));

        final var expected = StatsAggregation.builder()
                .name("stats")
                .count(100)
                .minimum(0)
                .maximum(100)
                .sum(5000)
                .average(50)
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);
        final var aggregation = aggregationParser.parse(result, "stats");

        assertInstanceOf(StatsAggregationParser.class, aggregationParser, "must return parser for stats");
        assertEquals(StatsAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a stats aggregation");
    }

    @Test
    @DisplayName("Cardinality")
    void testCardinalityAggregation() {
        final var aggregator = CardinalityAggregator.builder()
                .name("cardinality")
                .field("field")
                .build();

        final var result = Aggregate.of(a -> a.cardinality(CardinalityAggregate.of(ca -> ca
                .value(100L))));

        final var expected = CardinalityAggregation.builder()
                .name("cardinality")
                .count(100)
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);
        final var aggregation = aggregationParser.parse(result, "cardinality");

        assertInstanceOf(CardinalityAggregationParser.class, aggregationParser, "must return parser for cardinality");
        assertEquals(CardinalityAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a cardinality aggregation");
    }

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        aggregationParserResolver = toolkit.aggregationParserResolver();
    }
}
