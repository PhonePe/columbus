package com.phonepe.platform.columbus.core.worker.producer;

import java.io.Closeable;

public interface ColumbusProducer<K, M, O> extends Closeable {
    O publish(K key, M message);
}
