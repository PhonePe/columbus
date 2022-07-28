package com.phonepe.platform.columbus.search;

import com.phonepe.platform.columbus.core.ColumbusBundle;
import com.phonepe.platform.columbus.core.constants.ColumbusConstants;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.feature.ColumbusBaseFeature;
import com.phonepe.platform.columbus.core.feature.core.ColumbusCoreFeature;
import com.phonepe.platform.columbus.core.search.SearchRegistry;
import com.phonepe.platform.columbus.core.search.preprocessor.Preprocessor;
import com.phonepe.platform.columbus.core.utils.ReflectionUtils;
import com.phonepe.platform.columbus.core.visitors.Visitor;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusErrorCode;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ColumbusSearchFeature<T extends Configuration> extends ColumbusBaseFeature<T, ColumbusSearchFeature.SearchOptions<T>> {
    private SearchRegistry searchRegistry;

    protected ColumbusSearchFeature(SearchOptions<T> options) {
        super(options);
    }

    @Override
    public void run(T configuration, Environment environment) {
        // Nothing at run time
    }

    @Override
    public void initialize(ColumbusBundle<T> bundle, Bootstrap<T> bootstrap) {
        //noinspection unchecked
        searchRegistry = bundle.getFeature(ColumbusCoreFeature.class).getSearchRegistry();
        if (searchRegistry == null) {
            throw ColumbusException.error(ColumbusErrorCode.FEATURE_NOT_INITIALIZED, "Core feature is not initialized");
        }
        registerSearchEntities();
    }

    @SuppressWarnings("java:S1452") // SonarQube fix
    public static <C extends Configuration> SearchOptions.SearchOptionsBuilder<C, ?, ?> configure() {
        return SearchOptions.builder();
    }

    private void registerSearchEntities() {
        SearchOptions<T> options = getOptions();
        searchRegistry.registerFilters(getEntities(Filter.class, options.getFilters(), options.getFilterPackages()));
        searchRegistry.registerSorters(getEntities(Sorter.class, options.getSorters(), options.getSorterPackages()));
        searchRegistry.registerPreprocessors(getEntities(Preprocessor.class, options.getPreprocessors(), options.getPreprocessorPackages()));
        searchRegistry.register(new ArrayList<>(getEntities(Visitor.class, options.getVisitors(), options.getVisitorPackages())));
    }

    private <B> Collection<Class<? extends B>> getEntities(
        Class<B> base,
        List<Class<? extends B>> classes,
        List<String> packages
    ) {
        List<String> commonPackages = getOptions().getCommonPackages();
        Set<Class<? extends B>> entities = new HashSet<>(classes);
        if (!packages.isEmpty() || !commonPackages.isEmpty()) {
            Reflections reflections = new Reflections(ColumbusConstants.BASE_PACKAGE, packages, commonPackages);
            entities.addAll(ReflectionUtils.getJsonSubtypes(reflections, base)
                .stream()
                .filter(cls -> !cls.getPackage().getName().startsWith(ColumbusConstants.BASE_PACKAGE))
                .collect(Collectors.toSet()));
        }
        log.info("[{}]: Registering {}", base.getSimpleName(), entities);
        return entities;
    }

    @Getter
    @SuperBuilder
    public static class SearchOptions<T extends Configuration> implements ColumbusBaseFeature.Options<T> {
        @Singular
        private final List<Class<? extends Filter>> filters;
        @Singular
        private final List<String> filterPackages;

        @Singular
        private final List<Class<? extends Sorter>> sorters;
        @Singular
        private final List<String> sorterPackages;

        @Singular
        private final List<Class<? extends Preprocessor>> preprocessors;
        @Singular
        private final List<String> preprocessorPackages;

        @Singular
        @SuppressWarnings("rawtypes")
        private final List<Class<? extends Visitor>> visitors;
        @Singular
        private final List<String> visitorPackages;

        @Singular
        private final List<String> commonPackages;

        @Override
        public ColumbusSearchFeature<T> toFeature() {
            return new ColumbusSearchFeature<>(this);
        }
    }
}
