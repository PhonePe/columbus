package com.phonepe.columbus;

import com.phonepe.columbus.internal.core.initializer.DefaultColumbusInitializer;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ColumbusBundleTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Environment environment;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Bootstrap<TestConfig> bootstrap;

    @Test
    @DisplayName("create and initialize bundle")
    void testBundleEnabled() {
        final var configuration = ColumbusBundleConfiguration.builder().build();
        final var testConfig = TestConfig.builder()
                .columbusBundleConfiguration(configuration)
                .build();

        final var bundle = ColumbusBundle.configure()
                .build()
                .toBundle(TestConfig::getColumbusBundleConfiguration);

        bundle.initialize(bootstrap);
        bundle.run(testConfig, environment);

        final var columbus = bundle.columbus();

        assertAll(
                "Columbus",
                () -> assertNotNull(columbus, "Columbus should be initialized"),
                () -> assertNotNull(columbus.toolkit(), "must have a valid toolkit"),
                () -> assertTrue(columbus.storages().isEmpty(), "no storages provided"));
    }

    @Test
    @DisplayName("create and initialize bundle with disabled configuration")
    void testBundleDisabled() {
        final var configuration = ColumbusBundleConfiguration.builder()
                .enabled(false)
                .build();
        final var testConfig = TestConfig.builder()
                .columbusBundleConfiguration(configuration)
                .build();

        final var bundle = ColumbusBundle.configure()
                .build()
                .toBundle(TestConfig::getColumbusBundleConfiguration);

        bundle.initialize(bootstrap);
        bundle.run(testConfig, environment);

        assertThrows(IllegalStateException.class, bundle::columbus, "Columbus must not be initialized");
    }

    @Test
    @DisplayName("create bundle with custom initializer")
    void testBundleCustom() {
        final var configuration = ColumbusBundleConfiguration.builder().build();
        final var testConfig = TestConfig.builder()
                .columbusBundleConfiguration(configuration)
                .build();

        final var bundle = ColumbusBundle.configure()
                .columbusInitializer(new DefaultColumbusInitializer())
                .build()
                .toBundle(TestConfig::getColumbusBundleConfiguration);

        bundle.initialize(bootstrap);
        bundle.run(testConfig, environment);

        final var columbus = bundle.columbus();

        assertAll(
                "Columbus",
                () -> assertNotNull(columbus, "Columbus should be initialized"),
                () -> assertNotNull(columbus.toolkit(), "must have a valid toolkit"),
                () -> assertTrue(columbus.storages().isEmpty(), "no storages provided"));
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestConfig extends Configuration {

        private ColumbusBundleConfiguration columbusBundleConfiguration;
    }
}
