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
import co.elastic.clients.elasticsearch._types.aggregations.RangeAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.FilterAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.FiltersAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.HistogramAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.RangeAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.SamplerAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.TermsAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.metrics.CardinalityAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.metrics.StatsAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.metrics.TopHitsAggregationParser;
import com.phonepe.columbus.test.annotations.WithToolkit;
import com.phonepe.columbus.type.Types;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@WithToolkit.All
class ElasticAggregationParserTest {

    private static final Aggregate STERMS_AGGREGATE = Aggregate.of(a -> a.sterms(StringTermsAggregate.of(st -> st
            .buckets(Buckets.of(b -> b.array(List.of(
                    StringTermsBucket.of(sb -> sb.key(k -> k.stringValue("k")).docCount(1L))
            ))))
            .sumOtherDocCount(0L)
            .docCountErrorUpperBound(0L))));

    private static final Aggregate RANGE_AGGREGATE = Aggregate.of(a -> a.range(RangeAggregate.rangeAggregateOf(ra -> ra
            .buckets(Buckets.of(b -> b.array(List.of()))))));

    @Test
    @DisplayName("invalid input to terms parser")
    void testParseInvalidTerms() {
        final var parser = TermsAggregationParser.builder().build();
        assertThrows(
                IllegalArgumentException.class, () -> parser.parse(RANGE_AGGREGATE, "test"),
                "must throw if input is not StringTermsAggregate");
    }

    @Test
    @DisplayName("invalid input to range parser")
    void testParseInvalidRange() {
        final var parser = RangeAggregationParser.builder().build();
        assertThrows(
                IllegalArgumentException.class, () -> parser.parse(STERMS_AGGREGATE, "test"),
                "must throw if input is not RangeAggregate");
    }

    @Test
    @DisplayName("invalid input to filter parser")
    void testParseInvalidFilter() {
        final var parser = FilterAggregationParser.builder().build();
        assertThrows(
                IllegalArgumentException.class, () -> parser.parse(STERMS_AGGREGATE, "test"),
                "must throw if input is not FilterAggregate");
    }

    @Test
    @DisplayName("invalid input to filters parser")
    void testParseInvalidFilters() {
        final var parser = FiltersAggregationParser.builder().build();
        assertThrows(
                IllegalArgumentException.class, () -> parser.parse(STERMS_AGGREGATE, "test"),
                "must throw if input is not FiltersAggregate");
    }

    @Test
    @DisplayName("invalid input to histogram parser")
    void testParseInvalidHistogram() {
        final var parser = HistogramAggregationParser.builder().build();
        assertThrows(
                IllegalArgumentException.class, () -> parser.parse(STERMS_AGGREGATE, "test"),
                "must throw if input is not HistogramAggregate");
    }

    @Test
    @DisplayName("invalid input to sampler parser")
    void testParseInvalidSampler() {
        final var parser = SamplerAggregationParser.builder().build();
        assertThrows(
                IllegalArgumentException.class, () -> parser.parse(STERMS_AGGREGATE, "test"),
                "must throw if input is not SamplerAggregate");
    }

    @Test
    @DisplayName("invalid input to stats parser")
    void testParseInvalidStats() {
        final var parser = new StatsAggregationParser();
        assertThrows(
                IllegalArgumentException.class, () -> parser.parse(STERMS_AGGREGATE, "test"),
                "must throw if input is not StatsAggregate");
    }

    @Test
    @DisplayName("invalid input to top hits parser")
    void testParseInvalidTopHits(final Toolkit toolkit) {
        final var parser = TopHitsAggregationParser.builder()
                .serDe(toolkit.serDe())
                .entityType(Types.forClass(Object.class))
                .build();
        assertThrows(
                IllegalArgumentException.class, () -> parser.parse(STERMS_AGGREGATE, "test"),
                "must throw if input is not TopHitsAggregate");
    }

    @Test
    @DisplayName("invalid input to cardinality parser")
    void testParseInvalidCardinality() {
        final var parser = new CardinalityAggregationParser();
        assertThrows(
                IllegalArgumentException.class, () -> parser.parse(STERMS_AGGREGATE, "test"),
                "must throw if input is not CardinalityAggregate");
    }
}
