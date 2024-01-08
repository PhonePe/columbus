package com.phonepe.commons.columbus.elastic.internal.dao.index;

import com.phonepe.commons.columbus.dao.index.IndexName;
import com.phonepe.commons.columbus.elastic.dao.index.ElasticIndex;
import com.phonepe.commons.columbus.elastic.dao.index.ElasticIndexResolutionContext;
import com.phonepe.commons.columbus.elastic.dao.index.IndexToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

@RegistryKey(name = IndexName.TYPE, type = IndexName.class)
public class IndexNameToElastic<C extends ElasticIndexResolutionContext<C>> implements IndexToElastic<IndexName, C> {

    @Override
    public ElasticIndex visit(final IndexName index, final Object props, final C context) {
        return ElasticIndex.builder()
                .name(index.getName())
                .build();
    }
}
