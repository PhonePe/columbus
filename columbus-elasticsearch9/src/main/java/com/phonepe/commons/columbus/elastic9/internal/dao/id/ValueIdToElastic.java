package com.phonepe.commons.columbus.elastic9.internal.dao.id;

import com.phonepe.commons.columbus.dao.id.ValueId;
import com.phonepe.commons.columbus.elastic9.dao.id.ElasticId;
import com.phonepe.commons.columbus.elastic9.dao.id.ElasticIdResolutionContext;
import com.phonepe.commons.columbus.elastic9.dao.id.IdToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ValueId.TYPE, type = ValueId.class)
public class ValueIdToElastic<C extends ElasticIdResolutionContext<C>> implements IdToElastic<ValueId, C> {

    @Override
    public ElasticId visit(final ValueId id, final Object props, final C context) {
        return ElasticId.builder()
                .id(id.getValue())
                .build();
    }
}
