# Columbus Framework

The building blocks and utilities used by Columbus.

## Table of Contents

1. [Dependency](#dependency)
2. Utilities
   1. [Pair](#pair)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-framework</artifactId>
</dependency>
```

---

## Utilities

### Pair

A simple helper to store two values of any type and operate on them. The object is immutable, with various methods to
create new copies with modified values.

#### Examples

Create a pair object using static constructor or builder.

```java
Pair<String, Integer> pairConstructor = Pair.of("Hello", 5);
Pair<String, Integer> pairBuilder = Pair.<String, Integer>builder()
        .first("Hello")
        .second(5)
        .build();
// pairConstructor == pairBuilder == <"Hello", 5>
```

Modify a field using `with` functions.

```java
var pair = Pair.of("Hello", 5);           // = <"Hello", 5>
var pairFirst = pair.withFirst("World");  // = <"World", 5>
var pairSecond = pair.withSecond(7);      // = <"Hello", 7>
```

Create new pairs with modified values, and combine them.

```java
var pair = Pair.of("Hello", 5);                             // = <"Hello", 5>
var pairTransformed = pair.transformFirst(String::length);  // = <5, 5>
var same = pair.combine(Objects::equals);                   // = true
```

Use `PairFunctions` to help with functional usages in streams.

```java
pairs.stream()
        .filter(PairFunctions.filteringFirst(string -> string.length() > 4))
        .map(PairFunctions.transformingFirst(String::toLowerCase))
        .map(PairFunctions.combine(String::repeat))
        .mapToInt(String::length)
        .sum();
```
