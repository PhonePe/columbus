# Columbus

A one stop solution for all search and discovery problems.

## Table of Contents

1. [Features](#features)
2. [Requirements](#requirements)
3. [Dependency](#dependency)
4. [Usage](#usage)
5. [Documentation](#documentation)
6. [Testing](#testing)
7. [Contributing](#contributing)
8. [Troubleshooting](#troubleshooting)

## Features

1. A collection of building blocks and design patterns creating the [Framework](columbus-framework/README.md).
2. A skeleton for storages that support CRUD and search, and their contracts, defined
   in [Core](columbus-core/README.md), and the data models defined in [Models](columbus-models/README.md).
    1. Support for single and bulk CRUD requests.
    2. Support for filters, sorters, aggregations, scoring, pagination, etc.
3. Easily integrate with Dropwizard application using [Bundle](columbus-bundle/README.md).
4. Storage implementations for [Elasticsearch v7](columbus-elasticsearch-v7/README.md)
   and [Elasticsearch v9](columbus-elasticsearch-v9/README.md).
5. Support for extending various components by simply implementing some interfaces and registering them.
6. A [Test Suite](columbus-test-suite) to easily write tests while using Columbus.

## Requirements

1. Java 17
2. Dropwizard 2.x

## Dependency

Columbus is a multi-module project with different modules giving different features.
To ensure consistent version across all dependencies, it is recommended to import the bom.

Add the following dependency in `dependencyManagement` (of root pom):

```xml
<dependency>
    <groupId>com.phonepe.columbus</groupId>
    <artifactId>columbus-bom</artifactId>
    <version>${columbus.version}</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

and then add the dependencies that are needed in `dependencies` of the corresponding poms.
For example, to use Columbus bundle, add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.columbus</groupId>
    <artifactId>columbus-bundle</artifactId>
</dependency>
```

The features provided by each module are specified in their README, linked in the [Features](#features) section above.
The storage modules might have to be added as `dependencies` as well.

## Usage

With Dropwizard, the recommended way is to use the [bundle](columbus-bundle/README.md#usage).

To directly setup Columbus without Dropwizard (some features might not work), refer
[here](columbus-core/README.md#initialization-and-usage).

### Quickstart

Add a field in Dropwizard configuration to store a `ColumbusBundleConfiguration`.

```java
@Getter
public class AppConfiguration extends Configuration {
    @Valid
    @NotNull
    private final ColumbusBundleConfiguration columbus;
}
```

Provide the values in YAML config.

```yaml
columbus:
  enabled: true  # defaults to true
  storages:
    - type: ELASTIC
      enabled: true  # defaults to false
      name: es-cluster
      hosts:
        endpoints:
          - host: node-01.es-cluster.com
            port: 9200
          - host: node-02.es-cluster.com
            port: 9200

```

Create and add Columbus Bundle to bootstrap.

```java
final var columbusBundle = ColumbusBundle.configure()
        .build()
        .toBundle(AppConfiguration::columbus);

bootstrap.addBundle(columbusBundle);
```

Either store the bundle for later usage, or provide it to a Guice module for dependency injection.

Get the storage from bundle.

```java
final StorageClient storage = columbusBundle.storage("es-cluster");
```

Run commands as needed.

Create index

```java
final var index = Indices.name("scope.name.version.index");

CreateIndexCommand.builder()
        .index(index)
        .properties(IndexProperties.builder()
                // set properties
                .build())
        .build()
        .execute(storage);
```

Insert entities

```java
final var data = ...; // create object

InsertCommand.withValue(data)
        .index(index)
        .id(Ids.value(data.getId()))  // or any other way to get the id
        .build()
        .execute(storage);
```

Get entities

```java
final var id = Ids.value(...);

final var entity = GetCommand.forType(Types.forClass(/* data type */))
        .index(index)
        .id(id)
        .build()
        .execute(storage);

if (entity.isPresent()) {
    // do something
}
```

Search

```java
final var searchResult = SearchCommand.forType(Types.forClass(/* data type */))
        .index(index)
        .query(EqualsFilter.builder()
                .field("field-name")
                .value("value")
                .build())
        .sort(FieldSort.builder()
                .order(SortOrder.ASC)
                .field("sort-field")
                .build())
        .page(Pages.offset(10, 0))
        .build()
```

And more...

## Documentation

See the module guides for API details and examples:

- [Framework](columbus-framework/README.md): registries, visitors, commands, and serialization.
- [Models](columbus-models/README.md): data access and search models.
- [Core](columbus-core/README.md): storage commands, initialization, and extension points.
- [Bundle](columbus-bundle/README.md) and [Dropwizard](columbus-dropwizard/README.md): application integration.
- [Elasticsearch v7](columbus-elasticsearch-v7/README.md): storage configuration and usage.
- [Elasticsearch v9](columbus-elasticsearch-v9/README.md): storage configuration and Java API Client integration.
- Test utilities: [Core](columbus-test-suite/columbus-core-test-suite/README.md),
  [Elasticsearch v7](columbus-test-suite/columbus-elasticsearch-v7-test-suite/README.md), and
  [Elasticsearch v9](columbus-test-suite/columbus-elasticsearch-v9-test-suite/README.md).

## Testing

Use Java 17 and Maven, and run commands from the repository root.
Elasticsearch tests use Testcontainers and require a running Docker-compatible
container runtime with access to pull Elasticsearch images.

```sh
# Run all tests.
mvn clean test

# Run a module and its required reactor modules (example: core).
mvn -pl columbus-core -am test

# Run tests and generate the aggregate coverage report.
mvn clean verify
```

Test results are written to each module's `target/surefire-reports/`.
The aggregate coverage report is at
`columbus-test-report/target/site/jacoco-aggregate/index.html`.

## Contributing

Please follow our [Code of Conduct](CODE_OF_CONDUCT.md) when participating in the project.

### License headers

Columbus is licensed under the [Apache License, Version 2.0](LICENSE). All Java source and test files must include the
license header. Before submitting changes, run from the repository root:

```sh
mvn license:format
git diff
mvn validate
```

The formatter updates files in place without duplicating matching headers; review and commit those changes. Normal
builds check headers during `validate` without modifying files. Configure IDE cleanup and formatting to preserve the
license comments, and retain any existing third-party attribution notices.

## Troubleshooting

Columbus uses Apache Commons for some of the utilities, and it might run into issues if the versions conflict.
The following error indicates that the `commons-lang3` version does not match.
```
java.lang.NullPointerException: Cannot invoke "Object.equals(Object)" because "toTypeArg" is null
```
Add the following to `dependencyManagement` to resolve it:
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.12.0</version>
</dependency>
```
