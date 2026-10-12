package com.phonepe.columbus.elastic7.storage.factory;

import com.phonepe.columbus.elastic7.storage.ElasticClient;
import io.dropwizard.setup.Environment;

/**
 * An interface that uses {@link Environment} to initialize {@link ElasticClient}.
 *
 * @see ElasticClientInitializer
 */
public interface DropwizardElasticClientInitializer extends ElasticClientInitializer {

    void use(Environment environment);
}
