package com.phonepe.platform.columbus.core.search.sorter.elastic;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.utils.EsUtil;
import com.phonepe.platform.columbus.models.core.search.sorter.FieldSorter;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

@JsonTypeName(FieldSorter.TYPE)
public class ElasticFieldSorterBuilder implements ElasticSorterBuilder<FieldSorter> {
    @Override
    public FieldSortBuilder visit(FieldSorter sorter, ElasticSorterBuilderContext context) {
        return SortBuilders.fieldSort(sorter.getField())
            .order(EsUtil.esSortOrder(sorter.getOrder()));
    }
}
