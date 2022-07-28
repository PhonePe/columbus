package com.phonepe.platform.columbus.core.feature;

import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

public abstract class ColumbusConfiguredFeature<T extends Configuration, C, O extends ColumbusConfiguredFeature.ConfiguredFeatureOptions<T, C, O>> extends ColumbusBaseFeature<T, O> {
    @NonNull
    private final ConfigGetter<T, C> configGetter;

    protected ColumbusConfiguredFeature(O options) {
        super(options);
        configGetter = options.getConfigGetter();
    }

    @Override
    public void run(T configuration, Environment environment) {
        runWith(getConfiguration(configuration), environment);
    }

    protected C getConfiguration(T configuration) {
        return configGetter.getConfig(configuration);
    }

    protected abstract void runWith(C configuration, Environment environment);

    @Getter
    @SuperBuilder
    public abstract static class ConfiguredFeatureOptions<T extends Configuration, C, O extends ConfiguredFeatureOptions<T, C, O>> implements ColumbusBaseFeature.Options<T> {
        @NonNull
        private final ConfigGetter<T, C> configGetter;

        @Override
        public abstract ColumbusConfiguredFeature<T, C, O> toFeature();
    }
}
