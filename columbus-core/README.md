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
    <groupId>com.phonepe.commons.columbus</groupId>
    <artifactId>columbus-core</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../README.md#dependency).

---

## Toolkit

A collection of all the building blocks used by Columbus.

An initializer ([ToolkitInitializer](src/main/java/com/phonepe/platform/columbus/core/toolkit/ToolkitInitializer.java))
is used to create an instance of the toolkit. The object is configured lazily using the provided dependencies.

### Usage

Create a new initializer and provide the dependencies via the `use()` methods.

```java
ToolkitInitializer initializer = ToolkitHelpers.initializer();
initializer.use(/* create */ typeRegistry);
initializer.use(/* create */ mapper);
initializer.use(/* create */ settings);

Toolkit toolkit = initializer.get();
```

### Advanced Usage

The initializer can be completely customized by providing a custom implementation extending
[DefaultToolkitInitializer](src/main/java/com/phonepe/platform/columbus/internal/core/toolkit/DefaultToolkitInitializer.java)
or its parents. The lazy initializers allow for different dependencies to exist without having to modify the order of
execution of the methods.

---

## Storage

A storage client allows Columbus to communicate with some database to store the data, query it and operate on it as
needed.

The storage client is modeled as a `CommandExecutor` with different storage operations modeled as `Command`s. This not
only makes it easy to separate the different operations and their logic, but also allows adding custom operations
supported by any special storages, without affecting other storages.

#### Commands

Columbus defines a set of commands commonly used to store and operate on the data in a storage.
See [index commands](src/main/java/com/phonepe/platform/columbus/storage/commands/index) to manage the index, and
[entity commands](src/main/java/com/phonepe/platform/columbus/storage/commands/entity) and their bulk variants to
manage the data.

New `Command`s can be created by implementing the interface, and registering an implementation of
`StorageCommandHandler` to execute it.

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
Toolkit toolkit = ...; // create a toolkit
StorageSettings settings = StorageSettings.defaults();
StorageClientFactory factory = Storages.storageClientFactory(settings, toolkit);

StorageClient storage = factory.create(configuration);
```

### Advanced Usage

New commands can be created by simply implementing the `Command` interface, and creating a `CommandHandler` by extending
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

### Settings

Columbus supports different parameters to be set during initialization to alter the behaviour. Some of them are:

* **Type Registry**: configure entity types to be resolved from names.
* **Toolkit > Reflect**: configure any packages / classes to be used by Columbus for registering command handlers,
  visitors, etc.
* **Storage > Registry**: configure the creation of registry for visitors and command handlers.
* **Storage > nameOverride**: configure different storage factory for same storage type but different storage name.
* **...Initializer** and **...Factory**: classes that actually handle the initialization / object creation. Providing a
  new instance gives full control to modify the behaviour.

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

To make a visitor fully functional, it needs a concrete implementation of `VisitorContext`. But due to the self
reference, once an implementation is concrete, it will also lose extensibility. It is hence recommended to always create
an abstract implementation with all the necessary features, and then create a concrete implementation of this new class
to support instantiation.

#### Extending `VisitorContext`

To extend any of the contexts, the following steps can be followed:
1. Identify the concrete subtype of `VisitorContext` being used for a use-case.
2. Find the appropriate parent class which has all the necessary features.
3. Create an abstract subclass of this class and add the new capabilities.
4. Create a concrete, final subclass of the newly created abstract class.
5. Use the new concrete class to create visitors.

An example for extension can be seen in subclasses of
[StorageClientFactoryContext](src/main/java/com/phonepe/platform/columbus/storage/factory/visitor/StorageClientFactoryContext.java)
with `ColumbusStorageClientFactory` being one of the concrete classes.

#### Extending `EntityVisitor`

To extend any of the entity collections, the following steps can be followed:
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
|    PageBounds    | Convert to storage's representation |       PageResolutionContext        |                   PageResolutionVisitor                    |

### Command Executor extensions

Columbus' commands and executors are an easy way to create two disjoint collections of objects - one representing the
different requests or operations, another representing different contexts in which the operations will be performed -
while keeping them compatible and extensible.

The commands used by Columbus implement the `Command` interface, and the executors implement the `Executor` interface.
For each executor, there is a collection of `CommandHandler`s which execute different commands in the context of this
executor. This separation of responsibilities allows addition of new commands and executors easily, from within the
module or outside.

Like visitors, to make an executor fully functional, it needs a concrete implementation of `Executor`. But due to the
self reference, once an implementation is concrete, it will also lose extensibility. It is hence recommended to always
create an abstract implementation with all the necessary capabilities, and then create a concrete implementation of
this new class to support instantiation.

#### Extending `Executor`

To create a new executor or extend an existing one, the following steps can be followed:
1. Identify the subtype of `Executor` which fits the use-case. For example, `BaseStorageClient` is an executor to
   execute operations on storage. If none fit, you're likely defining a new use case, use `BaseExecutor`.
2. Create an abstract subclass / implementation, retaining the self reference, and add any capabilities needed.
3. Create a concrete, final subclass of the newly created abstract class.
4. Use the new concrete class to create an executor.

#### Extending `Command`

To create a new command, the following steps can be followed:
1. Create a new implementation of `Command` (or its subtypes if any have been defined).
2. For each subtype of `Executor` the command is applicable for, follow the below steps.
3. Identify the topmost subtype of `Executor` that the command can be executed with.
4. Create a new implementation of `CommandHandler` (or its sub-interface / subtype) which works with this executor, to
   handle the command.
5. Annotate this class with `@RegistryKey(...)` to ensure it gets picked up during creation. Note that the package or
   classes might have to be added in `ReflectSettings`.
