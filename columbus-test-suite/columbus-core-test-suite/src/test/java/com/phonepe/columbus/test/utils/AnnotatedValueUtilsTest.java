package com.phonepe.columbus.test.utils;

import com.phonepe.columbus.test.annotations.WithMockito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@WithMockito
class AnnotatedValueUtilsTest {

    @Mock
    private Store store;

    @Test
    @DisplayName("get required value failure")
    void testGetRequiredValueFailure() {
        doReturn(null).when(store).get(any(), any());

        assertThrows(
                NullPointerException.class,
                () -> AnnotatedValueUtils.getRequiredValue(store, "key", null, Object.class));
    }
}
