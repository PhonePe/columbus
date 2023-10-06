# Columbus Framework

The building blocks and utilities used by Columbus.

## Table of Contents

1. [Dependency](#dependency)
2. Features
   1. [Reflect](#reflect)
   2. [Registry](#registry)
   3. [Visitor](#visitor)
   4. [Command](#command)
   5. [SerDe](#serde) (Serializer - Deserializer)
3. Utilities
   1. [Pair](#pair)
   2. [Initializers](#initializers)
   3. [Entity type](#entity-type)

## Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.phonepe.platform.columbus</groupId>
    <artifactId>columbus-framework</artifactId>
</dependency>
```

Either add the version, or include the bom in `dependencyManagement` as specified [here](../README.md#dependency).

---

## Reflect

A configurable set of utilities using reflections and generics.

### Usage

#### Initialization

```java
ReflectSettings settings = ReflectSettings.builder()
        // set other parameters
        .build();
        // use ReflectSettings.defaults(); for default settings.
ReflectFactory factory = ReflectHelpers.reflectFactory();
Reflect reflect = factory.create(settings);
```

#### Methods

Use the provided methods on [`Reflect`](src/main/java/com/phonepe/platform/columbus/reflect/Reflect.java) to fetch
annotations, subtypes, and subtypes with a given annotation.

#### Utilities

See [`Generics`](src/main/java/com/phonepe/platform/columbus/reflect/Generics.java) for utilities on `Type`.

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

## Registry

A generic way to create a collection of key - value pairs of any type, focused more on configurability.

Very useful to dynamically create a collection of classes identified uniquely by some property. The same can be extended
in a type-safe way to configure different behaviors for different classes.

### Usage

#### Creation

```java
MutableRegistry<String, Integer> stored = Registries.stored(Map.of("zero", 0, "one", 1));
Registry<String, Integer> transforming = Registries.transformingKey(String::toLowerCase, stored);
RegistryChain<String, Integer> chain = Registries.chaining(stored, transforming).build();
```

#### Get

```java
Integer one = chain.get("One").orElseThrow(); // this will be 1
```

#### Register

```java
stored.register("two", 2);
Integer two = chain.get("two");
```

#### Types

Different types of registries that are available for use can be found in, and created
using, [Registries](src/main/java/com/phonepe/platform/columbus/registry/Registries.java).

### Advanced Usage

#### Monitoring

Use a `MonitoredRegistry` to know how a registry is performing. The successes and failures of `get()` calls can be
tracked by providing a listener.

#### Registry Factory

Registries whose values are instances of different types can be created using annotations. Annotate the different
classes with `@ReflectKey` and specify `name` and / or `type`. Use a `ReflectFactory` and `ReflectOptions` to collect
the classes and their annotations to create a registry.

See [BaseType](src/test/java/com/phonepe/platform/columbus/internal/registry/factory/BaseType.java) for an example.

See [RegistryHelpers](src/main/java/com/phonepe/platform/columbus/registry/RegistryHelpers.java) to create options for
specific registries.

---

## Visitor

A dynamic way to create and bind visitors with classes, without having to worry about cyclic dependencies. The classes
being visited need not be aware of the visitors, allowing new classes to be introduced into the two hierarchies
seamlessly, across modules.

### Usage

1. Define a context object extending `VisitorContext`, with fields for necessary data.
2. Define the visitor classes implementing `EntityVisitor` for each value that needs to be visited using the context.
   Create a common base class to help group the visitors for a context.
3. Determine a strategy to differentiate between classes being visited. Create a registry to resolve to the
   corresponding visitors.
4. Create resolver and context instances and use it.

See [Visitors](src/test/java/com/phonepe/platform/columbus/internal/visitor/Visitors.java) for an example.

### Advanced Usage

Use `VisitorRegistryFactory` to create registries of visitors using the annotation.
See [Registry factory](#registry-factory) for more details.

See [VisitorHelpers](src/main/java/com/phonepe/platform/columbus/visitor/VisitorHelpers.java) to create different
options and specific registries.

---

## Command

A command is an operation represented as data, performed by the executors. Each command might be executed differently
depending on the executor, but all executors are expected to return the same type.

This library provides an easy way to define commands and executors, and also extend them across modules.

### Usage

1. Define the `Command`s to represent different operations, with their corresponding return types.
2. Define the executor class to perform operations in different environments.
3. Define the handler classes implementing `CommandHandler` for each command that needs to be executed by the executor.
4. Determine a strategy to differentiate between all the commands and create a registry.
5. Create resolver and executor instances and use it.

See [Commands](src/test/java/com/phonepe/platform/columbus/internal/command/Commands.java) for an example.

### Advanced Usage

Use `HandlerRegistryFactory` to create registries of handlers using the annotation.
See [Registry factory](#registry-factory) for more details.

See [CommandHelpers](src/main/java/com/phonepe/platform/columbus/command/CommandHelpers.java) to create different
options and specific registries.

---

## SerDe

A serde is responsible for serializing data to some format and deserializing it. It also supports
the [Entity types](#entity-type) to allow deserializing types by just their names.

The library provides a definition for JSON format.

### Examples

Create a serde with the help of the factory.

```java
JsonSerDe serDe = SerDes.json(/* Jackson's ObjectMapper */ mapper, typeResolver);
```
See [SerDe](src/main/java/com/phonepe/platform/columbus/serde/SerDe.java) for the different methods supported.

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

### Initializers

An object that represents a value that can be initialized exactly once. This is similar to the behaviour of `final`,
except the value can be created after the constructor is called.

#### Late Initializer

An initializer which allows setting the value exactly once, in a thread-safe way.

##### Examples

Create with or without an initial value. If an initial value is not specified, the value can be set later, once.

```java
LateInit<String> empty = new LateInit<>();
LateInit<String> initialized = new LateInit<>("some-value");
```

Check the status of initialization.

```java
empty.isPresent()          // = false
initialized.isPresent()    // = true
```

Get the value from the wrapper.

```java
initialized.get()    // = "some-value"
empty.get()          // throws IllegalStateException
```

Set a value.

```java
empty.initialize("some-value")    // to know if the initialization was successful
empty.set("some-value")           // to set, or fail if it is already initialized
empty.initializeWith(             // create and set a value if not already initialized
        () -> "expensive-value")
```

#### Lazy Initializer

An object to create a value lazily. A precondition can be provided to check if the value can be initialized. The value
will be initialized when it is asked for if the precondition is satisfied.

##### Examples

Simple lazy value with a factory.

```java
LazyInit<Integer> lazyInt = new LazyInit<>(()-> /* calculate and return an integer */);
lazyInt.isPresent()   // returns true
lazyInt.get()         // returns the integer
```

A lazy value with a condition.

```java
LazyInit<Integer> lazyConditionalInt = new LazyInit<>(
    () -> /* some condition to check */ someValue.isPresent(),
    () -> /* calculate and return an integer */ someValue.get() + 10
);

// with someValue.isPresent() == false
lazyConditionalInt.isPresent()   // returns false
// modify someValue
lazyConditionalInt.isPresent()   // returns true
lazyConditionalInt.get()         // returns the computed value
```

### Entity Type

A wrapper over Java's `Type` instances to support referencing them by name, especially for serialization and
deserialization. A registry is used for resolving the names to their corresponding `Type`s.

#### Examples

Create an instance of `EntityType` in one of the following ways.

```java
JavaType<String> string = Types.forClass(String.class);
JavaType<List<Integer>> integers = Types.forTypeReference(new TypeReference<>() {});
NamedType<String> nameString = Types.forName("str");
```

Use a registry and a resolver to find the matching `JavaType` instance for a given `NamedType`.

```java
Map<String, JavaType<?>> types = Map.of("str", string);
TypeRegistry typeRegistry = Types.typeRegistry(Registries.stored(types));
TypeResolver resolver = Types.resolver(typeRegistry);

resolver.toJavaType(nameString) // = string: JavaType<String>
```
