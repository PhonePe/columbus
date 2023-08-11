# Columbus Core

All the main features provided by Columbus.

## Table of Contents

1. [Dependency](#dependency)
2. Features
    1. [Toolkit](#toolkit)
    2. [Storage](#storage)

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

### Usage

```java
StorageClient storage = ...;  // create a client using a configuration.
VersionCommand command = VersionCommand.builder().build();
String version = command.execute(storage);  // or storage.execute(command)
```

### Advanced Usage

New commands can be created by simply extending `Command` class, and creating a `CommandHandler` by extending
the `StorageCommandHandler` or its subtype corresponding to the `StorageClient` being used, and implement the `handle()`
method. See [VersionHandler](src/test/java/com/phonepe/platform/columbus/storage/memory/handlers/VersionHandler.java)
for an example.
