package com.phonepe.platform.columbus.core;

import com.phonepe.platform.columbus.core.feature.core.ColumbusCoreConfig;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
public class TestApplicationConfiguration extends Configuration {
    @Valid
    ColumbusCoreConfig columbusCoreConfig;
}
