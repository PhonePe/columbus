# Columbus Bundle

A plugin to use all the features of Columbus within a Dropwizard application.

## Table of Contents

1. [Dependency](#dependency)
2. [Usage](#usage)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-bundle</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../README.md#dependency).

## Usage

Add `ColumbusBundleConfiguration` to your configuration class and YAML. Then add the bundle to bootstrap as shown below:

```java
ColumbusBundle bundle = ColumbusBundle.configure()
        // set the required parameters
        .build()
        .toBundle(config -> /* extract ColumbusBundleConfiguration from config */);

bootstrap.addBundle(bundle);
```
The different settings supported are defined [here](../columbus-core/README.md#settings).

The storage and other Columbus objects can be accessed from the bundle:
```java
Columbus columbus = bundle.columbus()

StorageClient storage = columbus.storage("<storage-name>");
SerDe serDe = columbus.toolkit().serDe();
...
```

For detailed usage of Columbus, refer to [columbus-core](../columbus-core/README.md)
