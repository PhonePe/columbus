package com.phonepe.platform.columbus.elastic.internal.dao.index;

import com.phonepe.platform.columbus.dao.index.IndexReference;
import com.phonepe.platform.columbus.elastic.dao.index.ElasticIndex;
import com.phonepe.platform.columbus.elastic.dao.index.ElasticIndexResolutionContext;
import com.phonepe.platform.columbus.elastic.dao.index.IndexToElastic;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

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
