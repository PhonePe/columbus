package com.phonepe.commons.columbus.elastic.internal.dao.index;

import com.phonepe.commons.columbus.dao.index.IndexReference;
import com.phonepe.commons.columbus.elastic.dao.index.ElasticIndex;
import com.phonepe.commons.columbus.elastic.dao.index.ElasticIndexResolutionContext;
import com.phonepe.commons.columbus.elastic.dao.index.IndexToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

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
