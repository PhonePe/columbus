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
package com.phonepe.columbus.elastic7.internal.search.aggregation;

import lombok.Builder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;

public class ElasticTopHits extends ParsedTopHits {

    private final SearchHits searchHits;

    @Builder
    public ElasticTopHits(final String name, final SearchHits hits) {
        super();
        setName(name);
        searchHits = hits;
    }

    @Override
    public SearchHits getHits() {
        return searchHits;
    }
}
