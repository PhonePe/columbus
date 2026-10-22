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
package com.phonepe.columbus.dao.id;

import lombok.experimental.UtilityClass;

/**
 * A factory class to create different types of {@link Id} instances.
 */
@UtilityClass
public class Ids {

    private static final AutoId AUTO_ID = AutoId.builder().build();

    public ValueId value(final String value) {
        return ValueId.builder()
                .value(value)
                .build();
    }

    public AutoId auto() {
        return AUTO_ID;
    }
}
