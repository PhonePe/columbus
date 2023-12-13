package com.phonepe.platform.columbus.elastic.internal.dao.index;

import com.phonepe.platform.columbus.dao.index.IndexName;
import com.phonepe.platform.columbus.elastic.dao.index.ElasticIndex;
import com.phonepe.platform.columbus.elastic.dao.index.ElasticIndexResolutionContext;
import com.phonepe.platform.columbus.elastic.dao.index.IndexToElastic;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

@RegistryKey(name = IndexName.TYPE, type = IndexName.class)
public class IndexNameToElastic<C extends ElasticIndexResolutionContext<C>> implements IndexToElastic<IndexName, C> {

    @Override
    public ElasticIndex visit(final IndexName index, final Object props, final C context) {
        return ElasticIndex.builder()
                .name(index.getName())
                .build();
    }
}
