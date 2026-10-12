package com.phonepe.columbus.elastic7.internal.dao.index;

import com.phonepe.columbus.dao.index.IndexName;
import com.phonepe.columbus.elastic7.dao.index.ElasticIndex;
import com.phonepe.columbus.elastic7.dao.index.ElasticIndexResolutionContext;
import com.phonepe.columbus.elastic7.dao.index.IndexToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = IndexName.TYPE, type = IndexName.class)
public class IndexNameToElastic<C extends ElasticIndexResolutionContext<C>> implements IndexToElastic<IndexName, C> {

    @Override
    public ElasticIndex visit(final IndexName index, final Object props, final C context) {
        return ElasticIndex.builder()
                .name(index.getName())
                .build();
    }
}
