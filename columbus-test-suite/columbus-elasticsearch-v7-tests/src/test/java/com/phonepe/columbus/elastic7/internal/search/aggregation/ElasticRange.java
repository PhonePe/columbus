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
import lombok.Singular;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;

import java.util.List;

public class ElasticRange extends ParsedRange {

    @Builder
    public ElasticRange(final String name, final @Singular List<ParsedBucket> buckets) {
        super();
        setName(name);
        this.buckets.addAll(buckets);
    }

    public static class ElasticBucket extends ParsedBucket {

        @Builder
        public ElasticBucket(
                final String key,
                final Double from,
                final Double to,
                final long docCount,
                final Aggregations aggregations
        ) {
            super();
            setKeyAsString(key);
            if (from != null) {
                this.from = from;
            }
            if (to != null) {
                this.to = to;
            }
            setDocCount(docCount);
            setAggregations(aggregations == null ? new Aggregations(List.of()) : aggregations);
        }
    }
}
