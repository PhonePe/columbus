package com.phonepe.platform.columbus.core.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CacheConfig {
    public static final int DEFAULT_EXPIRY = 604800; // 7 days
    public static final int DEFAULT_REFRESH = 21600; // 6hours
    public static final int DEFAULT_MAX_ELEMENTS = 50000;

    @Min(30)
    @Max(1204800)
    @Builder.Default
    private int expiryInSeconds = DEFAULT_EXPIRY;

    @Min(30)
    @Max(86400)
    @Builder.Default
    private int refreshInSeconds = DEFAULT_REFRESH;

    @Min(10)
    @Max(1048576)
    @Builder.Default
    private int maxElements = DEFAULT_MAX_ELEMENTS;
}