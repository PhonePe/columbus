package com.phonepe.commons.columbus.search.algorithms.common.distance;

import lombok.NonNull;

public interface Distance<T> {

    double distance(@NonNull T from, @NonNull T to);

    default double distance(@NonNull T from, @NonNull T to, double max) {
        return Math.min(distance(from, to), max);
    }
}
