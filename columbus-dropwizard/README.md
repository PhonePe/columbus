# Columbus Dropwizard

Introduce Dropwizard features into Columbus.

## Table of Contents

1. [Dependency](#dependency)
2. [Features](#features)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-dropwizard</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../README.md#dependency).

## Features

### Initializer

Dropwizard's `Environment` is accessible to `ColumbusInitializer` to allow binding different components to the lifecycle
of the Dropwizard application.

### Storage

Provides a new `StorageClientFactory` which includes Dropwizard's `Environment`, making it easier to bind the
connections to the application lifecycle, and set up health checks.
