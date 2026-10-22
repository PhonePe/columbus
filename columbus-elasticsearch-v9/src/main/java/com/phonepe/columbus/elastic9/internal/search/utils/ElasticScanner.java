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
package com.phonepe.columbus.elastic9.internal.search.utils;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.phonepe.columbus.common.iterator.Iterators;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.storage.commands.search.ScanCommand;
import com.phonepe.columbus.storage.commands.search.ScanCommand.Scanner;
import com.phonepe.columbus.storage.models.result.StoredEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class ElasticScanner<T, E extends ElasticClient<E>> implements Scanner<T> {

    @NonNull
    private final SearchRequest searchRequest;
    @NonNull
    private final ScanCommand<T> command;
    @NonNull
    private final E storage;

    @Override
    public Iterator<StoredEntity<T>> iterate() {
        return Iterators.flattening(createIterator());
    }

    @Override
    public Iterator<List<StoredEntity<T>>> iterateBatch(final int batchSize) {
        final var iterator = createIterator();
        return batchSize == command.getBatchSize() ? iterator : Iterators.repartitioning(iterator, batchSize);
    }

    @Override
    public Stream<StoredEntity<T>> stream() {
        return Iterators.stream(createIterator()).flatMap(Collection::stream);
    }

    @Override
    public Stream<List<StoredEntity<T>>> streamBatch(final int batchSize) {
        return Iterators.repartitioningStream(createIterator(), batchSize);
    }

    private Iterator<List<StoredEntity<T>>> createIterator() {
        return new StoredEntityBatchIterator<>(searchRequest, command, storage);
    }
}
