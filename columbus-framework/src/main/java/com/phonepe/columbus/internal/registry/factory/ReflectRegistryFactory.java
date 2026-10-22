/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.internal.registry.factory;

import com.phonepe.columbus.common.pair.Pair;
import com.phonepe.columbus.common.pair.PairFunctions;
import com.phonepe.columbus.reflect.InstanceFactory;
import com.phonepe.columbus.reflect.Reflect;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.registry.Registry;
import com.phonepe.columbus.registry.factory.RegistryEntry;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.registry.factory.RegistryOptions;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A factory to create registry using {@link Reflect}. This factory utilizes the {@link RegistryKey} annotation on
 * the subclasses of the given type, to add them to the registry. The entry in the registry can be customized by
 * modifying the options provided.
 *
 * @implNote The mixins in {@link Reflect} will be used to fetch the annotations.
 * @see RegistryOptions
 * @see RegistryKey
 */
@Slf4j
@AllArgsConstructor(staticName = "with")
public class ReflectRegistryFactory implements RegistryFactory {

    @NonNull
    private final Reflect reflect;

    @Override
    public <K, V> Registry<K, V> create(final RegistryOptions<K, V> options) {
        final var entries = getRegistryEntries(options);
        final var resolvedEntries = resolvePriorityConflicts(entries, options.ignorePriorityConflicts());
        final var instances = createInstances(resolvedEntries, reflect.getInstanceFactory());
        logRegistry(instances);

        return Registries.stored(new HashMap<>(
                instances.stream().collect(Collectors.toMap(Pair::first, Pair::second))));
    }

    private <K, V> List<Pair<K, List<Class<? extends V>>>> getRegistryEntries(final RegistryOptions<K, V> options) {
        final var keyExtractor = options.keyExtractor();
        final var entries = reflect.getAnnotatedSubTypes(options.valueType(), RegistryKey.class)
                .stream()
                .filter(PairFunctions.filteringFirst(options.typeFilter()))
                .flatMap(pair -> pair.second().stream()
                        .map(key -> RegistryEntry.of(key, pair.first())))
                .map(entry -> Pair.of(keyExtractor.apply(entry), entry))
                .filter(PairFunctions.filteringFirst(options.keyValidator()))
                .collect(Collectors.groupingBy(
                        Pair::first,
                        Collectors.groupingBy(
                                pair -> pair.second().priority(),
                                Collectors.mapping(
                                        pair -> pair.second().value(),
                                        Collectors.<Class<? extends V>>toList()))));
        return entries.entrySet().stream()
                .map(entry -> Pair.of(
                        entry.getKey(),
                        entry.getValue().get(Collections.max(entry.getValue().keySet()))))
                .toList();
    }

    private static <K, V> List<Pair<K, Class<? extends V>>> resolvePriorityConflicts(
            final List<Pair<K, List<Class<? extends V>>>> entries,
            final boolean ignoreConflicts
    ) {
        final var conflicts = entries.stream()
                .filter(PairFunctions.filteringSecond(classes -> classes.size() > 1))
                .toList();
        if (!conflicts.isEmpty()) {
            final var messageBuilder = new StringBuilder("The following classes have conflicting entries:\n");
            conflicts.forEach(pair -> {
                messageBuilder.append(pair.first()).append(":\n");
                pair.second().forEach(type -> messageBuilder.append(" - ")
                        .append(type)
                        .append("\n"));
            });
            final var message = messageBuilder.toString();
            log.warn(message);
            if (!ignoreConflicts) {
                throw new IllegalArgumentException(message);
            }
        }
        return entries.stream()
                .<Pair<K, Class<? extends V>>>map(PairFunctions.transformingSecond(classes -> classes.get(0)))
                .toList();
    }

    private static <K, V> List<Pair<K, V>> createInstances(
            final List<Pair<K, Class<? extends V>>> entries,
            final InstanceFactory instanceFactory
    ) {
        return entries.stream()
                .<Pair<K, V>>map(PairFunctions.transformingSecond(instanceFactory::getInstance))
                .toList();
    }

    private static <K, V> void logRegistry(final List<Pair<K, V>> registry) {
        if (registry.isEmpty()) {
            log.debug("Registry is empty");
            return;
        }
        final var entries = registry.stream()
                .map(PairFunctions.transforming(
                        ReflectRegistryFactory::toReadableString,
                        ReflectRegistryFactory::toReadableString))
                .toList();
        final var length = 4 + entries.stream()
                .mapToInt(pair -> pair.first().length())
                .max()
                .orElse(0);
        final var message = new StringBuilder("Registry entries:\n");
        final var indent = " ".repeat(length);
        entries.forEach(pair -> message
                .append("    ")
                .append(pair.first())
                .append(indent, pair.first().length(), length)
                .append(" : ")
                .append(pair.second())
                .append("\n"));
        log.debug(message.toString());
    }

    private static String toReadableString(final Object value) {
        if (value instanceof String string) {
            return string;
        } else if (value instanceof Class<?> type) {
            return classToString(type);
        } else {
            return String.format("%s [@%d]", classToString(value.getClass()), System.identityHashCode(value));
        }
    }

    private static String classToString(final Class<?> type) {
        return type.getName().replaceAll("\\b(\\w)\\w*(?=\\.)", "$1");
    }
}
