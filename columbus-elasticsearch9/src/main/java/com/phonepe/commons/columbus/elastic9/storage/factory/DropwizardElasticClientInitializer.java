package com.phonepe.commons.columbus.elastic9.storage.factory;

import com.phonepe.commons.columbus.elastic9.storage.ElasticClient;
import io.dropwizard.setup.Environment;

/**
 * An interface that uses {@link Environment} to initialize {@link ElasticClient}.
 *
 * @see ElasticClientInitializer
 */
public interface DropwizardElasticClientInitializer extends ElasticClientInitializer {

    void use(Environment environment);
}
