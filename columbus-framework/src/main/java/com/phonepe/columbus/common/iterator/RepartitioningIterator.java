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
package com.phonepe.columbus.common.iterator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Re-partitions an iterator of lists into sublists of the given size. The last list could contain fewer elements.
 * The order of the elements is retained.
 * <p>
 * Repartitioning could result in the elements from multiple lists being returned in a single list, and the elements
 * of a single list being returned across different lists.
 *
 * @param <T> the type of the elements.
 */
@RequiredArgsConstructor
class RepartitioningIterator<T> implements Iterator<List<T>> {

    @NonNull
    private final Iterator<List<T>> iterator;
    private final int size;
    private List<T> last;

    @Override
    public boolean hasNext() {
        while (last == null || last.isEmpty()) {
            if (!iterator.hasNext()) {
                return false;
            }
            last = iterator.next();
        }
        return true;
    }

    @Override
    public List<T> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final var partition = new ArrayList<T>(size);
        // Using < instead of <= to avoid consuming empty lists when the partition is full
        while (partition.size() + last.size() < size) {
            partition.addAll(last);
            if (!iterator.hasNext()) {
                last = null;
                return partition;
            }
            last = iterator.next();
        }
        final int remaining = size - partition.size();
        partition.addAll(last.subList(0, remaining));
        last = last.subList(remaining, last.size());
        return partition;
    }
}
