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
package com.phonepe.columbus.search.page;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Cursor-based pagination using a pointer/token from a previous page for efficient deep pagination")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(PagePointer.TYPE)
public class PagePointer extends AbstractPageBounds {

    public static final String TYPE = "POINTER";

    @JsonPropertyDescription("Opaque pointer/cursor token from the previous page to continue pagination from")
    private Object pointer;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
