package com.phonepe.platform.columbus.elastic.internal.dao.id;

import com.phonepe.platform.columbus.dao.id.ValueId;
import com.phonepe.platform.columbus.elastic.dao.id.ElasticId;
import com.phonepe.platform.columbus.elastic.dao.id.ElasticIdResolutionContext;
import com.phonepe.platform.columbus.elastic.dao.id.IdToElastic;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ValueId.TYPE, type = ValueId.class)
public class ValueIdToElastic<C extends ElasticIdResolutionContext<C>> implements IdToElastic<ValueId, C> {

    @Override
    public ElasticId visit(final ValueId id, final C context) {
        return ElasticId.builder()
                .id(id.getValue())
                .build();
    }
}
