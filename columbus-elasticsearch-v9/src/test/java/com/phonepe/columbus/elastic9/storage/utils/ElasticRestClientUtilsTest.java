package com.phonepe.columbus.elastic9.storage.utils;

import com.phonepe.columbus.common.http.Endpoint;
import com.phonepe.columbus.elastic9.internal.storage.utils.ElasticRestClientUtils;
import com.phonepe.columbus.elastic9.storage.configuration.auth.Security;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ElasticRestClientUtilsTest {

    @Test
    @DisplayName("endpoint to http host")
    void testToHttpHost() {
        final var endpoint = Endpoint.builder().host("localhost").build();
        final var httpHost = ElasticRestClientUtils.toHttpHost(endpoint);

        assertEquals("localhost", httpHost.getHostName(), "hostname must match");
    }

    @Test
    @DisplayName("credentials is null when auth is null")
    void testNullCredentials() {
        assertNull(ElasticRestClientUtils.getCredentialsProvider(null), "no credentials must be created");
    }

    @Test
    @DisplayName("ssl context is null when security configuration is null")
    void testNullSslContext() {
        assertNull(ElasticRestClientUtils.getSslContext(null), "no ssl context must be created");
    }

    @Test
    @DisplayName("create valid ssl context")
    void testCreateSslContext() {
        @SuppressWarnings("SpellCheckingInspection")
        final var security = Security.builder()
                .trustStorePath(SystemUtils.JAVA_HOME + "/lib/security/cacerts")
                .keyStoreType("jks")
                .keyStorePassword("changeit")
                .build();
        final var sslContext = ElasticRestClientUtils.getSslContext(security);

        assertNotNull(sslContext, "must create a valid ssl context");
    }

    @Test
    @DisplayName("ssl context creation failure")
    void testCreateInvalidSslContext() {
        @SuppressWarnings("SpellCheckingInspection")
        final var security = Security.builder()
                .trustStorePath(SystemUtils.JAVA_HOME + "/lib/security/cacerts")
                .keyStoreType("jks")
                .keyStorePassword("invalid")
                .build();
        assertThrows(IllegalArgumentException.class, () -> ElasticRestClientUtils.getSslContext(security),
                "invalid password should throw error");
    }
}
