package com.phonepe.platform.columbus.internal.reflect;

import com.phonepe.platform.columbus.reflect.InstanceFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConstructorInstanceFactoryTest {

    private static final InstanceFactory factory = new ConstructorInstanceFactory();

    @Test
    @DisplayName("get instance")
    void testGetInstance() {
        final var expected = new Sample();
        final var notExpected = new Sample(null);

        final var instance = factory.getInstance(Sample.class);

        assertEquals(expected, instance, "Factory uses default constructor");
        assertNotEquals(notExpected, instance, "Instance has fields initialized");
    }

    @Test
    @DisplayName("get instance from inaccessible constructor")
    void testGetInstanceInaccessible() {
        final var exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.getInstance(PrivateSample.class),
                "cannot instantiate from private constructor"
        );

        assertInstanceOf(NoSuchMethodException.class, exception.getCause());
    }

    @Test
    @DisplayName("get instance with null input")
    void testGetInstanceInvalidInput() {
        assertThrows(
                NullPointerException.class,
                () -> factory.getInstance(null),
                "cannot instantiate from null type"
        );
    }

    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Sample {

        private String value = "sample-value";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class PrivateSample {
    }
}
