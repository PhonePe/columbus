package com.phonepe.platform.columbus.core.feature.core;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.grookage.jelastic.core.JElasticBundle;
import com.grookage.jelastic.core.config.JElasticConfiguration;
import com.grookage.jelastic.core.elastic.ElasticClient;
import com.phonepe.platform.columbus.core.ColumbusBundle;
import com.phonepe.platform.columbus.core.cache.category.ColumbusCategoryCacheRegistry;
import com.phonepe.platform.columbus.core.commands.category.CategoryEntityCommands;
import com.phonepe.platform.columbus.core.commands.category.CategoryIndexManageCommands;
import com.phonepe.platform.columbus.core.commands.unit.UnitEntityCommands;
import com.phonepe.platform.columbus.core.commands.unit.UnitIndexManageCommands;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.feature.ColumbusConfiguredFeature;
import com.phonepe.platform.columbus.core.search.SearchRegistry;
import com.phonepe.platform.columbus.core.search.preprocessor.Preprocessor;
import com.phonepe.platform.columbus.core.search.resolver.ColumbusResolver;
import com.phonepe.platform.columbus.core.search.resolver.FilterResolver;
import com.phonepe.platform.columbus.core.search.resolver.SorterResolver;
import com.phonepe.platform.columbus.core.search.resolver.impl.DefaultFilterResolver;
import com.phonepe.platform.columbus.core.search.resolver.impl.DefaultSearchQueryBuilder;
import com.phonepe.platform.columbus.core.search.resolver.impl.DefaultSorterResolver;
import com.phonepe.platform.columbus.core.search.resolver.impl.StoredFilterResolver;
import com.phonepe.platform.columbus.core.search.resolver.impl.StoredSorterResolver;
import com.phonepe.platform.columbus.core.services.CategoryService;
import com.phonepe.platform.columbus.core.services.impl.AsyncCategoryService;
import com.phonepe.platform.columbus.core.services.impl.CategoryRequestValidatorService;
import com.phonepe.platform.columbus.core.services.impl.SyncCategoryService;
import com.phonepe.platform.columbus.core.utils.ReflectionUtils;
import com.phonepe.platform.columbus.core.visitors.Visitor;
import com.phonepe.platform.columbus.core.worker.consumer.ColumbusCategoryConsumerManager;
import com.phonepe.platform.columbus.core.worker.producer.ColumbusCategoryProducerRegistry;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ColumbusCoreFeature<T extends Configuration> extends ColumbusConfiguredFeature<T, ColumbusCoreConfig, ColumbusCoreFeature.CoreFeatureOptions<T>> {
    private JElasticBundle<T> jElasticBundle;

    @Getter
    private List<ColumbusTenantType> tenantNames;

    @Getter
    private ElasticClient elasticClient;

    @Getter
    private CategoryService categoryService;

    @Getter
    private ColumbusCategoryCacheRegistry columbusCategoryCacheRegistry;

    @Getter
    private CategoryEntityCommands categoryEntityCommands;

    @Getter
    private CategoryIndexManageCommands categoryIndexManageCommands;

    @Getter
    private SearchRegistry searchRegistry;

    @Getter
    private final Class<? extends ColumbusTenantType> entity;

    @Getter
    private ColumbusResolver columbusResolver;

    @VisibleForTesting
    @Getter(AccessLevel.PACKAGE)
    private ColumbusCategoryProducerRegistry columbusCategoryProducerRegistry;

    @VisibleForTesting
    @Getter(AccessLevel.PACKAGE)
    private ColumbusCategoryConsumerManager columbusCategoryConsumerManager;

    @Getter
    private UnitIndexManageCommands unitIndexManageCommands;

    @Getter
    private UnitEntityCommands unitEntityCommands;

    public ColumbusCoreFeature(CoreFeatureOptions<T> options) {
        super(options);
        entity = options.getEntity();
    }

    @SuppressWarnings("java:S1452") // SonarQube fix
    public static <C extends Configuration> CoreFeatureOptions.CoreFeatureOptionsBuilder<C, ?, ?> configure() {
        return CoreFeatureOptions.builder();
    }

    @SneakyThrows
    private List<ColumbusTenantType> getTenantNames(Class<? extends ColumbusTenantType> entity) {
        Method method = entity.getDeclaredMethod("values");
        ColumbusTenantType[] columbusTenantTypes = (ColumbusTenantType[]) method.invoke(null);
        return List.of(columbusTenantTypes);
    }

    @Override
    public void initialize(ColumbusBundle<T> bundle, Bootstrap<T> bootstrap) {
        // register abstract type mapping for ColumbusTenantType
        registerMapping(bootstrap);

        // create tenantNames
        this.tenantNames = getTenantNames(getEntity());
        Preconditions.checkState(!this.tenantNames.isEmpty(), "Tenants list is empty.");

        initializeSearch(bootstrap.getObjectMapper());
        registerKeyDeserializer(bootstrap);
        addJelasticBundle(bootstrap);
    }

    private void registerMapping(Bootstrap<T> bootstrap) {
        bootstrap.getObjectMapper().registerModule(
            new SimpleModule("columbus").addAbstractTypeMapping(ColumbusTenantType.class, entity));
    }

    private void addJelasticBundle(Bootstrap<T> bootstrap) {
        this.jElasticBundle = new JElasticBundle<>() {

            @Override
            public JElasticConfiguration getElasticConfiguration(T configuration) {
                return getConfiguration(configuration).getElasticConfiguration();
            }
        };
        bootstrap.addBundle(jElasticBundle);
    }

    private void registerKeyDeserializer(Bootstrap<T> bootstrap) {
        bootstrap.getObjectMapper().registerModule(new SimpleModule().addKeyDeserializer(
            ColumbusTenantType.class, new TenantTypeDeserializer()));
    }

    @Override
    protected void runWith(ColumbusCoreConfig configuration, Environment environment) {
        this.elasticClient = jElasticBundle.getClient();
        var filterResolvers = ImmutableList.<FilterResolver>builder()
            .add(new DefaultFilterResolver());
        var sorterResolvers = ImmutableList.<SorterResolver>builder()
            .add(new DefaultSorterResolver());
        if (configuration.getStoredFilters() != null) {
            filterResolvers.add(new StoredFilterResolver(Map.copyOf(configuration.getStoredFilters())));
        }
        if (configuration.getStoredSorters() != null) {
            sorterResolvers.add(new StoredSorterResolver(Map.copyOf(configuration.getStoredSorters())));
        }
        columbusResolver = new ColumbusResolver(
            List.copyOf(filterResolvers.addAll(getOptions().getFilterResolvers()).build()),
            List.copyOf(sorterResolvers.addAll(getOptions().getSorterResolvers()).build())
        );

        // commands creation
        this.categoryEntityCommands = CategoryEntityCommands.builder()
            .elasticRepository(jElasticBundle.getRepository())
            .mapper(environment.getObjectMapper())
            .build();

        this.categoryIndexManageCommands = CategoryIndexManageCommands.builder()
            .elasticRepository(jElasticBundle.getRepository())
            .build();

        // category cache registry
        columbusCategoryCacheRegistry = ColumbusCategoryCacheRegistry.builder()
            .metricRegistry(environment.metrics())
            .tenantNames(tenantNames)
            .tenantTypeCacheConfigMap(configuration.getTenantTypeCacheConfigMap())
            .categoryEntityCommands(categoryEntityCommands)
            .categoryIndexManageCommands(categoryIndexManageCommands)
            .build();
        environment.lifecycle().manage(columbusCategoryCacheRegistry);

        // category service sync impl
        var syncCategoryService = new SyncCategoryService(
            categoryEntityCommands,
            categoryIndexManageCommands,
            columbusCategoryCacheRegistry
        );

        if (configuration.getWorkerConfig() == null) {
            this.categoryService = new CategoryRequestValidatorService(
                syncCategoryService,
                categoryEntityCommands,
                categoryIndexManageCommands,
                columbusCategoryCacheRegistry
            );
        } else {
            // category producer registry
            this.columbusCategoryProducerRegistry = new ColumbusCategoryProducerRegistry(
                configuration.getWorkerConfig(),
                this.tenantNames,
                environment.metrics(),
                environment.getObjectMapper()
            );
            environment.lifecycle().manage(columbusCategoryProducerRegistry);

            // category service async impl
            var asyncCategoryService = new AsyncCategoryService(syncCategoryService, columbusCategoryProducerRegistry);
            this.categoryService = new CategoryRequestValidatorService(
                asyncCategoryService,
                categoryEntityCommands,
                categoryIndexManageCommands,
                columbusCategoryCacheRegistry
            );

            // category consumer manager
            this.columbusCategoryConsumerManager = new ColumbusCategoryConsumerManager(
                configuration.getWorkerConfig(),
                this.tenantNames,
                environment.metrics(),
                environment.getObjectMapper(),
                syncCategoryService
            );
            environment.lifecycle().manage(columbusCategoryConsumerManager);
        }

        this.unitIndexManageCommands = UnitIndexManageCommands.builder()
            .elasticRepository(jElasticBundle.getRepository())
            .build();

        this.unitEntityCommands = UnitEntityCommands.builder()
            .elasticRepository(jElasticBundle.getRepository())
            .searchQueryBuilder(new DefaultSearchQueryBuilder(columbusResolver))
            .mapper(environment.getObjectMapper())
            .build();
    }

    private void initializeSearch(ObjectMapper mapper) {
        Preconditions.checkState(searchRegistry == null, "Search is being initialized more than once");
        searchRegistry = new SearchRegistry(mapper);
        searchRegistry.registerFilters(ReflectionUtils.getJsonSubtypes(Filter.class));
        searchRegistry.registerSorters(ReflectionUtils.getJsonSubtypes(Sorter.class));
        searchRegistry.registerPreprocessors(ReflectionUtils.getJsonSubtypes(Preprocessor.class));
        searchRegistry.register(new ArrayList<>(ReflectionUtils.getJsonSubtypes(Visitor.class)));
    }

    public class TenantTypeDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String s, DeserializationContext deserializationContext) {
            return tenantNames.stream()
                .filter(t -> t.name().equals(s))
                .findFirst()
                .orElseThrow(() -> ColumbusException.error(ColumbusCoreErrorCode.TENANT_TYPE_NOT_FOUND,
                    "Tenant name is incorrect"));
        }
    }

    @Getter
    @SuperBuilder
    public static class CoreFeatureOptions<T extends Configuration> extends
        ColumbusConfiguredFeature.ConfiguredFeatureOptions<T, ColumbusCoreConfig, CoreFeatureOptions<T>> {

        @NonNull
        private Class<? extends ColumbusTenantType> entity;

        @Singular
        private List<FilterResolver> filterResolvers;
        @Singular
        private List<SorterResolver> sorterResolvers;

        @Override
        public ColumbusCoreFeature<T> toFeature() {
            return new ColumbusCoreFeature<>(this);
        }
    }
}
