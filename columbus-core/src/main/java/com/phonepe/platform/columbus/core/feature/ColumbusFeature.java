package com.phonepe.platform.columbus.core.feature;

import com.phonepe.platform.columbus.core.ColumbusBundle;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public interface ColumbusFeature<T extends Configuration> {
    void initialize(ColumbusBundle<T> bundle, Bootstrap<T> bootstrap);

    void run(T configuration, Environment environment);
}
