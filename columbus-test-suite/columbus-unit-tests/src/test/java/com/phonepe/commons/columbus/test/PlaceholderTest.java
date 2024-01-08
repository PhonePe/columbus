package com.phonepe.commons.columbus.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaceholderTest {

    @Test
    void testName() {
        assertEquals("PLACEHOLDER", new Placeholder() {}.name());
    }
}
