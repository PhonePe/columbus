# Columbus Core Test Suite

A collection of utilities to write tests with Columbus.

## Table of Contents

1. [Dependency](#dependency)
2. [Extensions](#extensions)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-core-test-suite</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../../README.md#dependency).

---

## Extensions

Test classes usually require some dependencies that are used across test methods, and sometimes across test classes.
`ValueExtension` or `AnnotatedValueExtension` can be subclassed to provide the value and annotate the test class to
inject the value in tests and/or setup methods. `@ValueProps` can be used to modify how the data is uniquely identified.

The values are stored in the extension context provided by Junit. The values are shared within the context, and are
inherited from parent contexts. A new value is created only if a value is not already present.

See [MapperExtension](src/main/java/com/phonepe/platform/columbus/test/extensions/MapperExtension.java) and
[WithMapper](src/main/java/com/phonepe/platform/columbus/test/annotations/WithMapper.java) for a usage with no
additional dependencies.
See [SerDeExtension](src/main/java/com/phonepe/platform/columbus/test/extensions/SerDeExtension.java) and
[WithSerDe](src/main/java/com/phonepe/platform/columbus/test/annotations/WithSerDe.java) for a usage with
additional dependencies.

Every annotation has 2 additional flavours for simpler cases:

- **Root**: the value is added to the root context, and is hence shared across all tests. Since the contexts are
  hierarchical in nature, this value could be used in any other class' context. It is always recommended to have a
  unique key for such values to avoid conflicts.
- **All**: the value is added to the current context, along with all its dependencies. This makes it easier to add a
  default value without having to add annotations of all its dependencies.

See [tests](src/test/java/com/phonepe/platform/columbus/test/extensions) for some usage examples.
