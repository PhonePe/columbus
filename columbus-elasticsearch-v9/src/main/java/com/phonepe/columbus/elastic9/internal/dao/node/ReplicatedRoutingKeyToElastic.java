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
package com.phonepe.columbus.elastic9.internal.dao.node;

import com.phonepe.columbus.common.Elements;
import com.phonepe.columbus.dao.node.ReplicatedRoutingKey;
import com.phonepe.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.columbus.elastic9.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.columbus.elastic9.dao.node.NodeIdToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ReplicatedRoutingKey.TYPE, type = ReplicatedRoutingKey.class)
public class ReplicatedRoutingKeyToElastic<C extends ElasticNodeIdResolutionContext<C>>
        implements NodeIdToElastic<ReplicatedRoutingKey, C> {

    @Override
    public ElasticNode visit(final ReplicatedRoutingKey node, final Object props, final C context) {
        return ElasticNode.builder()
                .routing(node.getKeys())
                .shard(Elements.firstOrNull(node.getKeys()))
                .preference(node.getReplica())
                .build();
    }
}
