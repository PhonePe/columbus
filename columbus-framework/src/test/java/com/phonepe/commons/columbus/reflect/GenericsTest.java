package com.phonepe.commons.columbus.reflect;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenericsTest {

    @Nested
    class SuperTypeTest {

        @Test
        @DisplayName("check valid super type")
        void testIsSuperType() {
            assertTrue(Generics.isSuperType(Object.class, String.class), "object is super type for all");
        }

        @Test
        @DisplayName("check invalid super type")
        void testIsNotSuperType() {
            assertFalse(Generics.isSuperType(Collection.class, Map.class), "collection is not a super type for map");
        }

        @Test
        @DisplayName("get super type towards")
        void testGetSuperType() {
            assertEquals(AbstractList.class, Generics.getSuperTypeTowards(ArrayList.class, List.class),
                    "array list extends abstract list which is also a subtype of list");
        }
    }

    @Nested
    class TypeParameterTest {

        @Test
        @DisplayName("sanity check")
        void testSubType() {
            final Value<?> value = new StringValue();
            assertEquals("string", value.get());
        }

        @Test
        @DisplayName("get the actual type parameter provided by a subtype")
        void testGetTypeParameter() {
            final var parameter = Generics.getTypeParameter(StringValue.class, Value.class.getTypeParameters()[0]);

            assertEquals(String.class, parameter, "type parameter is fetched");
        }

        @Test
        @DisplayName("get the bounds of type parameter provided by a subtype")
        void testGetTypeParameterVariable() {
            final var parameter = Generics.getTypeParameter(CollectionValue.class, Value.class.getTypeParameters()[0]);
            final var bound = new TypeReference<Collection<String>>() {}.getType();

            assertEquals(bound, parameter, "type variable bound is fetched");
        }

        private interface Value<T> {

            T get();
        }

        private static class StringValue implements Value<String> {

            @Override
            public String get() {
                return "string";
            }
        }

        private record CollectionValue<C extends Collection<String>>(C value) implements Value<C> {

            @Override
            public C get() {
                return value;
            }
        }
    }
}
