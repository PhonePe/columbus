package com.phonepe.columbus.common.iterator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Partitions an iterator into sublists of the given size. The last list could contain fewer elements. The order of
 * the elements is retained.
 *
 * @param <T> the type of the elements of the iterator.
 */
@RequiredArgsConstructor
class PartitioningIterator<T> implements Iterator<List<T>> {

    @NonNull
    private final Iterator<T> iterator;
    private final int size;

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public List<T> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final var partition = new ArrayList<T>(size);
        while (iterator.hasNext() && partition.size() < size) {
            partition.add(iterator.next());
        }
        return partition;
    }
}
