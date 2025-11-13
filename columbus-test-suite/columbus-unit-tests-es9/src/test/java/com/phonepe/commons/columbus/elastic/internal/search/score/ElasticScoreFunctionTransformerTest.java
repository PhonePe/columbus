package com.phonepe.commons.columbus.elastic.internal.search.score;

import co.elastic.clients.elasticsearch._types.ScriptSource;
import co.elastic.clients.elasticsearch._types.query_dsl.DecayFunction;
import co.elastic.clients.elasticsearch._types.query_dsl.DecayPlacement;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorModifier;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorScoreFunction;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.NumericDecayFunction;
import co.elastic.clients.elasticsearch._types.query_dsl.RandomScoreFunction;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptScoreFunction;
import co.elastic.clients.json.JsonData;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.search.misc.script.Script;
import com.phonepe.commons.columbus.search.query.simple.MatchNoneQuery;
import com.phonepe.commons.columbus.search.score.DecayScore;
import com.phonepe.commons.columbus.search.score.FieldScore;
import com.phonepe.commons.columbus.search.score.RandomScore;
import com.phonepe.commons.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.commons.columbus.search.score.ScriptScore;
import com.phonepe.commons.columbus.search.score.WeightScore;
import com.phonepe.commons.columbus.es9.test.annotations.WithElasticToolkit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithElasticToolkit.All
class ElasticScoreFunctionTransformerTest {

    private static ScoreFunctionTransformer<FunctionScore, Object> scoreFunctionTransformer;

    @Test
    @DisplayName("Weight")
    void testWeightScore() {
        final var scoreFunction = WeightScore.builder()
                .weight(2f)
                .query(MatchNoneQuery.builder().build())
                .build();

        final var expected = new FunctionScore.Builder().weight(2d)
                .filter(new co.elastic.clients.elasticsearch._types.query_dsl.MatchNoneQuery.Builder()
                        .build())
                .build();

        final var filterFunction = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected.toString(), filterFunction.toString(), "must return a weight score");
    }

    @Test
    @DisplayName("Field")
    void testFieldScore() {
        final var scoreFunction = FieldScore.builder()
                .field("field")
                .factor(3f)
                .missing(1f)
                .modifier("None")
                .weight(2f)
                .build();

        final var expected = new FunctionScore.Builder().fieldValueFactor(new FieldValueFactorScoreFunction.Builder()
                        .field("field")
                        .factor(3d)
                        .missing(1d)
                        .modifier(FieldValueFactorModifier.None)
                .build())
                .weight(2d)
                .build();

        final var filterFunction = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected.toString(), filterFunction.toString(), "must return a field score");
    }

    @Test
    @DisplayName("Random")
    void testRandomScore() {
        final var scoreFunction = RandomScore.builder()
                .field("field")
                .seed(997)
                .weight(2f)
                .build();

        final var expected = new FunctionScore.Builder().randomScore(new RandomScoreFunction.Builder()
                        .field("field")
                        .seed("997")
                        .build())
                .weight(2d)
                .build();

        final var filterFunction = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected.toString(), filterFunction.toString(), "must return a random score");
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

        final var expected = new FunctionScore.Builder().linear(new DecayFunction.Builder()
                        .numeric(new NumericDecayFunction.Builder()
                            .field("field")
                            .placement(new DecayPlacement.Builder<Double, Double>()
                                    .origin(5d)
                                    .decay(0.4d)
                                    .offset(1d)
                                    .scale(2d)
                                    .build())
                            .build())
                        .build())
                .weight(2d)
                .build();

        final var filterFunction = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected.toString(), filterFunction.toString(), "must return a linear decay score");
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

        final var expected = new FunctionScore.Builder().gauss(new DecayFunction.Builder()
                        .numeric(new NumericDecayFunction.Builder()
                                .field("field")
                                .placement(new DecayPlacement.Builder<Double, Double>()
                                        .origin(5d)
                                        .decay(0.4d)
                                        .offset(1d)
                                        .scale(2d)
                                        .build())
                                .build())
                        .build())
                .weight(2d)
                .build();

        final var filterFunction = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected.toString(), filterFunction.toString(), "must return a gauss decay score");
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

        final var expected = new FunctionScore.Builder().exp(new DecayFunction.Builder()
                        .numeric(new NumericDecayFunction.Builder()
                                .field("field")
                                .placement(new DecayPlacement.Builder<Double, Double>()
                                        .origin(5d)
                                        .decay(0.4d)
                                        .offset(1d)
                                        .scale(2d)
                                        .build())
                                .build())
                        .build())
                .weight(2d)
                .build();

        final var filterFunction = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected.toString(), filterFunction.toString(), "must return a exponential decay score");
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

        final var expected = new FunctionScore.Builder()
                .scriptScore(new ScriptScoreFunction.Builder()
                        .script(new co.elastic.clients.elasticsearch._types.Script.Builder()
                                .source(new ScriptSource.Builder()
                                        .scriptString("_doc")
                                        .build())
                                .params(Map.of("key", JsonData.of("value")))
                                .build())
                        .build())
                .weight(2d)
                .build();

        final var filterFunction = scoreFunctionTransformer.transform(scoreFunction);

        assertEquals(expected.toString(), filterFunction.toString(), "must return a script score");
    }

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        scoreFunctionTransformer = toolkit.scoreFunctionTransformer();
    }
}
