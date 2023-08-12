# Columbus Test Suite

A collection of utilities to write tests with Columbus.

## Table of Contents

1. [Dependency](#dependency)
2. [Extensions](#extensions)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-test-suite</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../README.md#dependency).

---

## Extensions

Test classes usually require some dependencies that are used across test methods, and sometimes across test classes.
`ValueExtension` or `AnnotatedValueExtension` can be subclassed to provide the value and annotate the test class to
inject the value in tests and/or setup methods. `@ValueProps` can be used to modify how the data is uniquely identified.

See [MapperExtension](src/main/java/com/phonepe/platform/columbus/test/extensions/mapper/MapperExtension.java) and
[WithMapper](src/main/java/com/phonepe/platform/columbus/test/extensions/mapper/WithMapper.java) for a usage with no
additional dependencies.
See [SerDeExtension](src/main/java/com/phonepe/platform/columbus/test/extensions/serde/SerDeExtension.java) and
[WithSerDe](src/main/java/com/phonepe/platform/columbus/test/extensions/serde/WithSerDe.java) for a usage with
additional dependencies.

See [tests](src/test/java/com/phonepe/platform/columbus/test/extensions) for some usage examples.
