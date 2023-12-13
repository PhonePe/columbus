package com.phonepe.platform.columbus.internal.search.filter;

import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.platform.columbus.search.filter.general.EqualsFilter;
import com.phonepe.platform.columbus.search.filter.general.InFilter;
import com.phonepe.platform.columbus.search.filter.predicate.AndFilter;
import com.phonepe.platform.columbus.search.filter.predicate.NotFilter;
import com.phonepe.platform.columbus.search.filter.predicate.OrFilter;
import com.phonepe.platform.columbus.search.filter.range.BetweenFilter;
import com.phonepe.platform.columbus.search.filter.range.GreaterThanEqualsFilter;
import com.phonepe.platform.columbus.search.filter.range.GreaterThanFilter;
import com.phonepe.platform.columbus.search.filter.range.LessThanEqualsFilter;
import com.phonepe.platform.columbus.search.filter.range.LessThanFilter;
import com.phonepe.platform.columbus.search.query.QueryTransformer;
import com.phonepe.platform.columbus.test.annotations.WithElasticToolkit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithElasticToolkit.All
class FilterToQueryTest {

    private static QueryTransformer<QueryBuilder, Object> queryTransformer;

    @Test
    @DisplayName("Equals")
    void testEqualsFilter() {
        final var filter = EqualsFilter.builder()
                .field("field")
                .value(10)
                .build();

        final var expected = QueryBuilders.termQuery("field", 10);

        final var query = queryTransformer.transform(filter);

        assertEquals(expected, query, "must return a term query");
    }

    @Test
    @DisplayName("In")
    void testInFilter() {
        final var filter = InFilter.builder()
                .field("field")
                .value(10)
                .value(20)
                .build();

        final var expected = QueryBuilders.termsQuery("field", List.of(10, 20));

        final var query = queryTransformer.transform(filter);

        assertEquals(expected, query, "must return a terms query");
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

        final var expected = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("field1", 10))
                .filter(QueryBuilders.termQuery("field2", 20));

        final var query = queryTransformer.transform(filter);

        assertEquals(expected, query, "must return a bool query");
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

        final var expected = QueryBuilders.boolQuery()
                .should(QueryBuilders.termQuery("field1", 10))
                .should(QueryBuilders.termQuery("field2", 20));

        final var query = queryTransformer.transform(filter);

        assertEquals(expected, query, "must return a bool query");
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

        final var expected = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery("field1", 10));

        final var query = queryTransformer.transform(filter);

        assertEquals(expected, query, "must return a bool query");
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

        final var expected = QueryBuilders.rangeQuery("field")
                .from(10)
                .to(20)
                .includeUpper(false);

        final var query = queryTransformer.transform(filter);

        assertEquals(expected, query, "must return a range query");
    }

    @Test
    @DisplayName("Greater Than")
    void testGreaterThanFilter() {
        final var filter = GreaterThanFilter.builder()
                .field("field")
                .value(10)
                .build();

        final var expected = QueryBuilders.rangeQuery("field")
                .from(10)
                .includeLower(false);

        final var query = queryTransformer.transform(filter);

        assertEquals(expected, query, "must return a range query");
    }

    @Test
    @DisplayName("Greater Than Equals")
    void testGreaterThanEqualsFilter() {
        final var filter = GreaterThanEqualsFilter.builder()
                .field("field")
                .value(10)
                .build();

        final var expected = QueryBuilders.rangeQuery("field")
                .from(10);

        final var query = queryTransformer.transform(filter);

        assertEquals(expected, query, "must return a range query");
    }

    @Test
    @DisplayName("Less Than")
    void testLessThanFilter() {
        final var filter = LessThanFilter.builder()
                .field("field")
                .value(10)
                .build();

        final var expected = QueryBuilders.rangeQuery("field")
                .to(10)
                .includeUpper(false);

        final var query = queryTransformer.transform(filter);

        assertEquals(expected, query, "must return a range query");
    }

    @Test
    @DisplayName("Less Than Equals")
    void testLessThanEqualsFilter() {
        final var filter = LessThanEqualsFilter.builder()
                .field("field")
                .value(10)
                .build();

        final var expected = QueryBuilders.rangeQuery("field")
                .to(10);

        final var query = queryTransformer.transform(filter);

        assertEquals(expected, query, "must return a range query");
    }

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        queryTransformer = toolkit.queryTransformer();
    }
}
