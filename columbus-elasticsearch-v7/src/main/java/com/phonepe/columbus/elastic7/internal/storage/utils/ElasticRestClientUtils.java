/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.elastic7.internal.storage.utils;

import com.phonepe.columbus.common.http.Endpoint;
import com.phonepe.columbus.elastic7.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic7.storage.configuration.HostConfiguration;
import com.phonepe.columbus.elastic7.storage.configuration.RequestConfiguration;
import com.phonepe.columbus.elastic7.storage.configuration.auth.AuthConfiguration;
import com.phonepe.columbus.elastic7.storage.configuration.auth.Credentials;
import com.phonepe.columbus.elastic7.storage.configuration.auth.Security;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.List;
import java.util.Optional;

import static com.phonepe.columbus.common.Predicates.applyIf;
import static com.phonepe.columbus.common.Predicates.applyIfNotNull;
import static com.phonepe.columbus.common.Predicates.applyIfPositive;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * A collection of helper methods to create ElasticSearch client.
 */
@UtilityClass
public class ElasticRestClientUtils {

    public RestHighLevelClient createClient(final ElasticConfiguration configuration) {
        final var client = RestClient.builder(getHosts(configuration))
                .setRequestConfigCallback(RequestConfigCallback.fromConfiguration(configuration))
                .setHttpClientConfigCallback(HttpClientConfigCallback.fromConfiguration(configuration));

        configureClientConnection(client, configuration);

        return new RestHighLevelClient(client);
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
                endpoint.getHost(),
                endpoint.getPort(),
                firstNonNull(endpoint.getScheme(), scheme));
    }

    public Header[] toHeaders(final List<com.phonepe.columbus.common.http.Header> headers) {
        return headers.stream()
                .map(header -> new BasicHeader(header.getName(), header.getValue()))
                .toArray(Header[]::new);
    }

    public void configureClientConnection(final RestClientBuilder client, final ElasticConfiguration configuration) {
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
                configuration.getUsername(),
                configuration.getPassword()
        );
        provider.setCredentials(AuthScope.ANY, credentials);
        return provider;
    }

    public SSLContext getSslContext(@Nullable final Security configuration) {
        if (configuration == null) {
            return null;
        }
        final var trustStorePath = Paths.get(configuration.getTrustStorePath());
        try (var inputStream = Files.newInputStream(trustStorePath)) {
            final var trustStore = KeyStore.getInstance(configuration.getKeyStoreType());
            trustStore.load(inputStream, configuration.getKeyStorePassword().toCharArray());
            return SSLContexts.custom().loadTrustMaterial(trustStore, null).build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create SSL context", e);
        }
    }

    public String createHealthCheckName(final String name) {
        return name.replaceAll("[^-a-zA-Z0-9_]+", "-") + "-elastic-health-check";
    }

    @AllArgsConstructor
    public static class HttpClientConfigCallback implements RestClientBuilder.HttpClientConfigCallback {

        private final RequestConfiguration requestConfiguration;
        private final SSLContext sslContext;
        private final CredentialsProvider credentialsProvider;

        public static HttpClientConfigCallback fromConfiguration(final ElasticConfiguration configuration) {
            final var auth = Optional.ofNullable(configuration.getAuth());
            final var sslContext = auth.map(AuthConfiguration::getTls)
                    .map(ElasticRestClientUtils::getSslContext)
                    .orElse(null);
            final var credentials = auth.map(AuthConfiguration::getCredentials)
                    .map(ElasticRestClientUtils::getCredentialsProvider)
                    .orElse(null);
            return new HttpClientConfigCallback(configuration.getRequestConfig(), sslContext, credentials);
        }

        @Override
        public HttpAsyncClientBuilder customizeHttpClient(final HttpAsyncClientBuilder builder) {
            applyIfNotNull(sslContext, builder::setSSLContext);
            applyIfNotNull(credentialsProvider, builder::setDefaultCredentialsProvider);

            applyIfNotNull(requestConfiguration, request -> {
                applyIfPositive(request.getMaxTotalConnections(), builder::setMaxConnTotal);
                applyIfPositive(request.getMaxConnectionsPerRoute(), builder::setMaxConnPerRoute);
                applyIf(request, RequestConfiguration::isSystemPropertiesEnabled,
                        ignored -> builder.useSystemProperties());
                applyIfNotNull(request.getProxy(), ElasticRestClientUtils::toHttpHost, builder::setProxy);
                applyIfNotNull(request.getUserAgent(), builder::setUserAgent);
            });

            return builder;
        }
    }

    @AllArgsConstructor
    public static class RequestConfigCallback implements RestClientBuilder.RequestConfigCallback {

        private final RequestConfiguration configuration;

        public static RequestConfigCallback fromConfiguration(final ElasticConfiguration configuration) {
            return new RequestConfigCallback(configuration.getRequestConfig());
        }

        @Override
        public RequestConfig.Builder customizeRequestConfig(final RequestConfig.Builder builder) {
            applyIfNotNull(configuration, request -> {
                applyIfPositive(request.getConnectionRequestTimeout(), builder::setConnectionRequestTimeout);
                applyIfPositive(request.getConnectTimeout(), builder::setConnectTimeout);
                applyIfPositive(request.getSocketTimeout(), builder::setSocketTimeout);
            });
            return builder;
        }
    }
}
