package com.phonepe.commons.columbus.elastic.internal.search.query.special;

import com.phonepe.commons.columbus.elastic.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.special.KnnQuery;
import org.elasticsearch.index.query.QueryBuilder;

@RegistryKey(name = KnnQuery.TYPE, type = KnnQuery.class)
public class KnnQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<KnnQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final KnnQuery query, final Object props, final C context) {
        throw new UnsupportedOperationException("Knn query is not supported in this ES version");
    }
}