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
