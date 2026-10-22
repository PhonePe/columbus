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
package com.phonepe.columbus.internal.reflect;

import com.phonepe.columbus.reflect.InstanceFactory;
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
