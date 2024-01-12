package com.phonepe.commons.columbus.common.iterator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Flattens an iterator of lists to return the individual elements of each list.
 *
 * @param <T> the type of the elements.
 */
@RequiredArgsConstructor
class FlatteningIterator<T> implements Iterator<T> {

    @NonNull
    private final Iterator<List<T>> iterator;
    private Iterator<T> last;

    @Override
    public boolean hasNext() {
        while (last == null || !last.hasNext()) {
            if (!iterator.hasNext()) {
                return false;
            }
            last = iterator.next().iterator();
        }
        return true;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return last.next();
    }
}
