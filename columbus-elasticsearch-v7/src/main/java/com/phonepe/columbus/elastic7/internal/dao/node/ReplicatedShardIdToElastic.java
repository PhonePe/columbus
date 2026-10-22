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
package com.phonepe.columbus.elastic7.internal.dao.node;

import com.phonepe.columbus.dao.node.ReplicatedShardId;
import com.phonepe.columbus.elastic7.dao.node.ElasticNode;
import com.phonepe.columbus.elastic7.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.columbus.elastic7.dao.node.NodeIdToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ReplicatedShardId.TYPE, type = ReplicatedShardId.class)
public class ReplicatedShardIdToElastic<C extends ElasticNodeIdResolutionContext<C>>
        implements NodeIdToElastic<ReplicatedShardId, C> {

    @Override
    public ElasticNode visit(final ReplicatedShardId node, final Object props, final C context) {
        return ElasticNode.builder()
                .preference(node.getReplica())
                .shard(node.getShard())
                .routing(node.getShard())
                .build();
    }
}
