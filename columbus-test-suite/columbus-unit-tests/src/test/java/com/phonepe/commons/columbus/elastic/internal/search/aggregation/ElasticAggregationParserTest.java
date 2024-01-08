package com.phonepe.commons.columbus.elastic.internal.search.aggregation;

import com.phonepe.commons.columbus.core.toolkit.Toolkit;
import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.bucket.FilterAggregationParser;
import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.bucket.FiltersAggregationParser;
import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.bucket.RangeAggregationParser;
import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.bucket.TermsAggregationParser;
import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.metrics.StatsAggregationParser;
import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.metrics.TopHitsAggregationParser;
import com.phonepe.commons.columbus.test.annotations.WithToolkit;
import com.phonepe.commons.columbus.type.Types;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@WithToolkit.All
class ElasticAggregationParserTest {

    @Test
    @DisplayName("invalid input to terms parser")
    void testParseInvalidTerms() {
        final var parser = TermsAggregationParser.builder().build();
        final var result = ElasticRange.builder().build();
        assertThrows(IllegalArgumentException.class, () -> parser.parse(result),
                "must throw if input is not ParsedTerms");
    }

    @Test
    @DisplayName("invalid input to range parser")
    void testParseInvalidRange() {
        final var parser = RangeAggregationParser.builder().build();
        final var result = ElasticStringTerms.builder().build();
        assertThrows(IllegalArgumentException.class, () -> parser.parse(result),
                "must throw if input is not ParsedRange");
    }

    @Test
    @DisplayName("invalid input to filter parser")
    void testParseInvalidFilter() {
        final var parser = FilterAggregationParser.builder().build();
        final var result = ElasticStringTerms.builder().build();
        assertThrows(IllegalArgumentException.class, () -> parser.parse(result),
                "must throw if input is not ParsedFilter");
    }

    @Test
    @DisplayName("invalid input to filters parser")
    void testParseInvalidFilters() {
        final var parser = FiltersAggregationParser.builder().build();
        final var result = ElasticStringTerms.builder().build();
        assertThrows(IllegalArgumentException.class, () -> parser.parse(result),
                "must throw if input is not ParsedFilters");
    }

    @Test
    @DisplayName("invalid input to stats parser")
    void testParseInvalidStats() {
        final var parser = new StatsAggregationParser();
        final var result = ElasticStringTerms.builder().build();
        assertThrows(IllegalArgumentException.class, () -> parser.parse(result),
                "must throw if input is not ParsedStats");
    }

    @Test
    @DisplayName("invalid input to top hits parser")
    void testParseInvalidTopHits(final Toolkit toolkit) {
        final var parser = TopHitsAggregationParser.builder()
                .serDe(toolkit.serDe())
                .entityType(Types.forClass(Object.class))
                .build();
        final var result = ElasticStringTerms.builder().build();
        assertThrows(IllegalArgumentException.class, () -> parser.parse(result),
                "must throw if input is not ParsedTopHits");
    }
}
