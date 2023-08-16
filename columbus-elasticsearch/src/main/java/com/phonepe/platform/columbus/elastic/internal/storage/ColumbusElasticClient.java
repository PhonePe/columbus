package com.phonepe.platform.columbus.elastic.internal.storage;

import com.phonepe.platform.columbus.elastic.storage.ElasticClient;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * A concrete implementation of {@link ElasticClient}.
 */
@SuperBuilder(toBuilder = true)
public final class ColumbusElasticClient extends ElasticClient<ColumbusElasticClient> {

    @Override
    protected @NonNull ColumbusElasticClient self() {
        return this;
    }
}
