package com.phonepe.platform.columbus.core.visitors.impl;

import com.phonepe.platform.columbus.core.visitors.Visitor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class ElementStreamVisitor<T extends B, B, C extends ElementStreamContext<B, C>>
    implements Visitor<Stream<B>, T, B, C> {
    @SafeVarargs
    protected final Stream<B> of(B... values) {
        return Stream.of(values);
    }

    protected final Stream<B> valueAndRest(T value, List<Stream<B>> rest) {
        return valueAndRest(value, rest.stream());
    }

    protected final Stream<B> valueAndRest(T value, Stream<Stream<B>> rest) {
        return valueAndOther(value, rest.flatMap(Function.identity()));
    }

    protected final Stream<B> valueAndOther(T value, Stream<B> rest) {
        return Stream.concat(of(value), rest);
    }
}
