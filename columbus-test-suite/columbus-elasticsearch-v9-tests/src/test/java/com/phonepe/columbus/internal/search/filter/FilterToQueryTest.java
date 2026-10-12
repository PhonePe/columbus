package com.phonepe.columbus.internal.search.filter;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ConstantScoreQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.search.filter.general.EqualsFilter;
import com.phonepe.columbus.search.filter.general.InFilter;
import com.phonepe.columbus.search.filter.predicate.AndFilter;
import com.phonepe.columbus.search.filter.predicate.NotFilter;
import com.phonepe.columbus.search.filter.predicate.OrFilter;
import com.phonepe.columbus.search.filter.range.BetweenFilter;
import com.phonepe.columbus.search.filter.range.GreaterThanEqualsFilter;
import com.phonepe.columbus.search.filter.range.GreaterThanFilter;
import com.phonepe.columbus.search.filter.range.LessThanEqualsFilter;
import com.phonepe.columbus.search.filter.range.LessThanFilter;
import com.phonepe.columbus.search.filter.score.FixedScoreFilter;
import com.phonepe.columbus.search.filter.string.TextMatchFilter;
import com.phonepe.columbus.search.query.QueryTransformer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithElasticToolkit.All
class FilterToQueryTest {

    private static QueryTransformer<QueryVariant, Object> queryTransformer;

    @Test
    @DisplayName("Equals")
    void testEqualsFilter() {
        final var filter = EqualsFilter.builder()
                .field("field")
                .value(10)
                .build();

        final var expected = new TermQuery.Builder().field("field").value(10).build();

        final var query = queryTransformer.transform(filter);
        assertEquals(expected.toString(), query.toString(), "must return a term query");
    }

    @Test
    @DisplayName("In")
    void testInFilter() {
        final var filter = InFilter.builder()
                .field("field")
                .value(10)
                .value(20)
                .build();

        final var expected = new TermsQuery.Builder().field("field").terms(new TermsQueryField.Builder()
                        .value(List.of(FieldValue.of(10), FieldValue.of(20)))
                        .build())
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a terms query");
    }

    @Test
    @DisplayName("And")
    void testAndFilter() {
        final var filter = AndFilter.builder()
                .filter(EqualsFilter.builder()
                        .field("field1")
                        .value(10)
                        .build())
                .filter(EqualsFilter.builder()
                        .field("field2")
                        .value(20)
                        .build())
                .build();

        final var expected = new BoolQuery.Builder().must(
                        new TermQuery.Builder()
                                .field("field1")
                                .value(10)
                                .build(),
                        new TermQuery.Builder()
                                .field("field2")
                                .value(20)
                                .build())
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a bool query");
    }

    @Test
    @DisplayName("Or")
    void testOrFilter() {
        final var filter = OrFilter.builder()
                .filter(EqualsFilter.builder()
                        .field("field1")
                        .value(10)
                        .build())
                .filter(EqualsFilter.builder()
                        .field("field2")
                        .value(20)
                        .build())
                .build();

        final var expected = new BoolQuery.Builder().should(
                        new TermQuery.Builder()
                                .field("field1")
                                .value(10)
                                .build(),
                        new TermQuery.Builder()
                                .field("field2")
                                .value(20)
                                .build())
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a bool query");
    }

    @Test
    @DisplayName("Not")
    void testNotFilter() {
        final var filter = NotFilter.builder()
                .filter(EqualsFilter.builder()
                        .field("field1")
                        .value(10)
                        .build())
                .build();

        final var expected = new BoolQuery.Builder()
                .mustNot(new TermQuery.Builder()
                        .field("field1")
                        .value(10)
                        .build())
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a bool query");
    }

    @Test
    @DisplayName("Between")
    void testBetweenFilter() {
        final var filter = BetweenFilter.builder()
                .field("field")
                .from(10)
                .to(20)
                .includeUpper(false)
                .build();

        final var expected = new RangeQuery.Builder()
                .number(new NumberRangeQuery.Builder()
                        .field("field")
                        .gte(10d)
                        .lt(20d)
                        .build())
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a range query");
    }

    @Test
    @DisplayName("Greater Than")
    void testGreaterThanFilter() {
        final var filter = GreaterThanFilter.builder()
                .field("field")
                .value(10)
                .build();

        final var expected = new RangeQuery.Builder()
                .number(new NumberRangeQuery.Builder()
                        .field("field")
                        .gt(10d)
                        .build())
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a range query");
    }

    @Test
    @DisplayName("Greater Than Equals")
    void testGreaterThanEqualsFilter() {
        final var filter = GreaterThanEqualsFilter.builder()
                .field("field")
                .value(10)
                .build();

        final var expected = new RangeQuery.Builder()
                .number(new NumberRangeQuery.Builder()
                        .field("field")
                        .gte(10d)
                        .build())
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a range query");
    }

    @Test
    @DisplayName("Less Than")
    void testLessThanFilter() {
        final var filter = LessThanFilter.builder()
                .field("field")
                .value(10)
                .build();

        final var expected = new RangeQuery.Builder()
                .number(new NumberRangeQuery.Builder()
                        .field("field")
                        .lt(10d)
                        .build())
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a range query");
    }

    @Test
    @DisplayName("Less Than Equals")
    void testLessThanEqualsFilter() {
        final var filter = LessThanEqualsFilter.builder()
                .field("field")
                .value(10)
                .build();

        final var expected = new RangeQuery.Builder()
                .number(new NumberRangeQuery.Builder()
                        .field("field")
                        .lte(10d)
                        .build())
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a range query");
    }

    @Test
    @DisplayName("Fixed Score")
    void testFixedScoreFilter() {
        final var filter = FixedScoreFilter.builder()
                .filter(EqualsFilter.builder()
                        .field("field")
                        .value(10)
                        .build())
                .boost(2f)
                .build();

        final var expected = new ConstantScoreQuery.Builder()
                .filter(new TermQuery.Builder()
                        .field("field")
                        .value(10)
                        .build())
                .boost(2f)
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a fixed score query");
    }

    @Test
    @DisplayName("Text Match")
    void testTextMatchFilter() {
        final var filter = TextMatchFilter.builder()
                .field("field")
                .value("text")
                .build();

        final var expected = new MatchQuery.Builder()
                .field("field")
                .query("text")
                .build();

        final var query = queryTransformer.transform(filter);

        assertEquals(expected.toString(), query.toString(), "must return a match query");
    }

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        queryTransformer = toolkit.queryTransformer();
    }
}
