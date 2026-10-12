package com.phonepe.columbus.core.toolkit;

import com.phonepe.columbus.reflect.ReflectFactory;
import com.phonepe.columbus.reflect.ReflectSettings;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * A set of parameters to configure {@link Toolkit} and its fields as needed.
 */
@Data
@SuperBuilder(toBuilder = true)
public class ToolkitSettings {

    private final ReflectSettings reflectSettings;

    private final ReflectFactory reflectFactory;

    public static ToolkitSettings defaults() {
        return builder().build();
    }
}
