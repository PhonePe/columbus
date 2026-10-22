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
package com.phonepe.columbus.elastic7.internal.dao.id;

import com.phonepe.columbus.dao.id.AutoId;
import com.phonepe.columbus.elastic7.dao.id.ElasticId;
import com.phonepe.columbus.elastic7.dao.id.ElasticIdResolutionContext;
import com.phonepe.columbus.elastic7.dao.id.IdToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = AutoId.TYPE, type = AutoId.class)
public class AutoIdToElastic<C extends ElasticIdResolutionContext<C>> implements IdToElastic<AutoId, C> {

    @Override
    public ElasticId visit(final AutoId id, final Object props, final C context) {
        return ElasticId.builder().build();
    }
}
