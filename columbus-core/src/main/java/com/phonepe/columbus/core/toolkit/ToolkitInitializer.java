package com.phonepe.columbus.core.toolkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.type.TypeRegistry;

/**
 * An interface to initialize {@link Toolkit} using the different, possibly optional, attributes it needs.
 *
 * @implSpec The attributes are provided as and when they are available, and the object is expected to be created
 * lazily on calling {@link #get()}.
 * @implNote Most likely, an instance of this class can only be used once due to the stateful nature.
 */
public interface ToolkitInitializer {

    void use(ObjectMapper mapper);

    void use(TypeRegistry typeRegistry);

    void use(ToolkitSettings settings);

    Toolkit get();
}
