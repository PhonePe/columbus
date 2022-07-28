package com.phonepe.platform.columbus.core.visitors.impl;

import com.phonepe.platform.columbus.core.visitors.Context;
import lombok.experimental.SuperBuilder;

import java.util.stream.Stream;

@SuperBuilder
public abstract class ElementStreamContext<B, C extends ElementStreamContext<B, C>> extends Context<Stream<B>, B, C> {
}
