# Columbus ElasticSearch Test Suite

A collection of utilities to write tests while using ElasticSearch with Columbus.

## Table of Contents

1. [Dependency](#dependency)
2. [Extensions](#extensions)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.columbus</groupId>
    <artifactId>columbus-elasticsearch9-test-suite</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../../README.md#dependency).

---

## Extensions

Adds support for initializing a test container with ElasticSearch, and a client for the same.
