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

1. Easily integrate with Dropwizard application using [Bundle](columbus-bundle/README.md)

## Requirements

1. Java 17
2. (Optional) Dropwizard 2.x

## Dependency

Columbus is a multi-module project with different modules giving different features.
To ensure consistent version across all dependencies, it is recommended to import the bom.

Add the following dependency in `dependencyManagement` (of root pom):

```xml
<dependency>
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-bom</artifactId>
    <version>2.0.0-SNAPSHOT</version>
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

The features provided by each module are specified in their README, linked in [Features](#features) section above.

## Usage

TODO

## Documentation

TODO

## Testing

TODO

## Contributing

TODO
