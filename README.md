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

## Features

1. A collection of building blocks and design patterns creating the [Framework](columbus-framework/README.md).
2. A skeleton for storages that support CRUD and search, and their contracts, defined
   in [Core](columbus-core/README.md), and the data models defined in [Models](columbus-models/README.md).
    1. Support for single and bulk CRUD requests.
    2. Support for filters, sorters, aggregations, scoring, pagination, etc.
3. Easily integrate with Dropwizard application using [Bundle](columbus-bundle/README.md).
4. An implementation of storage over [ElasticSearch](columbus-elasticsearch/README.md).
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
    <groupId>com.phonepe.platform.columbus</groupId>
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
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-bundle</artifactId>
</dependency>
```

The features provided by each module are specified in their README, linked in the [Features](#features) section above.

## Usage

With Dropwizard, the recommended way is to use the [bundle](columbus-bundle/README.md#usage).

To directly setup Columbus without Dropwizard (some features might not work), refer
[here](columbus-core/README.md#initialization-and-usage).

## Documentation

TODO

## Testing

TODO

## Contributing

TODO
