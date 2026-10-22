# Columbus Elasticsearch v9

Implement storage and search features with Elasticsearch v9.

## Table of Contents

1. [Dependency](#dependency)
2. [Storage](#storage)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.columbus</groupId>
    <artifactId>columbus-elasticsearch-v9</artifactId>
</dependency>
```

Either add the version, or include the BOM in `dependencyManagement` as specified
[here](../README.md#dependency).

---

## Storage

This module provides a `StorageClient` that can be used to interact with Elasticsearch v9.
An instance can be created from its `ElasticConfiguration`. Unlike the v7 module,
it uses the Elasticsearch Java API Client (`ElasticsearchClient`) with
`Rest5ClientTransport`, rather than `RestHighLevelClient`.

The module's classes are in `com.phonepe.columbus.elastic9`. Configuration uses
the storage type `ELASTIC`, the same as the v7 module; select the appropriate
module for your cluster.

### Factory

`ElasticClientProvider` creates clients from their configuration using an
`ElasticClientInitializer`. The initializer creates the underlying Elasticsearch
client, an `ElasticToolkit`, and the command-handler resolver before building a
`ColumbusElasticClient`. The Dropwizard integration manages the client lifecycle
and registers health checks.

### Usage

Creating a storage client with a factory:

```java
ElasticConfiguration configuration = ...; // create the configuration

DropwizardStorageClientFactory factory = DropwizardStorages.storageClientFactory(
        StorageSettings.defaults(), toolkit, environment);
// The environment binds the client to the Dropwizard application lifecycle.

StorageClient storage = factory.create(configuration);

String version = VersionCommand.builder().build().execute(storage);
```

For manual construction using the internal implementation:

```java
ElasticConfiguration configuration = ...; // create the configuration
StorageClient storage = ColumbusElasticClient.builder()
        .resolver(resolver)
        .client(ElasticRestClientUtils.createClient(configuration, objectMapper))
        .toolkit(elasticToolkit)
        .configuration(configuration)
        .build();
```

Here, `resolver` is the storage command-handler resolver, `objectMapper` is the
application's configured Jackson mapper, and `elasticToolkit` is an initialized
`ElasticToolkit`. Unlike the v7 helper, `ElasticRestClientUtils.createClient`
requires the mapper explicitly. Manual construction also requires managing the
underlying client's lifecycle; prefer the factory for application integration.
