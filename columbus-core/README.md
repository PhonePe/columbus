# Columbus Core

All the main features provided by Columbus.

## Table of Contents

1. [Dependency](#dependency)
2. Features
    1. [Toolkit](#toolkit)
    2. [Storage](#storage)
3. [Initialization and Usage](#initialization-and-usage)
4. [Extensions](#extensions)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-core</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../README.md#dependency).

---

## Toolkit

A collection of all the building blocks used by Columbus.

An initializer ([ToolkitInitializer](/src/main/java/com/phonepe/platform/columbus/core/toolkit/ToolkitInitializer.java))
is used to initialize the toolkit. It initializes the value lazily, using the provided fields.

### Usage

Create a new initializer and provide the fields via the `use()` methods.

```java
ToolkitInitializer initializer = ToolkitHelpers.initializer();
initializer.use(/* create */ typeRegistry);
initializer.use(/* create */ mapper);
initializer.use(/* create */ settings);

Toolkit toolkit = initializer.get();
```

Get a default instance.

```java
Toolkit toolkit = ToolkitHelpers.defaultInstance();
```

### Advanced Usage

The initializer can be completely customized by providing a custom implementation extending
[DefaultToolkitInitializer](/src/main/java/com/phonepe/platform/columbus/internal/core/toolkit/DefaultToolkitInitializer.java)
or its parents. The lazy initializers allow for different dependencies to exist without having to modify the order of
execution of the methods.

---

## Storage

A storage client allows Columbus to communicate with some database to store the data, query it and operate on it as
needed.

The storage client is modeled as a `CommandExecutor` with different storage operations modeled as `Command`s. This not
only makes it easy to separate the different operations and their logic, but also allows adding custom operations
supported by any special storages, without affecting other storages.

#### Factory

A `StorageClient` can be created from its `StorageConfiguration` using the factory. The default implementation uses
visitors on the configuration to resolve a `StorageClientProvider` which is registered to create a client from the
corresponding configuration.

New storages can be registered by creating a configuration extending `StorageConfiguration`, a client implementing
`StorageClient` and a provider extending `StorageClientProvider`.

See [MemoryStorageProvider](../columbus-test-suite/columbus-core-test-suite/src/main/java/com/phonepe/platform/columbus/test/storage/memory/factory/MemoryStorageProvider.java)
for an example.

### Usage

```java
StorageConfiguration configuration = ...; // create the configuration
StorageClient storage = ...;  // create a client using a configuration.
VersionCommand command = VersionCommand.builder().build();
String version = command.execute(storage);  // or storage.execute(command)
```

The storage can also be created using a factory.

```java
Toolkit toolkit = ToolkitHelpers.defaultInstance();
StorageSettings settings = StorageSettings.defaults();
StorageClientFactory factory = Storages.storageClientFactory(settings, toolkit);

StorageClient storage = factory.create(configuration);
```

### Advanced Usage

New commands can be created by simply extending `Command` class, and creating a `CommandHandler` by extending
the `StorageCommandHandler` or its subtype corresponding to the `StorageClient` being used, and implement the `handle()`
method. See [VersionHandler](../columbus-test-suite/columbus-core-test-suite/src/main/java/com/phonepe/platform/columbus/test/storage/memory/handlers/VersionHandler.java)
for an example.

#### Factory

Each `StorageClientProvider` accepts a specific `StorageClientFactoryContext` which provides the necessary features
required to create the client. This context itself can be configured by extending it or one of its non-final subclasses,
with another abstract class and adding necessary members to it. Finally, create another final class extending this to
complete the hierarchy.

See
[ColumbusStorageFactoryContext](src/main/java/com/phonepe/platform/columbus/storage/factory/impl/ColumbusStorageFactoryContext.java)
and its final subclass
[ColumbusStorageClientFactory](src/main/java/com/phonepe/platform/columbus/internal/storage/factory/ColumbusStorageClientFactory.java)
for an example.

The visitor will only pick those `StorageClientProvider`s which can work with the provided context class.

---

## Initialization and Usage

Columbus requires settings, configuration, and some additional attributes like `ObjectMapper`, during initialization.
It is handled by `ColumbusInitializer` and is designed to perform the initialization lazily, as and when the attributes
become available, depending on the framework being integrated with.

```java
ColumbusInitializer initializer = new DefaultColumbusInitializer();
initializer.use(/* create */ settings);
initializer.use(/* create */ mapper);
initializer.use(/* create */ configuration);

Columbus columbus = initializer.get();

columbus.toolkit()                  // returns the initialized, underlying toolkit
columbus.storages()                 // returns all the storages
columbus.storage("<storage-name>")  // returns the storage with the given name
```

---

## Extensions

This section contains additional details for extending Columbus with new entities to enrich the behaviour. Please read
thoroughly.

### Visitor extensions

Columbus' visitors are extensively used to ensure easy addition of new entities to the hierarchy. Each such collection
of entities has a subclass of `VisitorContext` to visit them for a specific use-case, and a collection
of `EntityVisitor`s with the logic for each entity being visited. Some of these are storage specific, while some others
can work across all storages.

As a convention, this module contains a base class for context, and an interface for visitor, for each use-case that
needs visitors. If the use-case requires storage specific handling, there are subclasses / sub-interfaces in the
storage module.

To extend any of these entity collections, the following steps can be followed:
1. Create a new implementation of the base class / interface of the entity collection.
2. Identify the `VisitorContext`s that visit this collection of entities. Each direct subclass likely represents a
   different use-case. For each use-case, follow the below steps.
3. Determine the topmost subclass of `VisitorContext` needed to perform the operation. Identify the sub-interface
   of `EntityVisitor` which uses this context.
4. Create a new implementation of `EntityVisitor` (or its sub-interface) with the required subclass of `VisitorContext`.
5. Annotate this class with `@RegistryKey(...)` to ensure it gets picked up during creation. Note that the package or
   classes might have to be added in `ReflectSettings`.

All the entities and the different use-cases for which they use visitors are listed below.

#### Storage Factory

|        Entity        | Use case                |           Context           |        Visitor        |
|:--------------------:|-------------------------|:---------------------------:|:---------------------:|
| StorageConfiguration | Create a storage client | StorageClientFactoryContext | StorageClientProvider |

#### Data Access

| Entity | Use case                            |         Context         |         Visitor         |
|:------:|-------------------------------------|:-----------------------:|:-----------------------:|
|   Id   | Convert to storage's representation |   IdResolutionContext   |   IdResolutionVisitor   |
| Index  | Convert to storage's representation | IndexResolutionContext  | IndexResolutionVisitor  |
| NodeId | Convert to storage's representation | NodeIdResolutionContext | NodeIdResolutionVisitor |

#### Search

|      Entity      | Use case                            |              Context               |                          Visitor                           |
|:----------------:|-------------------------------------|:----------------------------------:|:----------------------------------------------------------:|
| Query (& Filter) | Convert to storage's representation |     QueryTransformationContext     | QueryTransformationVisitor (& FilterTransformationVisitor) |
|       Sort       | Convert to storage's representation |     SortTransformationContext      |                 SortTransformationVisitor                  |
|  ScoreFunction   | Convert to storage's representation | ScoreFunctionTransformationContext |             ScoreFunctionTransformationVisitor             |
|    Aggregator    | Convert to storage's representation |  AggregatorTransformationContext   |              AggregatorTransformationVisitor               |
|    Aggregator    | Parse `Aggregation` from storage    | AggregationParserResolutionContext |             AggregationParserResolutionVisitor             |
