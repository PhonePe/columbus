package com.phonepe.platform.columbus.resources;

import com.phonepe.platform.columbus.core.ColumbusBundle;
import com.phonepe.platform.columbus.core.feature.ColumbusBaseFeature;
import com.phonepe.platform.columbus.core.feature.core.ColumbusCoreFeature;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.experimental.SuperBuilder;

public class ColumbusResourceFeature<T extends Configuration> extends ColumbusBaseFeature<T, ColumbusResourceFeature.ResourceOptions<T>> {
    private ColumbusCoreFeature<T> coreFeature;

    protected ColumbusResourceFeature(ResourceOptions<T> options) {
        super(options);
    }

    @SuppressWarnings("java:S1452") // SonarQube fix
    public static <C extends Configuration> ResourceOptions.ResourceOptionsBuilder<C, ?, ?> configure() {
        return ResourceOptions.builder();
    }

    @Override
    public void run(T configuration, Environment environment) {
        // register param converter provider
        registerParamConverterProvider(environment);
        // register category resource
        registerResources(environment);
    }

    private void registerResources(Environment environment) {
        environment.jersey().register(new CategoryResource(
            coreFeature.getCategoryService(),
            coreFeature.getTenantNames()
        ));
    }

    private void registerParamConverterProvider(Environment environment) {
        environment.jersey().register(new ColumbusTenantTypeParamConverterProvider(
            environment.getObjectMapper(),
            coreFeature.getEntity()
        ));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(ColumbusBundle<T> bundle, Bootstrap<T> bootstrap) {
        coreFeature = bundle.getFeature(ColumbusCoreFeature.class);
    }

    @SuperBuilder
    public static class ResourceOptions<T extends Configuration> implements ColumbusBaseFeature.Options<T> {
        @Override
        public ColumbusResourceFeature<T> toFeature() {
            return new ColumbusResourceFeature<>(this);
        }
    }
}
