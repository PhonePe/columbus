package com.phonepe.columbus.elastic7.internal.search.aggregation;

import lombok.Builder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.sampler.ParsedSampler;

public class ElasticSampler extends ParsedSampler {

    @Builder
    public ElasticSampler(final String name, final long count, final Aggregations aggregations) {
        super();
        setName(name);
        setDocCount(count);
        this.aggregations = aggregations;
    }
}
