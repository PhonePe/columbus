# Columbus Framework

The building blocks and utilities used by Columbus.

## Table of Contents

1. [Dependency](#dependency)
2. Features
   1. [Reflect](#reflect)
3. Utilities
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

## Reflect

A configurable set of utilities using reflections and generics.

### Usage

#### Initialization

```java
ReflectSettings settings = ReflectSettings.builder()
        // set other parameters
        .build();
ReflectFactory factory = new SimpleReflectFactory();
Reflect reflect = factory.create(settings);
```

#### Methods

Use the provided methods on `Reflect` to fetch annotations, subtypes, and subtypes with a given annotation.

### Advanced Usage

#### Reflection initialization

Reflect uses `SimpleReflectionInitializer` by default, but any other implementation can be provided
by specifying `initializerOverride` in `ReflectSettings`.

#### Mixins

Mixins allow modifying or adding annotations to a class that cannot be edited.

Create an interface and add the necessary annotations on it. Register this as a mixin for the class you wish to modify.
All annotation related operations will use the mixin's annotations instead of the main class.

```java
class ExternalNonModifiableClass {
    // ... members
}

@MyCustomAnnotation()
interface MyCustomMixin {
}

// while creating settings, do
settings.mixin(ExternalNonModifiableClass.class, MyCustomMixin.class);

// to get the type to use for annotation, use
reflect.getAnnotatedType(ExternalNonModifiableClass.class) // returns MyCustomMixin.class
```

Note that existing annotations on the main class will be ignored.

Fetching all the types with a given annotation also returns the types that have a registered mixin with the given
annotation.

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
