package com.phonepe.columbus.elastic9.internal.dao.index;

import com.phonepe.columbus.dao.index.IndexReference;
import com.phonepe.columbus.elastic9.dao.index.ElasticIndex;
import com.phonepe.columbus.elastic9.dao.index.ElasticIndexResolutionContext;
import com.phonepe.columbus.elastic9.dao.index.IndexToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = IndexReference.TYPE, type = IndexReference.class)
public class IndexReferenceToElastic<C extends ElasticIndexResolutionContext<C>>
        implements IndexToElastic<IndexReference, C> {

    @Override
    public ElasticIndex visit(final IndexReference index, final Object props, final C context) {
        final var key = index.getKey();
        return context.indexRegistry().get(key)
                .map(name -> ElasticIndex.builder()
                        .name(name)
                        .build())
                .orElseThrow(() -> new IllegalArgumentException("No index registered for '%s'".formatted(key)));
    }
}
