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
