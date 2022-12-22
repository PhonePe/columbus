package com.phonepe.platform.columbus.core.feature.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grookage.jelastic.core.config.JElasticConfiguration;
import com.phonepe.platform.columbus.core.cache.CacheConfig;
import com.phonepe.platform.columbus.core.models.worker.ColumbusWorkerConfig;
import com.phonepe.platform.columbus.core.search.resolver.models.FilterEntry;
import com.phonepe.platform.columbus.core.search.resolver.models.SorterEntry;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumbusCoreConfig {
    @Valid
    @NotNull
    private JElasticConfiguration elasticConfiguration;

    @Valid
    private ColumbusWorkerConfig workerConfig;

    @Valid
    private Map<ColumbusTenantType, CacheConfig> tenantTypeCacheConfigMap;
    @Valid
    private Map<String, FilterEntry> storedFilters;

    @Valid
    private Map<String, SorterEntry> storedSorters;
}
