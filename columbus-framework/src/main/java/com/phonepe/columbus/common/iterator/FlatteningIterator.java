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
