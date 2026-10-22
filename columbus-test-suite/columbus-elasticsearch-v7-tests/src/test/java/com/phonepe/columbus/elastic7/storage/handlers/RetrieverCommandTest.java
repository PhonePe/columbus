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
package com.phonepe.columbus.elastic7.storage.handlers;

import com.phonepe.columbus.dao.index.IndexName;
import com.phonepe.columbus.search.query.term.TermQuery;
import com.phonepe.columbus.search.retriever.KnnRetriever;
import com.phonepe.columbus.search.retriever.Retriever;
import com.phonepe.columbus.search.retriever.RrfRetriever;
import com.phonepe.columbus.search.retriever.StandardRetriever;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.commands.retriever.RetrieverCommand;
import com.phonepe.columbus.elastic7.test.annotations.WithElasticClient;
import com.phonepe.columbus.elastic7.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.test.annotations.WithMockito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithMockito
@WithElasticClient.All
@WithElasticToolkit.All
class RetrieverCommandTest {

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
                .queryVector(List.of(1.0f, 2.0f, 3.0f))
                .k(5)
                .numCandidates(10)
                .build();

        final Retriever rffRetriever = RrfRetriever.builder()
                .rankConstant(10)
                .retriever(standardRetriever)
                .retriever(knnRetriever)
                .build();

        assertEquals(StandardRetriever.TYPE, standardRetriever.getType());
        assertEquals(KnnRetriever.TYPE, knnRetriever.getType());
        assertEquals(RrfRetriever.TYPE, rffRetriever.getType());

        final var command = RetrieverCommand.forType(EntityHelpers.ENTITY_TYPE)
                .indices(List.of(IndexName.builder().name("test-index").build()))
                .retriever(rffRetriever)
                .build();

        assertThrows(UnsupportedOperationException.class, () -> command.execute(storage), "must throw on client failure");
    }
}
