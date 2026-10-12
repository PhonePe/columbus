package com.phonepe.columbus.elastic9.internal.dao.id;

import com.phonepe.columbus.dao.id.AutoId;
import com.phonepe.columbus.elastic9.dao.id.ElasticId;
import com.phonepe.columbus.elastic9.dao.id.ElasticIdResolutionContext;
import com.phonepe.columbus.elastic9.dao.id.IdToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = AutoId.TYPE, type = AutoId.class)
public class AutoIdToElastic<C extends ElasticIdResolutionContext<C>> implements IdToElastic<AutoId, C> {

    @Override
    public ElasticId visit(final AutoId id, final Object props, final C context) {
        return ElasticId.builder().build();
    }
}
