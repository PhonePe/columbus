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
package com.phonepe.columbus.elastic9.internal.storage.utils;

import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.node.NodeId;
import com.phonepe.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;

@UtilityClass
public class ElasticClientUtils {

    private static final ElasticNode DEFAULT_ELASTIC_NODE = ElasticNode.builder().build();

    public String resolveId(final ElasticToolkit toolkit, final Id id) {
        return toolkit.idResolver().resolve(id).getId();
    }

    public String resolveIndexName(final ElasticToolkit toolkit, final Index index) {
        return toolkit.indexResolver().resolve(index).getName();
    }

    @NonNull
    public ElasticNode resolveNodeId(final ElasticToolkit toolkit, @Nullable final NodeId nodeId) {
        if (nodeId == null) {
            return DEFAULT_ELASTIC_NODE;
        }
        return toolkit.nodeIdResolver().resolve(nodeId);
    }

    public String resolveSingleRouting(final ElasticToolkit toolkit, @Nullable final NodeId nodeId) {
        if (nodeId == null) {
            return null;
        }
        return EntityTransforms.getSingleRouting(resolveNodeId(toolkit, nodeId));
    }
}
