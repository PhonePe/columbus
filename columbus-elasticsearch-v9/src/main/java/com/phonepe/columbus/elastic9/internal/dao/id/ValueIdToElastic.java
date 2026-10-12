package com.phonepe.columbus.elastic9.internal.dao.id;

import com.phonepe.columbus.dao.id.ValueId;
import com.phonepe.columbus.elastic9.dao.id.ElasticId;
import com.phonepe.columbus.elastic9.dao.id.ElasticIdResolutionContext;
import com.phonepe.columbus.elastic9.dao.id.IdToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ValueId.TYPE, type = ValueId.class)
public class ValueIdToElastic<C extends ElasticIdResolutionContext<C>> implements IdToElastic<ValueId, C> {

    @Override
    public ElasticId visit(final ValueId id, final Object props, final C context) {
        return ElasticId.builder()
                .id(id.getValue())
                .build();
    }
}
