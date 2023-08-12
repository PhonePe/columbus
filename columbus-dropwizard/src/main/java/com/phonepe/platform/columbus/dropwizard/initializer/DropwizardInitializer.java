package com.phonepe.platform.columbus.dropwizard.initializer;

import com.phonepe.platform.columbus.core.initializer.ColumbusInitializer;
import io.dropwizard.setup.Environment;

/**
 * An interface that adds Dropwizard's {@link Environment} to the initializer of
 * {@link com.phonepe.platform.columbus.Columbus Columbus}.
 */
public interface DropwizardInitializer extends ColumbusInitializer {

    void use(Environment environment);
}
