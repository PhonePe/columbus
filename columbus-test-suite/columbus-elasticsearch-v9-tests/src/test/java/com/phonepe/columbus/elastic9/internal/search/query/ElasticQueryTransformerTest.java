package com.phonepe.columbus.elastic9.internal.search.query;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.GeoDistanceType;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.ExistsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoShapeFieldQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoShapeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch._types.query_dsl.ZeroTermsQuery;
import co.elastic.clients.json.JsonData;
import com.phonepe.columbus.dao.id.Ids;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.search.misc.geo.Location;
import com.phonepe.columbus.search.misc.script.Script;
import com.phonepe.columbus.search.misc.text.Analysis;
import com.phonepe.columbus.search.misc.text.Fuzziness;
import com.phonepe.columbus.search.misc.text.Terms;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.query.QueryTransformer;
import com.phonepe.columbus.search.query.compound.BoolQuery;
import com.phonepe.columbus.search.query.compound.BoostingQuery;
import com.phonepe.columbus.search.query.compound.ConstantScoreQuery;
import com.phonepe.columbus.search.query.compound.DisMaxQuery;
import com.phonepe.columbus.search.query.compound.FunctionScoreQuery;
import com.phonepe.columbus.search.query.geo.GeoDistanceQuery;
import com.phonepe.columbus.search.query.geo.GeoPointQuery;
import com.phonepe.columbus.search.query.simple.MatchAllQuery;
import com.phonepe.columbus.search.query.simple.MatchNoneQuery;
import com.phonepe.columbus.search.query.special.KnnQuery;
import com.phonepe.columbus.search.query.special.ScriptQuery;
import com.phonepe.columbus.search.query.term.FieldExistsQuery;
import com.phonepe.columbus.search.query.term.FuzzyQuery;
import com.phonepe.columbus.search.query.term.IdsQuery;
import com.phonepe.columbus.search.query.term.PrefixQuery;
import com.phonepe.columbus.search.query.term.RangeQuery;
import com.phonepe.columbus.search.query.term.TermQuery;
import com.phonepe.columbus.search.query.term.TermsQuery;
import com.phonepe.columbus.search.query.term.WildcardQuery;
import com.phonepe.columbus.search.query.text.MatchBoolPrefixQuery;
import com.phonepe.columbus.search.query.text.MatchPhrasePrefixQuery;
import com.phonepe.columbus.search.query.text.MatchPhraseQuery;
import com.phonepe.columbus.search.query.text.MatchQuery;
import com.phonepe.columbus.search.query.text.MultiMatchQuery;
import com.phonepe.columbus.search.score.WeightScore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WithElasticToolkit.All
class ElasticQueryTransformerTest {

    private static final Query ALL_QUERY = MatchAllQuery.builder()
            .boost(2f)
            .name("all")
            .build();
    private static final Query NONE_QUERY = MatchNoneQuery.builder()
            .boost(2f)
            .name("none")
            .build();

    private static final co.elastic.clients.elasticsearch._types.query_dsl.Query ALL_BUILDER =
            new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder()
                    .matchAll(new Builder()
                            .queryName("all")
                            .boost(2f)
                            .build())
                    .build();
    private static final co.elastic.clients.elasticsearch._types.query_dsl.Query NONE_BUILDER =
            new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder()
                    .matchNone(new co.elastic.clients.elasticsearch._types.query_dsl.MatchNoneQuery.Builder()
                            .boost(2f)
                            .queryName("none")
                            .build()).build();

    private static QueryTransformer<QueryVariant, Object> queryTransformer;

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        queryTransformer = toolkit.queryTransformer();
    }

    @Nested
    class Simple {

        @Test
        @DisplayName("Match All")
        void testMatchAllQuery() {
            final var query = queryTransformer.transform(ALL_QUERY);

            assertEquals(ALL_BUILDER._get().toString(), query.toString(), "must return a match all query");
        }

        @Test
        @DisplayName("Match None")
        void testMatchNoneQuery() {
            final var query = queryTransformer.transform(NONE_QUERY);

            assertEquals(NONE_BUILDER._get().toString(), query.toString(), "must return a match none query");
        }
    }

    @Nested
    class Term {

        @Test
        @DisplayName("Field Exists")
        void testFieldExistsQuery() {
            final var fieldExistsQuery = FieldExistsQuery.builder()
                    .field("field")
                    .boost(2f)
                    .name("exists")
                    .build();

            final var expected = new ExistsQuery.Builder()
                    .field("field")
                    .boost(2f)
                    .queryName("exists")
                    .build();

            final var query = queryTransformer.transform(fieldExistsQuery);

            assertEquals(expected.toString(), query.toString(), "must return an exists query");
        }

        @Test
        @DisplayName("Ids")
        void testIdsQuery() {
            final var idsQuery = IdsQuery.builder()
                    .id(Ids.value("id1"))
                    .id(Ids.value("id2"))
                    .boost(2f)
                    .name("ids")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery.Builder()
                    .boost(2f)
                    .queryName("ids")
                    .values("id1", "id2")
                    .build();

            final var query = queryTransformer.transform(idsQuery);

            assertEquals(expected.toString(), query.toString(), "must return an ids query");
        }

        @Test
        @DisplayName("Prefix")
        void testPrefixQuery() {
            final var prefixQuery = PrefixQuery.builder()
                    .field("field")
                    .value("pre")
                    .caseInsensitive(true)
                    .rewrite("rewrite")
                    .boost(2f)
                    .name("prefix")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.PrefixQuery.Builder()
                    .field("field")
                    .value("pre")
                    .caseInsensitive(true)
                    .rewrite("rewrite")
                    .boost(2f)
                    .queryName("prefix")
                    .build();

            final var query = queryTransformer.transform(prefixQuery);

            assertEquals(expected.toString(), query.toString(), "must return a prefix query");
        }

        @Test
        @DisplayName("Range")
        void testRangeQuery() {
            final var rangeQuery = RangeQuery.builder()
                    .field("field")
                    .from(10)
                    .to(20)
                    .boost(2f)
                    .name("range")
                    .build();

            final var expected =
                    new co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery.Builder()
                            .number(new co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery.Builder()
                                    .field("field")
                                    .gte(10d)
                                    .lte(20d)
                                    .boost(2f)
                                    .queryName("range")
                                    .build())
                            .build();

            final var query = queryTransformer.transform(rangeQuery);

            assertEquals(expected.toString(), query.toString(), "must return a range query");
        }

        @Test
        @DisplayName("Range - Date with timezone and format")
        void testDateRangeQuery() {
            final var rangeQuery = RangeQuery.builder()
                    .field("timestamp")
                    .from("2023-01-01")
                    .to("2023-12-31")
                    .timezone("+05:30")
                    .format("yyyy-MM-dd")
                    .relation("Within")
                    .boost(1f)
                    .name("date_range")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery.Builder()
                    .date(new co.elastic.clients.elasticsearch._types.query_dsl.DateRangeQuery.Builder()
                            .field("timestamp")
                            .gte("2023-01-01")
                            .lte("2023-12-31")
                            .timeZone("+05:30")
                            .format("yyyy-MM-dd")
                            .relation(co.elastic.clients.elasticsearch._types.query_dsl.RangeRelation.Within)
                            .boost(1f)
                            .queryName("date_range")
                            .build())
                    .build();

            final var query = queryTransformer.transform(rangeQuery);

            assertEquals(expected.toString(), query.toString(), "must return a date range query");
        }

        @Test
        @DisplayName("Range - Date with exclusive bounds")
        void testDateRangeQueryExclusiveBounds() {
            final var rangeQuery = RangeQuery.builder()
                    .field("timestamp")
                    .from("2023-01-01")
                    .to("2023-12-31")
                    .includeLower(false)
                    .includeUpper(false)
                    .timezone("UTC")
                    .boost(1f)
                    .name("date_range_exclusive")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery.Builder()
                    .date(new co.elastic.clients.elasticsearch._types.query_dsl.DateRangeQuery.Builder()
                            .field("timestamp")
                            .gt("2023-01-01")
                            .lt("2023-12-31")
                            .timeZone("UTC")
                            .boost(1f)
                            .queryName("date_range_exclusive")
                            .build())
                    .build();

            final var query = queryTransformer.transform(rangeQuery);

            assertEquals(expected.toString(), query.toString(), "must return a date range query with exclusive bounds");
        }

        @Test
        @DisplayName("Range - Term/String")
        void testTermRangeQuery() {
            final var rangeQuery = RangeQuery.builder()
                    .field("status")
                    .from("active")
                    .to("pending")
                    .includeLower(false)
                    .includeUpper(true)
                    .relation("Intersects")
                    .boost(1f)
                    .name("term_range")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery.Builder()
                    .term(new co.elastic.clients.elasticsearch._types.query_dsl.TermRangeQuery.Builder()
                            .field("status")
                            .gt("active")
                            .lte("pending")
                            .relation(co.elastic.clients.elasticsearch._types.query_dsl.RangeRelation.Intersects)
                            .boost(1f)
                            .queryName("term_range")
                            .build())
                    .build();

            final var query = queryTransformer.transform(rangeQuery);

            assertEquals(expected.toString(), query.toString(), "must return a term range query");
        }

        @Test
        @DisplayName("Range - Numeric with exclusive bounds and null from")
        void testNumericRangeQueryExclusiveWithNullFrom() {
            final var rangeQuery = RangeQuery.builder()
                    .field("price")
                    .to(100)
                    .includeLower(false)
                    .includeUpper(false)
                    .boost(1f)
                    .name("numeric_exclusive")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery.Builder()
                    .number(new co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery.Builder()
                            .field("price")
                            .lt(100d)
                            .boost(1f)
                            .queryName("numeric_exclusive")
                            .build())
                    .build();

            final var query = queryTransformer.transform(rangeQuery);

            assertEquals(expected.toString(), query.toString(), "must return numeric range with only upper bound");
        }

        @Test
        @DisplayName("Range - Term/String with null to")
        void testTermRangeQueryNullTo() {
            final var rangeQuery = RangeQuery.builder()
                    .field("status")
                    .from("active")
                    .boost(1f)
                    .name("term_range_no_to")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery.Builder()
                    .term(new co.elastic.clients.elasticsearch._types.query_dsl.TermRangeQuery.Builder()
                            .field("status")
                            .gte("active")
                            .boost(1f)
                            .queryName("term_range_no_to")
                            .build())
                    .build();

            final var query = queryTransformer.transform(rangeQuery);

            assertEquals(expected.toString(), query.toString(), "must return a term range query with null to");
        }

        @Test
        @DisplayName("Term")
        void testTermQuery() {
            final var termQuery = TermQuery.builder()
                    .field("field")
                    .value(10)
                    .boost(2f)
                    .name("term")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.TermQuery.Builder()
                    .field("field")
                    .value(10)
                    .boost(2f)
                    .queryName("term")
                    .build();

            final var query = queryTransformer.transform(termQuery);

            assertEquals(expected.toString(), query.toString(), "must return a term query");
        }

        @Test
        @DisplayName("Terms")
        void testTermsQuery() {
            final var termsQuery = TermsQuery.builder()
                    .field("field")
                    .value(10)
                    .value(20)
                    .boost(2f)
                    .name("terms")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery.Builder()
                    .field("field")
                    .terms(new TermsQueryField.Builder()
                            .value(List.of(FieldValue.of(10), FieldValue.of(20)))
                            .build())
                    .boost(2f)
                    .queryName("terms")
                    .build();

            final var query = queryTransformer.transform(termsQuery);

            assertEquals(expected.toString(), query.toString(), "must return a terms query");
        }

        @Test
        @DisplayName("Fuzzy")
        void testFuzzyQuery() {
            final var fuzzyQuery = FuzzyQuery.builder()
                    .field("field")
                    .value("query")
                    .fuzziness(Fuzziness.builder()
                            .amount("AUTO")
                            .prefixLength(0)
                            .transposition(true)
                            .rewrite("rewrite")
                            .build())
                    .maximumExpansions(20)
                    .boost(2f)
                    .name("fuzzy")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.FuzzyQuery.Builder()
                    .field("field")
                    .value("query")
                    .prefixLength(0)
                    .transpositions(true)
                    .rewrite("rewrite")
                    .maxExpansions(20)
                    .boost(2f)
                    .fuzziness("AUTO")
                    .queryName("fuzzy")
                    .build();

            final var query = queryTransformer.transform(fuzzyQuery);

            assertEquals(expected.toString(), query.toString(), "must return a fuzzy query");
        }

        @Test
        @DisplayName("Wildcard")
        void testWildcardQuery() {
            final var wildcardQuery = WildcardQuery.builder()
                    .field("field")
                    .value("pre*post")
                    .caseInsensitive(true)
                    .rewrite("rewrite")
                    .boost(2f)
                    .name("wildcard")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery.Builder()
                    .field("field")
                    .value("pre*post")
                    .caseInsensitive(true)
                    .rewrite("rewrite")
                    .boost(2f)
                    .queryName("wildcard")
                    .build();

            final var query = queryTransformer.transform(wildcardQuery);

            assertEquals(expected.toString(), query.toString(), "must return a wildcard query");
        }
    }

    @Nested
    class Text {

        @Test
        @DisplayName("Match")
        void testMatchQuery() {
            final var matchQuery = MatchQuery.builder()
                    .field("field")
                    .value("query")
                    .analysis(Analysis.builder()
                            .analyzer("english")
                            .maximumExpansions(100)
                            .returnAllForZeroTerms(true)
                            .generateSynonymQueries(true)
                            .build())
                    .terms(Terms.builder()
                            .matchAllTerms(true)
                            .minimumMatch("1")
                            .build())
                    .fuzziness(Fuzziness.builder()
                            .amount("AUTO")
                            .prefixLength(0)
                            .transposition(true)
                            .rewrite("rewrite")
                            .build())
                    .ignoreErrors(true)
                    .boost(2f)
                    .name("match")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery.Builder()
                    .field("field")
                    .query("query")
                    .analyzer("english")
                    .maxExpansions(100)
                    .zeroTermsQuery(ZeroTermsQuery.All)
                    .autoGenerateSynonymsPhraseQuery(true)
                    .operator(Operator.And)
                    .minimumShouldMatch("1")
                    .fuzziness("AUTO")
                    .prefixLength(0)
                    .fuzzyTranspositions(true)
                    .fuzzyRewrite("rewrite")
                    .lenient(true)
                    .boost(2f)
                    .queryName("match")
                    .build();

            final var query = queryTransformer.transform(matchQuery);

            assertEquals(expected.toString(), query.toString(), "must return a match query");
        }

        @Test
        @DisplayName("Match Bool Prefix")
        void testMatchBoolPrefixQuery() {
            final var prefixQuery = MatchBoolPrefixQuery.builder()
                    .field("field")
                    .value("query")
                    .analysis(Analysis.builder()
                            .analyzer("english")
                            .maximumExpansions(100)
                            .build())
                    .terms(Terms.builder()
                            .matchAllTerms(false)
                            .minimumMatch("1")
                            .build())
                    .fuzziness(Fuzziness.builder()
                            .amount("AUTO")
                            .prefixLength(0)
                            .transposition(true)
                            .rewrite("rewrite")
                            .build())
                    .boost(2f)
                    .name("match")
                    .build();

            final var expected =
                    new co.elastic.clients.elasticsearch._types.query_dsl.MatchBoolPrefixQuery.Builder()
                            .field("field")
                            .query("query")
                            .analyzer("english")
                            .maxExpansions(100)
                            .operator(Operator.Or)
                            .minimumShouldMatch("1")
                            .fuzziness("AUTO")
                            .prefixLength(0)
                            .fuzzyTranspositions(true)
                            .fuzzyRewrite("rewrite")
                            .boost(2f)
                            .queryName("match")
                            .build();

            final var query = queryTransformer.transform(prefixQuery);

            assertEquals(expected.toString(), query.toString(), "must return a match bool prefix query");
        }

        @Test
        @DisplayName("Match Phrase")
        void testMatchPhraseQuery() {
            final var phraseQuery = MatchPhraseQuery.builder()
                    .field("field")
                    .value("query")
                    .analysis(Analysis.builder()
                            .analyzer("english")
                            .returnAllForZeroTerms(false)
                            .build())
                    .slop(1)
                    .boost(2f)
                    .name("match")
                    .build();

            final var expected =
                    new co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery.Builder()
                            .field("field")
                            .query("query")
                            .analyzer("english")
                            .zeroTermsQuery(ZeroTermsQuery.None)
                            .slop(1)
                            .boost(2f)
                            .queryName("match")
                            .build();

            final var query = queryTransformer.transform(phraseQuery);

            assertEquals(expected.toString(), query.toString(), "must return a match phrase query");
        }

        @Test
        @DisplayName("Match Phrase Prefix")
        void testMatchPhrasePrefixQuery() {
            final var phrasePrefixQuery = MatchPhrasePrefixQuery.builder()
                    .field("field")
                    .value("query")
                    .analysis(Analysis.builder()
                            .analyzer("english")
                            .maximumExpansions(100)
                            .returnAllForZeroTerms(true)
                            .build())
                    .slop(1)
                    .boost(2f)
                    .name("match")
                    .build();

            final var expected =
                    new co.elastic.clients.elasticsearch._types.query_dsl.MatchPhrasePrefixQuery.Builder()
                            .field("field")
                            .query("query")
                            .analyzer("english")
                            .maxExpansions(100)
                            .zeroTermsQuery(ZeroTermsQuery.All)
                            .slop(1)
                            .boost(2f)
                            .queryName("match")
                            .build();

            final var query = queryTransformer.transform(phrasePrefixQuery);

            assertEquals(expected.toString(), query.toString(), "must return a match phrase prefix query");
        }

        @Test
        @DisplayName("Multi Match")
        void testMultiMatchQuery() {
            final var multiMatchQuery = MultiMatchQuery.builder()
                    .fieldName(null)
                    .fieldName("field")
                    .fieldNames(null)
                    .fieldNames(List.of("field2"))
                    .matchType("best_fields")
                    .value("query")
                    .fieldBoost("boost_field", 2.00f)
                    .tieBreaker(0.3f)
                    .analysis(Analysis.builder()
                            .analyzer("english")
                            .maximumExpansions(100)
                            .returnAllForZeroTerms(true)
                            .generateSynonymQueries(true)
                            .build())
                    .terms(Terms.builder()
                            .matchAllTerms(false)
                            .minimumMatch("1")
                            .build())
                    .fuzziness(Fuzziness.builder()
                            .amount("AUTO")
                            .prefixLength(0)
                            .transposition(true)
                            .rewrite("rewrite")
                            .build())
                    .slop(1)
                    .ignoreErrors(true)
                    .boost(2f)
                    .name("multi_match")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery.Builder()
                    .fields("field^1.000000", "field2^1.000000", "boost_field^2.000000")
                    .query("query")
                    .type(TextQueryType.BestFields)
                    .tieBreaker(0.30000001192092896)
                    .analyzer("english")
                    .maxExpansions(100)
                    .zeroTermsQuery(ZeroTermsQuery.All)
                    .autoGenerateSynonymsPhraseQuery(true)
                    .operator(Operator.Or)
                    .minimumShouldMatch("1")
                    .fuzziness("AUTO")
                    .prefixLength(0)
                    .fuzzyTranspositions(true)
                    .fuzzyRewrite("rewrite")
                    .slop(1)
                    .lenient(true)
                    .boost(2f)
                    .queryName("multi_match")
                    .build();

            final var query = queryTransformer.transform(multiMatchQuery);

            assertEquals(expected.toString(), query.toString(), "must return a multi match query");
        }
    }

    @Nested
    class Compound {

        @Test
        @DisplayName("Bool")
        void testBoolQuery() {
            final var boolQuery = BoolQuery.builder()
                    .must(ALL_QUERY)
                    .must(ALL_QUERY)
                    .filter(ALL_QUERY)
                    .filter(ALL_QUERY)
                    .mustNot(NONE_QUERY)
                    .should(ALL_QUERY)
                    .minimumMatch("2")
                    .boost(3f)
                    .name("bool")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder()
                    .must(ALL_BUILDER)
                    .must(ALL_BUILDER)
                    .filter(ALL_BUILDER)
                    .filter(ALL_BUILDER)
                    .mustNot(NONE_BUILDER)
                    .should(ALL_BUILDER)
                    .minimumShouldMatch("2")
                    .boost(3f)
                    .queryName("bool")
                    .build();

            final var query = queryTransformer.transform(boolQuery);

            assertEquals(expected.toString(), query.toString(), "must return a bool query");
        }

        @Test
        @DisplayName("Boosting")
        void testBoostingQuery() {
            final var boostingQuery = BoostingQuery.builder()
                    .positive(ALL_QUERY)
                    .negative(NONE_QUERY)
                    .boost(3f)
                    .negativeBoost(2f)
                    .name("boost")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.BoostingQuery.Builder()
                    .positive(ALL_BUILDER)
                    .negative(NONE_BUILDER)
                    .boost(3f)
                    .negativeBoost(2f)
                    .queryName("boost")
                    .build();

            final var query = queryTransformer.transform(boostingQuery);

            assertEquals(expected.toString(), query.toString(), "must return a boosting query");
        }

        @Test
        @DisplayName("Constant Score")
        void testConstantScoreQuery() {
            final var constantScoreQuery = ConstantScoreQuery.builder()
                    .query(ALL_QUERY)
                    .boost(2f)
                    .name("fixed")
                    .build();

            final var expected =
                    new co.elastic.clients.elasticsearch._types.query_dsl.ConstantScoreQuery.Builder()
                            .filter(ALL_BUILDER)
                            .boost(2f)
                            .queryName("fixed")
                            .build();

            final var query = queryTransformer.transform(constantScoreQuery);

            assertEquals(expected.toString(), query.toString(), "must return a constant score query");
        }

        @Test
        @DisplayName("Function Score")
        void testFunctionScoreQuery() {
            final var functionScoreQuery = FunctionScoreQuery.builder()
                    .query(ALL_QUERY)
                    .function(WeightScore.builder()
                            .weight(2f)
                            .build())
                    .maxBoost(10f)
                    .minScore(1f)
                    .boostMode("Multiply")
                    .scoreMode("Max")
                    .boost(2f)
                    .name("function_score")
                    .build();

            final var expected =
                    new co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreQuery.Builder()
                            .functions(new FunctionScore.Builder().weight(2d).build())
                            .maxBoost(10d)
                            .minScore(1d)
                            .boostMode(FunctionBoostMode.Multiply)
                            .scoreMode(FunctionScoreMode.Max)
                            .boost(2f)
                            .queryName("function_score")
                            .query(ALL_BUILDER)
                            .build();

            final var query = queryTransformer.transform(functionScoreQuery);

            assertEquals(expected.toString(), query.toString(), "must return a function score query");
        }

        @Test
        @DisplayName("Function Score without Query")
        void testFunctionScoreQueryWithoutInnerQuery() {
            final var functionScoreQuery = FunctionScoreQuery.builder()
                    .function(WeightScore.builder()
                            .weight(2f)
                            .build())
                    .maxBoost(10f)
                    .minScore(1f)
                    .boostMode("Multiply")
                    .scoreMode("Max")
                    .boost(2f)
                    .name("function_score")
                    .build();

            final var expected =
                    new co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreQuery.Builder()
                            .functions(new FunctionScore.Builder().weight(2d).build())
                            .maxBoost(10d)
                            .minScore(1d)
                            .boostMode(FunctionBoostMode.Multiply)
                            .scoreMode(FunctionScoreMode.Max)
                            .boost(2f)
                            .queryName("function_score")
                            .build();

            final var query = queryTransformer.transform(functionScoreQuery);

            assertEquals(expected.toString(), query.toString(), "must return a function score query");
        }

        @Test
        @DisplayName("DisMax")
        void testDisMaxQuery() {
            final var disMax = DisMaxQuery.builder()
                    .query(ALL_QUERY)
                    .query(NONE_QUERY)
                    .tieBreaker(0.5f)
                    .boost(3f)
                    .name("disMax")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.DisMaxQuery.Builder()
                    .queries(List.of(ALL_BUILDER, NONE_BUILDER))
                    .tieBreaker(0.5d)
                    .boost(3f)
                    .queryName("disMax")
                    .build();

            final var query = queryTransformer.transform(disMax);

            assertEquals(expected.toString(), query.toString(), "must return a dis max query");
        }
    }

    @Nested
    class Geo {

        @Test
        @DisplayName("Geo Distance")
        void testGeoDistanceQuery() {
            final var geoDistanceQuery = GeoDistanceQuery.builder()
                    .field("location")
                    .center(Location.builder()
                            .lat(10d)
                            .lon(20d)
                            .build())
                    .distance("10km")
                    .distanceType("Arc")
                    .ignoreMissingField(true)
                    .boost(2f)
                    .name("geo_distance")
                    .build();

            final var expected =
                    new co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery.Builder()
                            .field("location")
                            .location(new GeoLocation.Builder()
                                    .latlon(new LatLonGeoLocation.Builder()
                                            .lat(10d)
                                            .lon(20d)
                                            .build())
                                    .build())
                            .distance("10km")
                            .distanceType(GeoDistanceType.Arc)
                            .ignoreUnmapped(true)
                            .boost(2f)
                            .queryName("geo_distance")
                            .build();

            final var query = queryTransformer.transform(geoDistanceQuery);

            assertEquals(expected.toString(), query.toString(), "must return a geo distance query");
        }

        @Test
        @DisplayName("Geo Point")
        void testGeoPointQuery() {
            final var pointQuery = GeoPointQuery.builder()
                    .field("location")
                    .point(Location.builder()
                            .lat(10d)
                            .lon(10d)
                            .build())
                    .ignoreMissingField(true)
                    .boost(2f)
                    .name("geo_point")
                    .build();

            final var expected = new GeoShapeQuery.Builder()
                    .field("location")
                    .shape(new GeoShapeFieldQuery.Builder()
                            .shape(JsonData.fromJson("""
                                    {
                                        "type":"Point",
                                        "coordinates": [10.0, 10.0]
                                    }
                                    """))
                            .build())
                    .queryName("geo_point")
                    .boost(2f)
                    .ignoreUnmapped(true)
                    .build();

            final var query = queryTransformer.transform(pointQuery);

            assertEquals(expected.toString(), query.toString(), "must return a geo shape query");
        }
    }

    @Nested
    class Special {

        @Test
        @DisplayName("Script")
        void testScriptQuery() {
            final var scriptQuery = ScriptQuery.builder()
                    .script(Script.builder()
                            .language("painless")
                            .id("id")
                            .parameter("field", "value")
                            .build())
                    .boost(2f)
                    .name("script")
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.query_dsl.ScriptQuery.Builder()
                    .script(new co.elastic.clients.elasticsearch._types.Script.Builder()
                            .id("id")
                            .params(Map.of("field", JsonData.of("value")))
                            .build())
                    .queryName("script")
                    .boost(2f)
                    .build();

            final var query = queryTransformer.transform(scriptQuery);

            assertEquals(expected.toString(), query.toString(), "must return a script query");
        }

        @Test
        @DisplayName("Knn")
        void testKnnQuery() {
            final Query knnQuery = KnnQuery.builder()
                    .name("knn")
                    .boost(2f)
                    .field("embedding")
                    .queryVector(List.of(1f, 2f))
                    .build();

            final var expected = new co.elastic.clients.elasticsearch._types.KnnQuery.Builder()
                    .field("embedding")
                    .queryVector(List.of(1f, 2f))
                    .queryName("knn")
                    .boost(2f)
                    .build();

            final var query = queryTransformer.transform(knnQuery);

            assertEquals(expected.toString(), query.toString(), "must return a knn query");
        }

        @Test
        @DisplayName("Knn with full params and filter")
        void testKnnQueryWithFullParams() {
            final Query knnQuery = KnnQuery.builder()
                    .name("knn-full")
                    .boost(1.5f)
                    .field("embedding")
                    .queryVector(List.of(1f, 2f, 3f))
                    .k(10)
                    .numCandidates(50)
                    .similarity(0.9f)
                    .oversample(1.5f)
                    .filter(TermQuery.builder().field("status").value("active").build())
                    .build();

            final var query = queryTransformer.transform(knnQuery);

            final var queryStr = query.toString();
            // Verify KNN query has all parameters set
            assertTrue(queryStr.contains("embedding"), "must contain field");
            assertTrue(queryStr.contains("active"), "must contain filter term");
        }
    }
}
