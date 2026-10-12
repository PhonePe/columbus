package com.phonepe.columbus.dropwizard.initializer;

import com.phonepe.columbus.core.initializer.ColumbusInitializer;
import io.dropwizard.setup.Environment;

/**
 * An interface that adds Dropwizard's {@link Environment} to the initializer of
 * {@link com.phonepe.columbus.Columbus Columbus}.
 */
public interface DropwizardInitializer extends ColumbusInitializer {

    void use(Environment environment);
}
