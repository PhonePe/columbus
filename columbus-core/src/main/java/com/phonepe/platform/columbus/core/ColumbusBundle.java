package com.phonepe.platform.columbus.core;

import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.exception.ColumbusExceptionMapper;
import com.phonepe.platform.columbus.core.feature.ColumbusBaseFeature;
import com.phonepe.platform.columbus.core.feature.ColumbusFeature;
import com.phonepe.platform.columbus.core.feature.core.ColumbusCoreFeature;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusErrorCode;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.Builder;
import lombok.Singular;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@Builder
public class ColumbusBundle<T extends Configuration> implements ConfiguredBundle<T> {
    @Singular
    @Size(min = 1)
    private final List<ColumbusFeature<T>> features;

    public static <C extends Configuration> ColumbusBundleBuilder<C> builder(ColumbusCoreFeature.CoreFeatureOptions<C> options) {
        return new ColumbusBundle.ColumbusBundleBuilder<C>()
            .options(options);
    }

    public static <C extends Configuration> ColumbusBundleBuilder<C> builder(ColumbusCoreFeature<C> coreFeature) {
        return new ColumbusBundle.ColumbusBundleBuilder<C>()
            .feature(coreFeature);
    }

    public <F extends ColumbusFeature<T>> Optional<F> getFeatureIfExists(Class<F> featureClass) {
        return features.stream()
            .filter(featureClass::isInstance)
            .map(featureClass::cast)
            .findFirst();
    }

    public <F extends ColumbusFeature<T>> F getFeature(Class<F> featureClass) {
        return getFeatureIfExists(featureClass).orElseThrow(
            () -> ColumbusException.error(ColumbusErrorCode.FEATURE_NOT_FOUND,
                "Feature " + featureClass + " not found"
            ));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Bootstrap<?> bootstrap) {
        features.forEach(feature -> feature.initialize(this, (Bootstrap<T>) bootstrap));
    }

    @Override
    public void run(T configuration, Environment environment) {
        MapperUtils.initialise(environment.getObjectMapper());
        environment.jersey().register(new ColumbusExceptionMapper());
        features.forEach(feature -> feature.run(configuration, environment));
    }

    public static class ColumbusBundleBuilder<T extends Configuration> {
        public ColumbusBundleBuilder<T> options(ColumbusBaseFeature.Options<T> options) {
            return feature(options.toFeature());
        }
    }
}
