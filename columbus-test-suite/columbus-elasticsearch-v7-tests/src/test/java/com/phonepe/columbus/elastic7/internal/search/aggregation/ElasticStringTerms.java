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
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;

import java.util.List;

public class ElasticStringTerms extends ParsedStringTerms {

    @Builder
    public ElasticStringTerms(
            final String name,
            final @Singular List<ParsedBucket> buckets,
            final long countError,
            final long otherDocCounts
    ) {
        super();
        setName(name);
        this.buckets.addAll(buckets);
        this.docCountErrorUpperBound = countError;
        this.sumOtherDocCount = otherDocCounts;
    }

    public static class ElasticBucket extends ParsedBucket {

        @Builder
        public ElasticBucket(
                final String key,
                final long docCount,
                final long docCountError,
                final Aggregations aggregations
        ) {
            super();
            setKeyAsString(key);
            setDocCount(docCount);
            this.docCountError = docCountError;
            setAggregations(aggregations == null ? new Aggregations(List.of()) : aggregations);
        }
    }
}
