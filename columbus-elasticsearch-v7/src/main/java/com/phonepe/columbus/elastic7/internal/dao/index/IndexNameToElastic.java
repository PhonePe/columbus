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
package com.phonepe.columbus.elastic7.internal.dao.index;

import com.phonepe.columbus.dao.index.IndexName;
import com.phonepe.columbus.elastic7.dao.index.ElasticIndex;
import com.phonepe.columbus.elastic7.dao.index.ElasticIndexResolutionContext;
import com.phonepe.columbus.elastic7.dao.index.IndexToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = IndexName.TYPE, type = IndexName.class)
public class IndexNameToElastic<C extends ElasticIndexResolutionContext<C>> implements IndexToElastic<IndexName, C> {

    @Override
    public ElasticIndex visit(final IndexName index, final Object props, final C context) {
        return ElasticIndex.builder()
                .name(index.getName())
                .build();
    }
}
