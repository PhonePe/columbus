package com.phonepe.commons.columbus.elastic.storage.handlers;

import com.phonepe.commons.columbus.dao.index.IndexName;
import com.phonepe.commons.columbus.search.query.term.TermQuery;
import com.phonepe.commons.columbus.search.retriever.KnnRetriever;
import com.phonepe.commons.columbus.search.retriever.Retriever;
import com.phonepe.commons.columbus.search.retriever.RrfRetriever;
import com.phonepe.commons.columbus.search.retriever.StandardRetriever;
import com.phonepe.commons.columbus.storage.StorageClient;
import com.phonepe.commons.columbus.storage.commands.retriever.RetrieverCommand;
import com.phonepe.commons.columbus.test.annotations.WithElasticClient;
import com.phonepe.commons.columbus.test.annotations.WithElasticToolkit;
import com.phonepe.commons.columbus.test.annotations.WithMockito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithMockito
@WithElasticClient.All
@WithElasticToolkit.All
public class RetrieverCommandTest {

    @Test
    @DisplayName("Retriever")
    void testRetrieverCommand(final StorageClient storage) {
        final Retriever standardRetriever = StandardRetriever.builder()
                .query(TermQuery.builder()
                        .field("title")
                        .value("Pincode")
                        .build())
                .minScore(2.0f)
                .build();

        final Retriever knnRetriever = KnnRetriever.builder()
                .field("embeddings")
                .queryVector(List.of(1.0f, 2.0f, 3.0f))
                .k(5)
                .numCandidates(10)
                .build();

        final Retriever rffRetriever = RrfRetriever.builder()
                .rankConstant(10)
                .retriever(standardRetriever)
                .retriever(knnRetriever)
                .build();

        assertEquals(standardRetriever.getType(), StandardRetriever.TYPE);
        assertEquals(knnRetriever.getType(), KnnRetriever.TYPE);
        assertEquals(rffRetriever.getType(), RrfRetriever.TYPE);

        final var command = RetrieverCommand.forType(EntityHelpers.ENTITY_TYPE)
                .indices(List.of(IndexName.builder().name("test-index").build()))
                .retriever(rffRetriever)
                .build();

        assertThrows(UnsupportedOperationException.class, () -> command.execute(storage), "must throw on client failure");
    }
}
