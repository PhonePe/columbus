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
package com.phonepe.columbus.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.internal.serde.JsonSerDe;
import com.phonepe.columbus.type.TypeResolver;
import lombok.experimental.UtilityClass;

/**
 * A factory class to create different types of serde.
 */
@UtilityClass
public class SerDes {

    public JsonSerDe json(final ObjectMapper mapper) {
        return JsonSerDe.builder()
                .mapper(mapper)
                .build();
    }

    public JsonSerDe json(final ObjectMapper mapper, final TypeResolver typeResolver) {
        return JsonSerDe.builder()
                .mapper(mapper)
                .typeResolver(typeResolver)
                .build();
    }
}
