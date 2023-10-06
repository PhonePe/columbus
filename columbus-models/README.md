# Columbus Models

A collection of models used by Columbus APIs.

## Table of Contents

1. [Dependency](#dependency)
2. Features
   1. [Id](#id)
   2. [Index](#index)
   3. [Node Id](#node-id)

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

## Id

Represents a (mostly) unique identifier of an entity.

See [Ids](src/main/java/com/phonepe/platform/columbus/dao/id/Ids.java) for different ways to create them.

### Usage

```java
AutoId auto = Ids.auto();
ValueId value = Ids.value("entity-id");
```

### Advanced Usage

New representations can be created by implementing the `Id` interface. Columbus will support any id, if their
corresponding visitors are registered.

---

## Index

Represents an index or table that stores (homogeneous) entities.

See [Indices](src/main/java/com/phonepe/platform/columbus/dao/index/Indices.java) for different ways to create them.

### Usage

```java
IndexName name = Indices.name("index-name");
IndexReference reference = Indices.reference("key");
```

### Advanced Usage

New representations can be created by implementing the `Index` interface. Columbus will support any index, if their
corresponding visitors are registered.

---

## Node Id

Represents a routing logic to a part of an index. This could be used to represent partitioning, sharding, replication,
and their combinations.

See [NodeIds](src/main/java/com/phonepe/platform/columbus/dao/node/NodeIds.java) for different ways to create them.

### Usage

```java
ReplicaId replica = NodeIds.replica("replica");
ShardId shard = NodeIds.shard("shard");
RoutingKey routing = NodeIds.routingKeys(List.of("route"));
ReplicatedShardId shard = NodeIds.shard("shard", "replica");
ReplicatedRoutingKey routing = NodeIds.routingKeys(List.of("route"), "replica");
```

### Advanced Usage

New representations can be created by implementing the `NodeId` interface. Columbus will support any routing logic, if
their corresponding visitors are registered.
