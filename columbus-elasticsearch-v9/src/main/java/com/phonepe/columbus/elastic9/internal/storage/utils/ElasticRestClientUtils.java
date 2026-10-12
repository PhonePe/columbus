package com.phonepe.columbus.elastic9.internal.storage.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest5_client.Rest5ClientTransport;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import co.elastic.clients.transport.rest5_client.low_level.Rest5ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.common.http.Endpoint;
import com.phonepe.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.HostConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.RequestConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.auth.AuthConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.auth.Credentials;
import com.phonepe.columbus.elastic9.storage.configuration.auth.Security;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.BasicUserPrincipal;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.ssl.SSLContexts;

import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.phonepe.columbus.common.Predicates.applyIf;
import static com.phonepe.columbus.common.Predicates.applyIfNotNull;
import static com.phonepe.columbus.common.Predicates.applyIfPositive;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * A collection of helper methods to create ElasticSearch client.
 */
@UtilityClass
public class ElasticRestClientUtils {
    public ElasticsearchClient createClient(final ElasticConfiguration configuration,
                                            final ObjectMapper mapper) {
        final var auth = Optional.ofNullable(configuration.getAuth());
        final var sslContext = auth.map(AuthConfiguration::getTls)
                .map(ElasticRestClientUtils::getSslContext)
                .orElse(null);

        final var client = Rest5Client.builder(getHosts(configuration))
                .setRequestConfigCallback(RequestConfigCallback.fromConfiguration(configuration))
                .setHttpClientConfigCallback(HttpClientConfigCallback.fromConfiguration(configuration))
                .setConnectionManagerCallback(ConnectionConfigManagerCallback.fromConfiguration(configuration))
                .setConnectionConfigCallback(ConnectionConfigCallback.fromConfiguration(configuration));

        applyIfNotNull(sslContext, client::setSSLContext);

        configureClientConnection(client, configuration);

        final JsonpMapper jsonpMapper = new JacksonJsonpMapper(mapper);

        return new ElasticsearchClient(new Rest5ClientTransport(client.build(), jsonpMapper));
    }

    public HttpHost[] getHosts(final HostConfiguration hosts, @Nullable final AuthConfiguration auth) {
        final var scheme = getScheme(auth);
        return hosts.getEndpoints().stream()
                .map(endpoint -> toHttpHost(endpoint, scheme))
                .toArray(HttpHost[]::new);
    }

    public HttpHost[] getHosts(final ElasticConfiguration configuration) {
        return getHosts(configuration.getHosts(), configuration.getAuth());
    }

    public String getScheme(@Nullable final AuthConfiguration auth) {
        return Optional.ofNullable(auth)
                .map(AuthConfiguration::getTls)
                .map(tls -> "https")
                .orElse("http");
    }

    public HttpHost toHttpHost(final Endpoint endpoint) {
        return toHttpHost(endpoint, null);
    }

    public HttpHost toHttpHost(final Endpoint endpoint, final String scheme) {
        return new HttpHost(
                firstNonNull(endpoint.getScheme(), scheme),
                endpoint.getHost(),
                endpoint.getPort());
    }

    public Header[] toHeaders(final List<com.phonepe.columbus.common.http.Header> headers) {
        return headers.stream()
                .map(header -> new BasicHeader(header.getName(), header.getValue()))
                .toArray(Header[]::new);
    }

    public void configureClientConnection(final Rest5ClientBuilder client, final ElasticConfiguration configuration) {
        applyIfNotNull(configuration.getRequestConfig(), request ->
                applyIf(request.getDefaultHeaders(), CollectionUtils::isNotEmpty,
                        headers -> client.setDefaultHeaders(toHeaders(headers))));

        final var hosts = configuration.getHosts();
        applyIf(hosts.getPathPrefix(), StringUtils::isNotBlank, client::setPathPrefix);
        if (hosts.isCompressionEnabled()) {
            client.setCompressionEnabled(true);
        }
    }

    public CredentialsProvider getCredentialsProvider(@Nullable final Credentials configuration) {
        if (configuration == null) {
            return null;
        }
        final var provider = new BasicCredentialsProvider();
        final var credentials = new UsernamePasswordCredentials(
                new BasicUserPrincipal(configuration.getUsername()),
                configuration.getPassword().toCharArray()
        );
        provider.setCredentials(new AuthScope(null, -1), credentials);
        return provider;
    }

    public SSLContext getSslContext(@Nullable final Security configuration) {
        if (configuration == null) {
            return null;
        }
        final var trustStorePath = Paths.get(configuration.getTrustStorePath());
        try (final var inputStream = Files.newInputStream(trustStorePath)) {
            final var trustStore = KeyStore.getInstance(configuration.getKeyStoreType());
            trustStore.load(inputStream, configuration.getKeyStorePassword().toCharArray());
            return SSLContexts.custom().loadTrustMaterial(trustStore, null).build();
        } catch (final Exception e) {
            throw new IllegalArgumentException("Failed to create SSL context", e);
        }
    }

    public String createHealthCheckName(final String name) {
        return name.replaceAll("[^-a-zA-Z0-9_]+", "-") + "-elastic-health-check";
    }

    @AllArgsConstructor
    public static class HttpClientConfigCallback implements Consumer<HttpAsyncClientBuilder> {

        private final RequestConfiguration requestConfiguration;
        private final CredentialsProvider credentialsProvider;

        public static HttpClientConfigCallback fromConfiguration(final ElasticConfiguration configuration) {
            final var auth = Optional.ofNullable(configuration.getAuth());
            final var credentials = auth.map(AuthConfiguration::getCredentials)
                    .map(ElasticRestClientUtils::getCredentialsProvider)
                    .orElse(null);
            return new HttpClientConfigCallback(configuration.getRequestConfig(), credentials);
        }

        @Override
        public void accept(final HttpAsyncClientBuilder builder) {
            applyIfNotNull(credentialsProvider, builder::setDefaultCredentialsProvider);

            applyIfNotNull(requestConfiguration, request -> {
                applyIf(request, RequestConfiguration::isSystemPropertiesEnabled,
                        ignored -> builder.useSystemProperties());
                applyIfNotNull(request.getProxy(), ElasticRestClientUtils::toHttpHost, builder::setProxy);
                applyIfNotNull(request.getUserAgent(), builder::setUserAgent);
            });
        }
    }

    @AllArgsConstructor
    public static class RequestConfigCallback implements Consumer<RequestConfig.Builder> {

        private final RequestConfiguration configuration;

        public static RequestConfigCallback fromConfiguration(final ElasticConfiguration configuration) {
            return new RequestConfigCallback(configuration.getRequestConfig());
        }

        @Override
        public void accept(final RequestConfig.Builder builder) {
            applyIfNotNull(configuration, requestConfiguration -> {
                if (configuration.getConnectionRequestTimeout() > 0) {
                    builder.setConnectionRequestTimeout(configuration.getConnectionRequestTimeout(), TimeUnit.SECONDS);
                }
            });
        }
    }

    @AllArgsConstructor
    public static class ConnectionConfigManagerCallback implements Consumer<PoolingAsyncClientConnectionManagerBuilder> {
        private final RequestConfiguration configuration;

        public static ConnectionConfigManagerCallback fromConfiguration(final ElasticConfiguration configuration) {
            return new ConnectionConfigManagerCallback(configuration.getRequestConfig());
        }

        @Override
        public void accept(final PoolingAsyncClientConnectionManagerBuilder builder) {
            applyIfNotNull(configuration, requestConfiguration -> {
                applyIfPositive(configuration.getMaxTotalConnections(), builder::setMaxConnTotal);
                applyIfPositive(configuration.getMaxConnectionsPerRoute(), builder::setMaxConnPerRoute);
            });
        }
    }

    @AllArgsConstructor
    public static class ConnectionConfigCallback implements Consumer<ConnectionConfig.Builder> {
        private final RequestConfiguration configuration;

        public static ConnectionConfigCallback fromConfiguration(final ElasticConfiguration configuration) {
            return new ConnectionConfigCallback(configuration.getRequestConfig());
        }

        @Override
        public void accept(final ConnectionConfig.Builder builder) {
            applyIfNotNull(configuration, requestConfiguration -> {
                if (configuration.getConnectTimeout() > 0) {
                    builder.setConnectTimeout(configuration.getConnectTimeout(), TimeUnit.SECONDS);
                }
            });
        }
    }
}
