package com.phonepe.commons.columbus.core.toolkit;

import com.phonepe.commons.columbus.internal.core.toolkit.DefaultToolkitInitializer;
import lombok.experimental.UtilityClass;

/**
 * A collection of helper methods to create different instances required to create a {@link Toolkit}.
 */
@UtilityClass
public class ToolkitHelpers {

    public ToolkitInitializer initializer() {
        return new DefaultToolkitInitializer();
    }
}
