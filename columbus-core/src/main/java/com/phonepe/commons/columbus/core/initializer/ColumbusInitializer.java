package com.phonepe.commons.columbus.core.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.commons.columbus.Columbus;
import com.phonepe.commons.columbus.core.settings.ColumbusConfiguration;
import com.phonepe.commons.columbus.core.settings.ColumbusSettings;
import com.phonepe.commons.columbus.core.toolkit.Toolkit;

/**
 * An interface to initialize {@link Columbus} using the different, possibly optional, attributes it needs.
 *
 * @implSpec The attributes are provided as and when they are available, and the object is expected to be created
 * lazily on calling {@link #get()}.
 * @implNote Most likely, an instance of this class can only be used once due to the stateful nature.
 */
public interface ColumbusInitializer {

    void use(ColumbusSettings settings);

    void use(ObjectMapper mapper);

    void use(ColumbusConfiguration configuration);

    Toolkit toolkit();

    Columbus get();
}
