package com.phonepe.columbus.elastic9.dao.index;

import com.phonepe.columbus.dao.index.visitor.IndexResolutionContext;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.registry.Registry;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * The context used to resolve {@link com.phonepe.columbus.dao.index.Index Indices} as needed by ElasticSearch.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticIndexResolutionContext<C extends ElasticIndexResolutionContext<C>>
        extends IndexResolutionContext<ElasticIndex, Object, C> {

    @NonNull
    @Builder.Default
    private final Registry<String, String> indexRegistry = Registries.empty();
}
