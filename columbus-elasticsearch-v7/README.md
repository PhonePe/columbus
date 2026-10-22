# Columbus ElasticSearch v7

Implement all storage and search features with ElasticSearch v7.

## Table of Contents

1. [Dependency](#dependency)
2. Features
   1. [Storage](#storage)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.columbus</groupId>
    <artifactId>columbus-elasticsearch-v7</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../README.md#dependency).

---

## Storage

This module provides a `StorageClient` that can be used to interact with ElasticSearch. An instance can be created from
its `ElasticConfiguration`. The client currently uses ElasticSearch's `RestHighLevelClient` to connect to the cluster.

#### Factory

`ElasticClientProvider` is registered as a factory to create Elastic clients from their configuration. It uses
`ElasticClientInitializer` to accept the different parameters and create an instance of `ColumbusElasticClient`.
The factory also registers health checks for the client.

### Usage

```java
ElasticConfiguration configuration = ...; // create the configuration
StorageClient storage = ColumbusElasticClient.builder()
        .resolver(CommandHandler.resolver(/* create registry */))
        .client(ElasticRestClientUtils.createClient(configuration))
        .configuration(configuration)
        .build();

// Use the storage to execute commands
String version = VersionCommand.builder().build().execute(storage);
```

Creating a storage client with a factory:

```java
DropwizardStorageClientFactory factory = DropwizardStorages.storageClientFactory(
        StorageSettings.defaults(), toolkit, environment);
// note that the environment is needed to bind with the Dropwizard application lifecycle

StorageClient storage = factory.create(configuration);  // returns ElasticClient
```
