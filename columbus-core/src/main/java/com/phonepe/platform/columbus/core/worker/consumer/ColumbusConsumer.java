package com.phonepe.platform.columbus.core.worker.consumer;

import java.io.Closeable;

public interface ColumbusConsumer<M, O> extends Runnable, Closeable {
    String name();

    O run(M input);
}
