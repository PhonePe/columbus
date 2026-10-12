package com.phonepe.columbus.elastic7.internal.search.score;

import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.misc.script.Script;
import com.phonepe.columbus.search.query.simple.MatchNoneQuery;
import com.phonepe.columbus.search.score.DecayScore;
import com.phonepe.columbus.search.score.FieldScore;
import com.phonepe.columbus.search.score.RandomScore;
import com.phonepe.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.columbus.search.score.ScriptScore;
import com.phonepe.columbus.search.score.WeightScore;
import com.phonepe.columbus.elastic7.test.annotations.WithElasticToolkit;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction.Modifier;
import org.elasticsearch.index.query.MatchNoneQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.script.ScriptType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithElasticToolkit.All
class ElasticScoreFunctionTransformerTest {

    private static ScoreFunctionTransformer<FilterFunctionBuilder, Object> scoreFunctionTransformer;

    @Test
    @DisplayName("Weight")
    void testWeightScore() {
        final var scoreFunction = WeightScore.builder()
                .weight(2f)
                .query(MatchNoneQuery.builder().build())
                .build();

        final var expected = new FilterFunctionBuilder(
                new MatchNoneQueryBuilder(),
                ScoreFunctionBuilders.weightFactorFunction(2f));

        final var filterFunctionBuilder = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected, filterFunctionBuilder, "must return a weight score");
    }

    @Test
    @DisplayName("Field")
    void testFieldScore() {
        final var scoreFunction = FieldScore.builder()
                .field("field")
                .factor(3f)
                .missing(1f)
                .modifier("none")
                .weight(2f)
                .build();

        final var expected = new FilterFunctionBuilder(ScoreFunctionBuilders.fieldValueFactorFunction("field")
                .factor(3f)
                .missing(1)
                .modifier(Modifier.NONE)
                .setWeight(2f));

        final var filterFunctionBuilder = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected, filterFunctionBuilder, "must return a field score");
    }

    @Test
    @DisplayName("Random")
    void testRandomScore() {
        final var scoreFunction = RandomScore.builder()
                .field("field")
                .seed(997)
                .weight(2f)
                .build();

        final var expected = new FilterFunctionBuilder(ScoreFunctionBuilders.randomFunction()
                .setField("field")
                .seed(997)
                .setWeight(2f));

        final var filterFunctionBuilder = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected, filterFunctionBuilder, "must return a random score");
    }

    @Test
    @DisplayName("Linear Decay")
    void testLinearDecayScore() {
        final var scoreFunction = DecayScore.builder()
                .mode("linear")
                .field("field")
                .origin(5)
                .scale(2)
                .offset(1)
                .decay(0.4)
                .weight(2f)
                .build();

        final var expected = new FilterFunctionBuilder(ScoreFunctionBuilders.linearDecayFunction("field", 5, 2, 1, 0.4)
                .setWeight(2f));

        final var filterFunctionBuilder = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected, filterFunctionBuilder, "must return a linear decay score");
    }

    @Test
    @DisplayName("Gauss Decay")
    void testGaussDecayScore() {
        final var scoreFunction = DecayScore.builder()
                .mode("gauss")
                .field("field")
                .origin(5)
                .scale(2)
                .offset(1)
                .decay(0.4)
                .weight(2f)
                .build();

        final var expected = new FilterFunctionBuilder(ScoreFunctionBuilders.gaussDecayFunction("field", 5, 2, 1, 0.4)
                .setWeight(2f));

        final var filterFunctionBuilder = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected, filterFunctionBuilder, "must return a gauss decay score");
    }

    @Test
    @DisplayName("Exponential Decay")
    void testExponentialDecayScore() {
        final var scoreFunction = DecayScore.builder()
                .mode("exponential")
                .field("field")
                .origin(5)
                .scale(2)
                .offset(1)
                .decay(0.4)
                .weight(2f)
                .build();

        final var expected = new FilterFunctionBuilder(
                ScoreFunctionBuilders.exponentialDecayFunction("field", 5, 2, 1, 0.4)
                        .setWeight(2f));

        final var filterFunctionBuilder = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected, filterFunctionBuilder, "must return a exponential decay score");
    }

    @Test
    @DisplayName("invalid Decay")
    void testInvalidDecayScore() {
        final var scoreFunction = DecayScore.builder()
                .mode("invalid")
                .field("field")
                .origin(5)
                .scale(2)
                .offset(1)
                .decay(0.4)
                .weight(2f)
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> scoreFunctionTransformer.transform(scoreFunction),
                "must throw for invalid decay score modes");
    }

    @Test
    @DisplayName("Script")
    void testScriptScore() {
        final var scoreFunction = ScriptScore.builder()
                .script(Script.builder()
                        .language("painless")
                        .source("_doc")
                        .parameter("key", "value")
                        .build())
                .weight(2f)
                .build();

        final var script = new org.elasticsearch.script.Script(
                ScriptType.INLINE, "painless", "_doc", Map.of("key", "value"));
        final var expected = new FilterFunctionBuilder(ScoreFunctionBuilders.scriptFunction(script)
                .setWeight(2f));

        final var filterFunctionBuilder = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected, filterFunctionBuilder, "must return a script score");
    }

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        scoreFunctionTransformer = toolkit.scoreFunctionTransformer();
    }
}
