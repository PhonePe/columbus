package com.phonepe.platform.columbus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumbusConfiguration {

    @Builder.Default
    private final boolean enabled = true;
}
