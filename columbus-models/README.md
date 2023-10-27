# Columbus Models

A collection of models used by Columbus APIs.

## Table of Contents

1. [Dependency](#dependency)
2. Features
   1. [Data Access](#data-access)
      1. [Id](#id)
      2. [Index](#index)
      3. [Node Id](#node-id)
   2. [Search](#search)
      1. [Query](#query)
      2. [Sort](#sort)
      3. [Score Function](#score-function)
      4. [Aggregation](#aggregation)
   3. [Advanced Usage](#advanced-usage)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-models</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../README.md#dependency).

---

## Data Access

### Id

Represents a (mostly) unique identifier of an entity.

See [Ids](src/main/java/com/phonepe/platform/columbus/dao/id/Ids.java) for different ways to create them.

#### Usage

```java
AutoId auto = Ids.auto();
ValueId value = Ids.value("entity-id");
```

### Index

Represents an index or table that stores (homogeneous) entities.

See [Indices](src/main/java/com/phonepe/platform/columbus/dao/index/Indices.java) for different ways to create them.

#### Usage

```java
IndexName name = Indices.name("index-name");
IndexReference reference = Indices.reference("key");
```

### Node Id

Represents a routing logic to a part of an index. This could be used to represent partitioning, sharding, replication,
and their combinations.

See [NodeIds](src/main/java/com/phonepe/platform/columbus/dao/node/NodeIds.java) for different ways to create them.

#### Usage

```java
ReplicaId replica = NodeIds.replica("replica");
ShardId shard = NodeIds.shard("shard");
RoutingKey routing = NodeIds.routingKeys(List.of("route"));
ReplicatedShardId shard = NodeIds.shard("shard", "replica");
ReplicatedRoutingKey routing = NodeIds.routingKeys(List.of("route"), "replica");
```

---

## Search

### Query

Represents a certain criteria to select a subset of data. This can be a simple leaf query, or a complex nested query.

A simplified set of queries are also defined as
[Filters](src/main/java/com/phonepe/platform/columbus/search/filter/Filter.java).

Columbus is capable of supporting any query / filter as required by the storage. The current set follows the Lucene and
ElasticSearch DSLs.

### Sort

Defines the order of the documents in the result.

For storages like ElasticSearch which can have multiple values indexed for the same field, `sortMode` can be specified
to define which value is to be used for sorting.

### Score Function

For storages that assign a score to each matching document to sort them, these functions define how the scores are
calculated.

### Aggregation

An `Aggregator` represents a way to summarize the documents by grouping them into buckets, applying mathematical
calculations, etc. The grouping aggregators can support additional aggregators within the groups.

Each `Aggregator` can produce a different `Aggregation` as its result.

---

## Advanced Usage

New representations can be created by implementing the corresponding interfaces. Columbus will support any
implementation, if their corresponding visitors are registered. Most of the entities above have a visitor per storage.

The visitors for each entity can be found [here](../columbus-core/README.md#extensions).
