package com.phonepe.platform.columbus.core.cache.category;

import com.phonepe.platform.columbus.core.cache.ColumbusCacheKey;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "forTenant")
public class ColumbusCategoryCacheKey implements ColumbusCacheKey<String> {
    private final ColumbusTenantType tenantType;

    @Override
    public String getKey() {
        return tenantType.name();
    }
}
