package com.phonepe.commons.columbus.elastic.internal.search.query;

import com.phonepe.commons.columbus.dao.id.Ids;
import com.phonepe.commons.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.search.misc.geo.Location;
import com.phonepe.commons.columbus.search.misc.script.Script;
import com.phonepe.commons.columbus.search.misc.text.Analysis;
import com.phonepe.commons.columbus.search.misc.text.Fuzziness;
import com.phonepe.commons.columbus.search.misc.text.Terms;
import com.phonepe.commons.columbus.search.query.Query;
import com.phonepe.commons.columbus.search.query.QueryTransformer;
import com.phonepe.commons.columbus.search.query.compound.BoolQuery;
import com.phonepe.commons.columbus.search.query.compound.BoostingQuery;
import com.phonepe.commons.columbus.search.query.compound.ConstantScoreQuery;
import com.phonepe.commons.columbus.search.query.compound.DisMaxQuery;
import com.phonepe.commons.columbus.search.query.compound.FunctionScoreQuery;
import com.phonepe.commons.columbus.search.query.geo.GeoDistanceQuery;
import com.phonepe.commons.columbus.search.query.geo.GeoPointQuery;
import com.phonepe.commons.columbus.search.query.simple.MatchAllQuery;
import com.phonepe.commons.columbus.search.query.simple.MatchNoneQuery;
import com.phonepe.commons.columbus.search.query.special.KnnQuery;
import com.phonepe.commons.columbus.search.query.special.ScriptQuery;
import com.phonepe.commons.columbus.search.query.term.FieldExistsQuery;
import com.phonepe.commons.columbus.search.query.term.FuzzyQuery;
import com.phonepe.commons.columbus.search.query.term.IdsQuery;
import com.phonepe.commons.columbus.search.query.term.PrefixQuery;
import com.phonepe.commons.columbus.search.query.term.RangeQuery;
import com.phonepe.commons.columbus.search.query.term.TermQuery;
import com.phonepe.commons.columbus.search.query.term.TermsQuery;
import com.phonepe.commons.columbus.search.query.term.WildcardQuery;
import com.phonepe.commons.columbus.search.query.text.MatchBoolPrefixQuery;
import com.phonepe.commons.columbus.search.query.text.MatchPhrasePrefixQuery;
import com.phonepe.commons.columbus.search.query.text.MatchPhraseQuery;
import com.phonepe.commons.columbus.search.query.text.MatchQuery;
import com.phonepe.commons.columbus.search.query.text.MultiMatchQuery;
import com.phonepe.commons.columbus.search.score.WeightScore;
import com.phonepe.commons.columbus.test.annotations.WithElasticToolkit;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.ScoreMode;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.geometry.Point;
import org.elasticsearch.index.query.MatchNoneQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.WeightBuilder;
import org.elasticsearch.index.search.MatchQuery.ZeroTermsQuery;
import org.elasticsearch.script.ScriptType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private static final QueryBuilder ALL_BUILDER = QueryBuilders.matchAllQuery()
            .boost(2f)
            .queryName("all");
    private static final QueryBuilder NONE_BUILDER = new MatchNoneQueryBuilder()
            .boost(2f)
            .queryName("none");

    private static QueryTransformer<QueryBuilder, Object> queryTransformer;

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        queryTransformer = toolkit.queryTransformer();
    }

    @Nested
    class Simple {

        @Test
        @DisplayName("Match All")
        void testMatchAllQuery() {
            final var queryBuilder = queryTransformer.transform(ALL_QUERY);

            assertEquals(ALL_BUILDER, queryBuilder, "must return a match all query");
        }

        @Test
        @DisplayName("Match None")
        void testMatchNoneQuery() {
            final var queryBuilder = queryTransformer.transform(NONE_QUERY);

            assertEquals(NONE_BUILDER, queryBuilder, "must return a match none query");
        }
    }

    @Nested
    class Term {

        @Test
        @DisplayName("Field Exists")
        void testFieldExistsQuery() {
            final var query = FieldExistsQuery.builder()
                    .field("field")
                    .boost(2f)
                    .name("exists")
                    .build();

            final var expected = QueryBuilders.existsQuery("field")
                    .boost(2f)
                    .queryName("exists");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return an exists query");
        }

        @Test
        @DisplayName("Ids")
        void testIdsQuery() {
            final var query = IdsQuery.builder()
                    .id(Ids.value("id1"))
                    .id(Ids.value("id2"))
                    .boost(2f)
                    .name("ids")
                    .build();

            final var expected = QueryBuilders.idsQuery().addIds("id1", "id2")
                    .boost(2f)
                    .queryName("ids");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return an ids query");
        }

        @Test
        @DisplayName("Prefix")
        void testPrefixQuery() {
            final var query = PrefixQuery.builder()
                    .field("field")
                    .value("pre")
                    .caseInsensitive(true)
                    .rewrite("rewrite")
                    .boost(2f)
                    .name("prefix")
                    .build();

            final var expected = QueryBuilders.prefixQuery("field", "pre")
                    .caseInsensitive(true)
                    .rewrite("rewrite")
                    .boost(2f)
                    .queryName("prefix");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a prefix query");
        }

        @Test
        @DisplayName("Range")
        void testRangeQuery() {
            final var query = RangeQuery.builder()
                    .field("field")
                    .from(10)
                    .to(20)
                    .boost(2f)
                    .name("range")
                    .build();

            final var expected = QueryBuilders.rangeQuery("field")
                    .from(10)
                    .to(20)
                    .boost(2f)
                    .queryName("range");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a range query");
        }

        @Test
        @DisplayName("Term")
        void testTermQuery() {
            final var query = TermQuery.builder()
                    .field("field")
                    .value(10)
                    .boost(2f)
                    .name("term")
                    .build();

            final var expected = QueryBuilders.termQuery("field", 10)
                    .boost(2f)
                    .queryName("term");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a term query");
        }

        @Test
        @DisplayName("Terms")
        void testTermsQuery() {
            final var query = TermsQuery.builder()
                    .field("field")
                    .value(10)
                    .value(20)
                    .boost(2f)
                    .name("terms")
                    .build();

            final var expected = QueryBuilders.termsQuery("field", List.of(10, 20))
                    .boost(2f)
                    .queryName("terms");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a terms query");
        }

        @Test
        @DisplayName("Fuzzy")
        void testFuzzyQuery() {
            final var query = FuzzyQuery.builder()
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

            final var expected = QueryBuilders.fuzzyQuery("field", "query")
                    .fuzziness(org.elasticsearch.common.unit.Fuzziness.AUTO)
                    .prefixLength(0)
                    .transpositions(true)
                    .rewrite("rewrite")
                    .maxExpansions(20)
                    .boost(2f)
                    .queryName("fuzzy");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a fuzzy query");
        }

        @Test
        @DisplayName("Wildcard")
        void testWildcardQuery() {
            final var query = WildcardQuery.builder()
                    .field("field")
                    .value("pre*post")
                    .caseInsensitive(true)
                    .rewrite("rewrite")
                    .boost(2f)
                    .name("wildcard")
                    .build();

            final var expected = QueryBuilders.wildcardQuery("field", "pre*post")
                    .caseInsensitive(true)
                    .rewrite("rewrite")
                    .boost(2f)
                    .queryName("wildcard");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a wildcard query");
        }
    }

    @Nested
    class Text {

        @Test
        @DisplayName("Match")
        void testMatchQuery() {
            final var query = MatchQuery.builder()
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

            final var expected = QueryBuilders.matchQuery("field", "query")
                    .analyzer("english")
                    .maxExpansions(100)
                    .zeroTermsQuery(ZeroTermsQuery.ALL)
                    .autoGenerateSynonymsPhraseQuery(true)
                    .operator(Operator.AND)
                    .minimumShouldMatch("1")
                    .fuzziness("AUTO")
                    .prefixLength(0)
                    .fuzzyTranspositions(true)
                    .fuzzyRewrite("rewrite")
                    .lenient(true)
                    .boost(2f)
                    .queryName("match");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a match query");
        }

        @Test
        @DisplayName("Match Bool Prefix")
        void testMatchBoolPrefixQuery() {
            final var query = MatchBoolPrefixQuery.builder()
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

            final var expected = QueryBuilders.matchBoolPrefixQuery("field", "query")
                    .analyzer("english")
                    .maxExpansions(100)
                    .operator(Operator.OR)
                    .minimumShouldMatch("1")
                    .fuzziness("AUTO")
                    .prefixLength(0)
                    .fuzzyTranspositions(true)
                    .fuzzyRewrite("rewrite")
                    .boost(2f)
                    .queryName("match");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a match bool prefix query");
        }

        @Test
        @DisplayName("Match Phrase")
        void testMatchPhraseQuery() {
            final var query = MatchPhraseQuery.builder()
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

            final var expected = QueryBuilders.matchPhraseQuery("field", "query")
                    .analyzer("english")
                    .zeroTermsQuery(ZeroTermsQuery.NONE)
                    .slop(1)
                    .boost(2f)
                    .queryName("match");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a match phrase query");
        }

        @Test
        @DisplayName("Match Phrase Prefix")
        void testMatchPhrasePrefixQuery() {
            final var query = MatchPhrasePrefixQuery.builder()
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

            final var expected = QueryBuilders.matchPhrasePrefixQuery("field", "query")
                    .analyzer("english")
                    .maxExpansions(100)
                    .zeroTermsQuery(ZeroTermsQuery.ALL)
                    .slop(1)
                    .boost(2f)
                    .queryName("match");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a match phrase prefix query");
        }

        @Test
        @DisplayName("Multi Match")
        void testMultiMatchQuery() {
            final var query = MultiMatchQuery.builder()
                    .fieldName(null)
                    .fieldName("field")
                    .fieldNames(null)
                    .fieldNames(List.of("field2"))
                    .matchType("best_fields")
                    .value("query")
                    .fieldBoost("boost_field", 2f)
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

            final var expected = QueryBuilders.multiMatchQuery("query", "field", "field2")
                    .type(Type.BEST_FIELDS)
                    .field("boost_field", 2f)
                    .tieBreaker(0.3f)
                    .analyzer("english")
                    .maxExpansions(100)
                    .zeroTermsQuery(ZeroTermsQuery.ALL)
                    .autoGenerateSynonymsPhraseQuery(true)
                    .operator(Operator.OR)
                    .minimumShouldMatch("1")
                    .fuzziness("AUTO")
                    .prefixLength(0)
                    .fuzzyTranspositions(true)
                    .fuzzyRewrite("rewrite")
                    .slop(1)
                    .lenient(true)
                    .boost(2f)
                    .queryName("multi_match");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a multi match query");
        }
    }

    @Nested
    class Compound {

        @Test
        @DisplayName("Bool")
        void testBoolQuery() {
            final var query = BoolQuery.builder()
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

            final var expected = QueryBuilders.boolQuery()
                    .must(ALL_BUILDER)
                    .must(ALL_BUILDER)
                    .filter(ALL_BUILDER)
                    .filter(ALL_BUILDER)
                    .mustNot(NONE_BUILDER)
                    .should(ALL_BUILDER)
                    .minimumShouldMatch(2)
                    .boost(3f)
                    .queryName("bool");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a bool query");
        }

        @Test
        @DisplayName("Boosting")
        void testBoostingQuery() {
            final var query = BoostingQuery.builder()
                    .positive(ALL_QUERY)
                    .negative(NONE_QUERY)
                    .boost(3f)
                    .negativeBoost(2f)
                    .name("boost")
                    .build();

            final var expected = QueryBuilders.boostingQuery(ALL_BUILDER, NONE_BUILDER)
                    .boost(3f)
                    .negativeBoost(2f)
                    .queryName("boost");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a boosting query");
        }

        @Test
        @DisplayName("Constant Score")
        void testConstantScoreQuery() {
            final var query = ConstantScoreQuery.builder()
                    .query(ALL_QUERY)
                    .boost(2f)
                    .name("fixed")
                    .build();

            final var expected = QueryBuilders.constantScoreQuery(ALL_BUILDER)
                    .boost(2f)
                    .queryName("fixed");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a constant score query");
        }

        @Test
        @DisplayName("Function Score")
        void testFunctionScoreQuery() {
            final var query = FunctionScoreQuery.builder()
                    .query(ALL_QUERY)
                    .function(WeightScore.builder()
                            .weight(2f)
                            .build())
                    .maxBoost(10f)
                    .minScore(1f)
                    .boostMode("multiply")
                    .scoreMode("max")
                    .boost(2f)
                    .name("function_score")
                    .build();

            final var expected = QueryBuilders.functionScoreQuery(ALL_BUILDER, new WeightBuilder()
                            .setWeight(2f))
                    .maxBoost(10f)
                    .setMinScore(1f)
                    .boostMode(CombineFunction.MULTIPLY)
                    .scoreMode(ScoreMode.MAX)
                    .boost(2f)
                    .queryName("function_score");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a function score query");
        }

        @Test
        @DisplayName("Function Score without Query")
        void testFunctionScoreQueryWithoutInnerQuery() {
            final var query = FunctionScoreQuery.builder()
                    .function(WeightScore.builder()
                            .weight(2f)
                            .build())
                    .maxBoost(10f)
                    .minScore(1f)
                    .boostMode("multiply")
                    .scoreMode("max")
                    .boost(2f)
                    .name("function_score")
                    .build();

            final var expected = QueryBuilders.functionScoreQuery(new WeightBuilder()
                            .setWeight(2f))
                    .maxBoost(10f)
                    .setMinScore(1f)
                    .boostMode(CombineFunction.MULTIPLY)
                    .scoreMode(ScoreMode.MAX)
                    .boost(2f)
                    .queryName("function_score");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a function score query");
        }

        @Test
        @DisplayName("DisMax")
        void testDisMaxQuery() {
            final var query = DisMaxQuery.builder()
                    .query(ALL_QUERY)
                    .query(NONE_QUERY)
                    .tieBreaker(0.5f)
                    .boost(3f)
                    .name("disMax")
                    .build();

            final var expected = QueryBuilders.disMaxQuery()
                    .add(ALL_BUILDER)
                    .add(NONE_BUILDER)
                    .tieBreaker(0.5f)
                    .boost(3f)
                    .queryName("disMax");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a dis max query");
        }
    }

    @Nested
    class Geo {

        @Test
        @DisplayName("Geo Distance")
        void testGeoDistanceQuery() {
            final var query = GeoDistanceQuery.builder()
                    .field("location")
                    .center(Location.builder()
                            .lat(10d)
                            .lon(20d)
                            .build())
                    .distance("10km")
                    .distanceType("arc")
                    .ignoreMissingField(true)
                    .boost(2f)
                    .name("geo_distance")
                    .build();

            final var expected = QueryBuilders.geoDistanceQuery("location")
                    .point(10, 20)
                    .distance(10, DistanceUnit.KILOMETERS)
                    .geoDistance(GeoDistance.ARC)
                    .ignoreUnmapped(true)
                    .boost(2f)
                    .queryName("geo_distance");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a geo distance query");
        }

        @Test
        @DisplayName("Geo Point")
        void testGeoPointQuery() throws IOException {
            final var query = GeoPointQuery.builder()
                    .field("location")
                    .point(Location.builder()
                            .lat(10d)
                            .lon(20d)
                            .build())
                    .ignoreMissingField(true)
                    .boost(2f)
                    .name("geo_point")
                    .build();

            final var expected = QueryBuilders.geoShapeQuery("location", new Point(20, 10))
                    .ignoreUnmapped(true)
                    .boost(2f)
                    .queryName("geo_point");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a geo shape query");
        }
    }

    @Nested
    class Special {

        @Test
        @DisplayName("Script")
        void testScriptQuery() {
            final var query = ScriptQuery.builder()
                    .script(Script.builder()
                            .language("painless")
                            .id("id")
                            .parameter("field", "value")
                            .build())
                    .boost(2f)
                    .name("script")
                    .build();

            final var expected = QueryBuilders.scriptQuery(new org.elasticsearch.script.Script(
                            ScriptType.STORED, null, "id", Map.of("field", "value")))
                    .boost(2f)
                    .queryName("script");

            final var queryBuilder = queryTransformer.transform(query);

            assertEquals(expected, queryBuilder, "must return a script query");
        }

        @Test
        @DisplayName("Knn")
        void testKnnQuery() {
            final Query query = KnnQuery.builder()
                    .name("knn")
                    .boost(2f)
                    .field("embedding")
                    .queryVector(List.of(1f, 2f))
                    .build();

            assertThrows(IllegalArgumentException.class, () -> queryTransformer.transform(query));
        }
    }
}
