package com.phonepe.commons.columbus.elastic.internal.search.utils;

import com.phonepe.commons.columbus.common.iterator.Iterators;
import com.phonepe.commons.columbus.elastic.storage.ElasticClient;
import com.phonepe.commons.columbus.storage.commands.search.ScanCommand;
import com.phonepe.commons.columbus.storage.commands.search.ScanCommand.Scanner;
import com.phonepe.commons.columbus.storage.models.result.StoredEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;

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
