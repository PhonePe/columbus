package com.phonepe.columbus.elastic9.internal.storage;

import com.phonepe.columbus.elastic9.storage.ElasticClient;
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
