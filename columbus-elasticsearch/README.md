# Columbus ElasticSearch

Implement all storage and search features with ElasticSearch.

## Table of Contents

1. [Dependency](#dependency)
2. Features
   1. [Storage](#storage)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-elasticsearch</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../README.md#dependency).

---

## Storage

This module provides a `StorageClient` that can be used to interact with ElasticSearch. An instance can be created from
its `ElasticConfiguration`. The client currently uses ElasticSearch's `RestHighLevelClient` to connect to the cluster.

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
