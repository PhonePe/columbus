package com.phonepe.platform.columbus.elastic.internal.search.aggregation;

import com.phonepe.platform.columbus.dao.id.Ids;
import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.bucket.FilterAggregationParser;
import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.bucket.FiltersAggregationParser;
import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.bucket.RangeAggregationParser;
import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.bucket.TermsAggregationParser;
import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.metrics.StatsAggregationParser;
import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.metrics.TopHitsAggregationParser;
import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.platform.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.platform.columbus.search.aggregation.bucket.FilterAggregation;
import com.phonepe.platform.columbus.search.aggregation.bucket.FiltersAggregation;
import com.phonepe.platform.columbus.search.aggregation.bucket.RangeAggregation;
import com.phonepe.platform.columbus.search.aggregation.bucket.TermsAggregation;
import com.phonepe.platform.columbus.search.aggregation.metrics.StatsAggregation;
import com.phonepe.platform.columbus.search.aggregation.metrics.TopHitsAggregation;
import com.phonepe.platform.columbus.search.aggregator.bucket.FilterAggregator;
import com.phonepe.platform.columbus.search.aggregator.bucket.FiltersAggregator;
import com.phonepe.platform.columbus.search.aggregator.bucket.RangeAggregator;
import com.phonepe.platform.columbus.search.aggregator.bucket.TermsAggregator;
import com.phonepe.platform.columbus.search.aggregator.metrics.StatsAggregator;
import com.phonepe.platform.columbus.search.aggregator.metrics.TopHitsAggregator;
import com.phonepe.platform.columbus.search.misc.bucket.NamedRange;
import com.phonepe.platform.columbus.search.misc.result.Hit;
import com.phonepe.platform.columbus.search.misc.result.Result;
import com.phonepe.platform.columbus.search.misc.result.TotalCount;
import com.phonepe.platform.columbus.search.query.simple.MatchAllQuery;
import com.phonepe.platform.columbus.test.annotations.WithElasticToolkit;
import com.phonepe.platform.columbus.type.Types;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.TotalHits.Relation;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.search.DocValueFormat;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.SearchSortValues;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@WithElasticToolkit.All
class ElasticAggregationParserResolverTest {

    private static AggregationParserResolver<Aggregation> aggregationParserResolver;

    @Test
    @DisplayName("Terms")
    void testTermsAggregation() {
        final var aggregator = TermsAggregator.builder()
                .name("count")
                .field("field")
                .subAggregation(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();

        final var result = ElasticStringTerms.builder()
                .name("count")
                .bucket(ElasticStringTerms.ElasticBucket.builder()
                        .key("key_1")
                        .docCount(1000)
                        .docCountError(0)
                        .aggregations(new Aggregations(List.of(ElasticStringTerms.builder()
                                .name("keys")
                                .bucket(ElasticStringTerms.ElasticBucket.builder()
                                        .key("key_1_1")
                                        .docCount(53)
                                        .build())
                                .build())))
                        .build())
                .bucket(ElasticStringTerms.ElasticBucket.builder()
                        .key("key_2")
                        .docCount(100)
                        .docCountError(0)
                        .aggregations(new Aggregations(List.of(ElasticStringTerms.builder()
                                .name("keys")
                                .bucket(ElasticStringTerms.ElasticBucket.builder()
                                        .key("key_2_1")
                                        .docCount(15)
                                        .build())
                                .build())))
                        .build())
                .bucket(ElasticStringTerms.ElasticBucket.builder()
                        .key("key_3")
                        .docCount(10)
                        .docCountError(0)
                        .aggregations(new Aggregations(List.of(ElasticStringTerms.builder()
                                .name("keys")
                                .bucket(ElasticStringTerms.ElasticBucket.builder()
                                        .key("key_3_1")
                                        .docCount(5)
                                        .build())
                                .build())))
                        .build())
                .countError(0)
                .otherDocCounts(10)
                .build();

        final var expected = TermsAggregation.builder()
                .name("count")
                .bucket(TermsAggregation.Bucket.builder()
                        .key("key_1")
                        .count(1000)
                        .countError(0)
                        .aggregation("keys", TermsAggregation.builder()
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
                        .aggregation("keys", TermsAggregation.builder()
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
                        .aggregation("keys", TermsAggregation.builder()
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

        final var aggregation = aggregationParser.parse(result);

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
                .range(NamedRange.builder()
                        .name("low")
                        .to(100d)
                        .build())
                .range(NamedRange.builder()
                        .name("high")
                        .from(100d)
                        .build())
                .subAggregation(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();
        final var result = ElasticRange.builder()
                .name("range")
                .bucket(ElasticRange.ElasticBucket.builder()
                        .key("low")
                        .to(100d)
                        .docCount(100)
                        .aggregations(new Aggregations(List.of(ElasticStringTerms.builder()
                                .name("keys")
                                .bucket(ElasticStringTerms.ElasticBucket.builder()
                                        .key("low_1")
                                        .docCount(15)
                                        .build())
                                .build())))
                        .build())
                .bucket(ElasticRange.ElasticBucket.builder()
                        .key("high")
                        .docCount(10)
                        .from(100d)
                        .aggregations(new Aggregations(List.of(ElasticStringTerms.builder()
                                .name("keys")
                                .bucket(ElasticStringTerms.ElasticBucket.builder()
                                        .key("high_1")
                                        .docCount(5)
                                        .build())
                                .build())))
                        .build())
                .build();

        final var expected = RangeAggregation.builder()
                .name("range")
                .bucket("low", RangeAggregation.Bucket.builder()
                        .from(Double.NEGATIVE_INFINITY)
                        .to(100d)
                        .count(100)
                        .aggregation("keys", TermsAggregation.builder()
                                .name("keys")
                                .bucket(TermsAggregation.Bucket.builder()
                                        .key("low_1")
                                        .count(15)
                                        .build())
                                .build())
                        .build())
                .bucket("high", RangeAggregation.Bucket.builder()
                        .from(100d)
                        .to(Double.POSITIVE_INFINITY)
                        .count(10)
                        .aggregation("keys", TermsAggregation.builder()
                                .name("keys")
                                .bucket(TermsAggregation.Bucket.builder()
                                        .key("high_1")
                                        .count(5)
                                        .build())
                                .build())
                        .build())
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);

        final var aggregation = aggregationParser.parse(result);

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
                .subAggregation(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();
        final var result = ElasticFilter.builder()
                .name("filter")
                .aggregations(new Aggregations(List.of(ElasticStringTerms.builder()
                        .name("keys")
                        .bucket(ElasticStringTerms.ElasticBucket.builder()
                                .key("filtered")
                                .docCount(15)
                                .build())
                        .build())))
                .build();

        final var expected = FilterAggregation.builder()
                .name("filter")
                .aggregation("keys", TermsAggregation.builder()
                        .name("keys")
                        .bucket(TermsAggregation.Bucket.builder()
                                .key("filtered")
                                .count(15)
                                .build())
                        .build())
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);

        final var aggregation = aggregationParser.parse(result);

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
                .subAggregation(TermsAggregator.builder()
                        .name("keys")
                        .field("other_field")
                        .build())
                .build();
        final var result = ElasticFilters.builder()
                .name("filters")
                .bucket(ElasticFilters.ElasticBucket.builder()
                        .key("all")
                        .docCount(100)
                        .aggregations(new Aggregations(List.of(ElasticStringTerms.builder()
                                .name("keys")
                                .bucket(ElasticStringTerms.ElasticBucket.builder()
                                        .key("filtered")
                                        .docCount(15)
                                        .build())
                                .build())))
                        .build())
                .build();

        final var expected = FiltersAggregation.builder()
                .name("filters")
                .bucket("all", FiltersAggregation.Bucket.builder()
                        .count(100)
                        .aggregation("keys", TermsAggregation.builder()
                                .name("keys")
                                .bucket(TermsAggregation.Bucket.builder()
                                        .key("filtered")
                                        .count(15)
                                        .build())
                                .build())
                        .build())
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);

        final var aggregation = aggregationParser.parse(result);

        assertInstanceOf(FiltersAggregationParser.class, aggregationParser, "must return parser for filters");
        assertEquals(FiltersAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a filters aggregation");
    }

    @Test
    @DisplayName("Top Hits")
    void testTopHitsAggregation() {
        final var aggregator = TopHitsAggregator.builder()
                .name("top_hits")
                .entityType(Types.forClass(Object.class))
                .build();
        final var hit = SearchHit.createFromMap(Map.of(
                "_id", "id",
                "_score", 10f,
                "_source", new BytesArray("{\"key\": \"value\"}"),
                "sort", new SearchSortValues(new Object[]{"sort"}, new DocValueFormat[]{DocValueFormat.RAW}),
                "matched_queries", List.of("query")
        ));
        final var result = ElasticTopHits.builder()
                .name("top_hits")
                .hits(new SearchHits(new SearchHit[]{hit}, new TotalHits(1, Relation.EQUAL_TO), 10))
                .build();

        final var expected = TopHitsAggregation.builder()
                .name("top_hits")
                .topHits(Result.builder()
                        .hit(Hit.builder()
                                .id(Ids.value("id"))
                                .entity(Map.of("key", "value"))
                                .score(10)
                                .sortValue("sort")
                                .matchedQuery("query")
                                .build())
                        .maxScore(10f)
                        .totalCount(TotalCount.exact(1))
                        .build())
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);

        final var aggregation = aggregationParser.parse(result);

        assertInstanceOf(TopHitsAggregationParser.class, aggregationParser, "must return parser for top hits");
        assertEquals(TopHitsAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a top hits aggregation");
    }

    @Test
    @DisplayName("Stats")
    void testStatsAggregation() {
        final var aggregator = StatsAggregator.builder()
                .name("stats")
                .field("field")
                .build();
        final var result = ElasticStats.builder()
                .name("stats")
                .count(100)
                .min(0)
                .max(100)
                .sum(5000)
                .avg(50)
                .build();

        final var expected = StatsAggregation.builder()
                .name("stats")
                .count(100)
                .minimum(0)
                .maximum(100)
                .sum(5000)
                .average(50)
                .build();

        final var aggregationParser = aggregationParserResolver.resolve(aggregator);

        final var aggregation = aggregationParser.parse(result);

        assertInstanceOf(StatsAggregationParser.class, aggregationParser, "must return parser for stats");
        assertEquals(StatsAggregator.TYPE, aggregation.getType(), "type must match that of aggregator");
        assertEquals(expected, aggregation, "must return a stats aggregation");
    }

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        aggregationParserResolver = toolkit.aggregationParserResolver();
    }
}
