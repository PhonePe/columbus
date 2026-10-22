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

import lombok.experimental.UtilityClass;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A collection of helpers to create iterators, iterables and streams.
 */
@UtilityClass
public class Iterators {

    public <T> Iterator<List<T>> partitioning(final Iterator<T> iterator, final int size) {
        return new PartitioningIterator<>(iterator, validateSize(size));
    }

    public <T> Iterator<List<T>> repartitioning(final Iterator<List<T>> iterator, final int size) {
        return new RepartitioningIterator<>(iterator, validateSize(size));
    }

    public <T> Iterator<T> flattening(final Iterator<List<T>> iterator) {
        return new FlatteningIterator<>(iterator);
    }

    public <T> Stream<List<T>> partitioning(final Stream<T> stream, final int size) {
        return partitioningStream(stream.iterator(), size);
    }

    public <T> Stream<List<T>> repartitioning(final Stream<List<T>> stream, final int size) {
        return repartitioningStream(stream.iterator(), size);
    }

    public <T> Stream<List<T>> partitioningStream(final Iterator<T> iterator, final int size) {
        return stream(partitioning(iterator, size));
    }

    public <T> Stream<List<T>> repartitioningStream(final Iterator<List<T>> iterator, final int size) {
        return stream(repartitioning(iterator, size));
    }

    public <T> Stream<T> stream(final Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    private int validateSize(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive");
        }
        return size;
    }
}
