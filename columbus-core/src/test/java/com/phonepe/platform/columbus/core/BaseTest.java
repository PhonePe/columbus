package com.phonepe.platform.columbus.core;


import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.phonepe.platform.columbus.core.feature.core.ColumbusCoreFeature;
import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class BaseTest {
    private static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:7.3.0";
    private static final String KAFKA_IMAGE = "confluentinc/cp-kafka:6.2.1";

    protected static final ElasticsearchContainer elasticsearchContainer;
    protected static final KafkaContainer kafkaContainer;
    protected static TestApplicationConfiguration testApplicationConfiguration;

    protected static ColumbusBundle<TestApplicationConfiguration> columbusBundle;

    protected static ColumbusCoreFeature<TestApplicationConfiguration> columbusCoreFeature;

    static AdminClient adminClient;

    static {
        elasticsearchContainer = new ElasticsearchContainer(ELASTICSEARCH_IMAGE);
        elasticsearchContainer.start();

        kafkaContainer = new KafkaContainer(DockerImageName.parse(KAFKA_IMAGE));
        kafkaContainer.start();

        adminClient = AdminClient.create(Map.of(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
            kafkaContainer.getBootstrapServers()
        ));

        Collection<NewTopic> topics = List.of(new NewTopic("test_tenant", 1, (short) 1));
        try {
            adminClient.createTopics(topics).all().get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    @SneakyThrows
    static void setup() {
        final var bootstrap = new Bootstrap<TestApplicationConfiguration>(null);
        testApplicationConfiguration = setupTestConfiguration();

        final var environment = new Environment(
            "Test-Application",
            bootstrap.getObjectMapper(),
            bootstrap.getValidatorFactory(),
            bootstrap.getMetricRegistry(),
            bootstrap.getClassLoader(),
            bootstrap.getHealthCheckRegistry(),
            testApplicationConfiguration
        );

        columbusBundle = getColumbusBundle();
        bootstrap.addBundle(columbusBundle);
        bootstrap.run(testApplicationConfiguration, environment);
        //noinspection unchecked
        columbusCoreFeature = columbusBundle.getFeature(ColumbusCoreFeature.class);
    }

    static ColumbusBundle<TestApplicationConfiguration> getColumbusBundle() {
        return ColumbusBundle.builder(ColumbusCoreFeature.<TestApplicationConfiguration>configure()
            .entity(TestTenant.class)
            .configGetter(config -> testApplicationConfiguration.getColumbusCoreConfig())
            .build()).build();
    }

    @SneakyThrows
    public static TestApplicationConfiguration setupTestConfiguration() {
        final var inputStream = BaseTest.class.getClassLoader()
            .getResourceAsStream("config/testapp-config.yml");
        Objects.requireNonNull(inputStream);
        final var configString = IOUtils.toString(inputStream, String.valueOf(StandardCharsets.UTF_8));
        final var replacedConfigString = configString
            .replaceAll("@@elastic.host@@", elasticsearchContainer.getContainerIpAddress())
            .replaceAll("@@elastic.port@@", Integer.toString(elasticsearchContainer.getFirstMappedPort()))
            .replaceAll("@@kafka.bootstrap@@", kafkaContainer.getBootstrapServers());
        final var mapper = Jackson.newObjectMapper(new YAMLFactory());
        System.out.println("************");
        System.out.println(replacedConfigString);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addKeyDeserializer(ColumbusTenantType.class, new TenantTypeKeyDeserializer());
        mapper.registerModule(simpleModule);
        return mapper.readValue(replacedConfigString, TestApplicationConfiguration.class);
    }

    @AfterAll
    @SneakyThrows
    static void tearDown() {
        var response = adminClient.deleteTopics(List.of("test_tenant"));

        // TODO: replace with .get() which waits internally?
        //noinspection StatementWithEmptyBody
        while (!response.all().isDone()) {
        }
    }
}
