package com.phonepe.platform.columbus.elastic.internal.dao.id;

import com.phonepe.platform.columbus.dao.id.AutoId;
import com.phonepe.platform.columbus.elastic.dao.id.ElasticId;
import com.phonepe.platform.columbus.elastic.dao.id.ElasticIdResolutionContext;
import com.phonepe.platform.columbus.elastic.dao.id.IdToElastic;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

@RegistryKey(name = AutoId.TYPE, type = AutoId.class)
public class AutoIdToElastic<C extends ElasticIdResolutionContext<C>> implements IdToElastic<AutoId, C> {

    @Override
    public ElasticId visit(final AutoId id, final Object props, final C context) {
        return ElasticId.builder().build();
    }
}
