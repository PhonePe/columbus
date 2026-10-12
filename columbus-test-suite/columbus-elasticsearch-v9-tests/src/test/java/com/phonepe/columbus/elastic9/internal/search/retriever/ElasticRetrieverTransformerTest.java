package com.phonepe.columbus.elastic9.internal.search.retriever;

import co.elastic.clients.elasticsearch._types.InnerRetriever;
import co.elastic.clients.elasticsearch._types.RescoreVector;
import co.elastic.clients.elasticsearch._types.Retriever;
import co.elastic.clients.elasticsearch._types.ScoreNormalizer;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.search.filter.Filter;
import com.phonepe.columbus.search.filter.general.EqualsFilter;
import com.phonepe.columbus.search.filter.range.GreaterThanFilter;
import com.phonepe.columbus.search.query.term.TermQuery;
import com.phonepe.columbus.search.retriever.KnnRetriever;
import com.phonepe.columbus.search.retriever.LinearRetriever;
import com.phonepe.columbus.search.retriever.PinnedRetriever;
import com.phonepe.columbus.search.retriever.RetrieverTransformer;
import com.phonepe.columbus.search.retriever.RrfRetriever;
import com.phonepe.columbus.search.retriever.StandardRetriever;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithElasticToolkit.All
public class ElasticRetrieverTransformerTest {

    private static final Filter EQUAL_FILTER = EqualsFilter.builder()
            .field("field_eq")
            .value("value_eq")
            .build();

    private static final Filter GREATER_THAN_FILTER = GreaterThanFilter.builder()
            .field("field_num")
            .value(100)
            .build();

    private static final RangeQuery ELASTIC_GREATER_THAN_FILTER =
            new RangeQuery.Builder()
                    .number(new NumberRangeQuery.Builder()
                            .field("field_num")
                            .gt(100d)
                            .build())
                    .build();

    private static final StandardRetriever STANDARD_RETRIEVER = StandardRetriever.builder()
            .query(TermQuery.builder()
                    .field("field")
                    .value("value")
                    .build())
            .minScore(1.0f)
            .filter(GREATER_THAN_FILTER)
            .filter(EQUAL_FILTER)
            .build();

    private static final KnnRetriever KNN_RETRIEVER = KnnRetriever.builder()
            .field("vector_field")
            .queryVector(List.of(1.0f, 2.0f, 3.0f))
            .k(5)
            .numCandidates(10)
            .oversample(1.5f)
            .filter(EQUAL_FILTER)
            .build();

    private static final co.elastic.clients.elasticsearch._types.query_dsl.TermQuery ELASTIC_EQUAL_FILTER =
            new co.elastic.clients.elasticsearch._types.query_dsl.TermQuery.Builder()
                    .field("field_eq")
                    .value("value_eq")
                    .build();

    private static final Retriever ELASTIC_STANDARD_RETRIEVER =
            new Retriever.Builder()
                    .standard(new co.elastic.clients.elasticsearch._types.StandardRetriever.Builder()
                            .query(new co.elastic.clients.elasticsearch._types.query_dsl.TermQuery.Builder()
                                    .field("field")
                                    .value("value")
                                    .build())
                            .minScore(1.0f)
                            .filter(ELASTIC_GREATER_THAN_FILTER)
                            .filter(ELASTIC_EQUAL_FILTER)
                            .build())
                    .build();

    private static final Retriever ELASTIC_KNN_RETRIEVER = new Retriever.Builder()
            .knn(new co.elastic.clients.elasticsearch._types.KnnRetriever.Builder()
                    .field("vector_field")
                    .queryVector(List.of(1.0f, 2.0f, 3.0f))
                    .k(5)
                    .numCandidates(10)
                    .filter(ELASTIC_EQUAL_FILTER)
                    .rescoreVector(new RescoreVector.Builder().oversample(1.5f).build())
                    .build())
            .build();

    private static RetrieverTransformer<Retriever, Object> retrieverTransformer;

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        retrieverTransformer = toolkit.retrieverTransformer();
    }

    @Nested
    class Simple {

        @Test
        @DisplayName("Standard")
        void testStandardRetriever() {
            final var transformedRetriever = retrieverTransformer.transform(STANDARD_RETRIEVER);

            assertEquals(
                    ELASTIC_STANDARD_RETRIEVER.toString(), transformedRetriever.toString(),
                    "Must return expected standard retriever");
        }

        @Test
        @DisplayName("Knn")
        void testKnnRetriever() {
            final var transformedRetriever = retrieverTransformer.transform(KNN_RETRIEVER);

            assertEquals(
                    ELASTIC_KNN_RETRIEVER.toString(), transformedRetriever.toString(),
                    "Must return expected knn retriever");
        }

        @Test
        @DisplayName("Rrf")
        void testRrfRetriever() {
            final var rrfRetriever = RrfRetriever.builder()
                    .retriever(STANDARD_RETRIEVER)
                    .retriever(KNN_RETRIEVER)
                    .filter(EQUAL_FILTER)
                    .filter(GREATER_THAN_FILTER)
                    .rankConstant(2)
                    .rankWindowSize(5)
                    .build();

            final var expected = new Retriever.Builder().rrf(
                    new co.elastic.clients.elasticsearch._types.RRFRetriever.Builder()
                            .retrievers(List.of(ELASTIC_STANDARD_RETRIEVER, ELASTIC_KNN_RETRIEVER))
                            .rankConstant(2)
                            .rankWindowSize(5)
                            .filter(ELASTIC_EQUAL_FILTER)
                            .filter(ELASTIC_GREATER_THAN_FILTER)
                            .build()).build();

            final var transformedRetriever = retrieverTransformer.transform(rrfRetriever);

            assertEquals(expected.toString(), transformedRetriever.toString(), "Must return expected rrf retriever");
        }

        @Test
        @DisplayName("Linear")
        void testLinearRetriever() {
            final var linearRetriever = LinearRetriever.builder()
                    .retriever(STANDARD_RETRIEVER, 2.0f)
                    .retriever(KNN_RETRIEVER, 1.5f)
                    .filter(EQUAL_FILTER)
                    .filter(GREATER_THAN_FILTER)
                    .rankWindowSize(5)
                    .normalizer("l2_norm")
                    .build();

            final var expected = new Retriever.Builder().linear(
                    new co.elastic.clients.elasticsearch._types.LinearRetriever.Builder()
                            .retrievers(new InnerRetriever.Builder()
                                    .retriever(ELASTIC_STANDARD_RETRIEVER)
                                    .weight(2.0f)
                                    .normalizer(ScoreNormalizer.L2Norm)
                                    .build())
                            .retrievers(new InnerRetriever.Builder()
                                    .retriever(ELASTIC_KNN_RETRIEVER)
                                    .weight(1.5f)
                                    .normalizer(ScoreNormalizer.L2Norm)
                                    .build())
                            .rankWindowSize(5)
                            .filter(ELASTIC_EQUAL_FILTER)
                            .filter(ELASTIC_GREATER_THAN_FILTER)
                            .normalizer(ScoreNormalizer.L2Norm)
                            .build()).build();

            final var transformedRetriever = retrieverTransformer.transform(linearRetriever);

            assertEquals(expected.toString(), transformedRetriever.toString(), "Must return expected linear retriever");
        }

        @Test
        @DisplayName("Pinned")
        void testPinnedRetriever() {
            final var pinnedRetriever = PinnedRetriever.builder()
                    .id("id_1")
                    .id("id_2")
                    .retriever(STANDARD_RETRIEVER)
                    .build();

            final var expected = new Retriever.Builder().pinned(
                    new co.elastic.clients.elasticsearch._types.PinnedRetriever.Builder()
                            .ids(List.of("id_1", "id_2"))
                            .retriever(ELASTIC_STANDARD_RETRIEVER)
                            .build()).build();

            final var transformedRetriever = retrieverTransformer.transform(pinnedRetriever);

            assertEquals(expected.toString(), transformedRetriever.toString(), "Must return expected pinned retriever");
        }
    }
}
