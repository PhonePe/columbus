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
import org.elasticsearch.search.aggregations.metrics.ParsedStats;

public class ElasticStats extends ParsedStats {

    @Builder
    public ElasticStats(
            final String name,
            final long count,
            final double min,
            final double max,
            final double sum,
            final double avg
    ) {
        super();
        setName(name);
        this.count = count;
        this.min = min;
        this.max = max;
        this.sum = sum;
        this.avg = avg;
    }
}
