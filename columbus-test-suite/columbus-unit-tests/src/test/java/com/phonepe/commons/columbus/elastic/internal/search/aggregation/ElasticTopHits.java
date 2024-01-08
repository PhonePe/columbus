package com.phonepe.commons.columbus.elastic.internal.search.aggregation;

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

    public SearchHits getHits() {
        return searchHits;
    }
}
