package com.phonepe.platform.columbus.core.toolkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.internal.core.toolkit.DefaultToolkitInitializer;
import lombok.experimental.UtilityClass;

/**
 * A collection of helper methods to create different instances required to create a {@link Toolkit}.
 */
@UtilityClass
public class ToolkitHelpers {

    public ToolkitInitializer initializer() {
        return new DefaultToolkitInitializer();
    }

    public Toolkit defaultInstance() {
        final var initializer = initializer();
        initializer.use(new ObjectMapper());
        initializer.use(ToolkitSettings.defaults());
        return initializer.get();
    }
}
