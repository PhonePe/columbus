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
package com.phonepe.columbus.search.misc.text;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Terms {

    /**
     * Indicates if all the (generated) terms must match or the minimum specified by {@link #minimumMatch}.
     */
    private Boolean matchAllTerms;

    /**
     * The minimum number of terms that must match for each document. Default behaviour effectively matches at least
     * one.
     */
    private String minimumMatch;
}
