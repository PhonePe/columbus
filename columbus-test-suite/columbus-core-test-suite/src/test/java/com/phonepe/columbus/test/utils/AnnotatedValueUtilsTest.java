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
